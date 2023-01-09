package firestorex

import (
	"context"
	"fmt"
	"time"

	"cloud.google.com/go/firestore"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/log"
)

type Watcher struct {
	collection   *firestore.CollectionRef
	pollDuration time.Duration
	dispatcher   Dispatcher
}

func NewWatcher(ctx context.Context, collection *firestore.CollectionRef, pollDuration time.Duration, dispatcher Dispatcher) (*Watcher, error) {
	if collection == nil {
		return nil, fmt.Errorf("collection is nil")
	}
	ref, err := collection.DocumentRefs(ctx).Next()
	if err != nil {
		return nil, fmt.Errorf("error checking collection correctness: %w", err)
	}
	job, err := loadJob(ctx, ref)
	if err != nil {
		return nil, fmt.Errorf("error checking collection correctness: %w", err)
	}
	if job.Pipeline == nil {
		return nil, fmt.Errorf("error checking collection correctness: empty pipeline reference at job document: %+v", job)
	}
	if pollDuration < time.Second {
		return nil, fmt.Errorf("pollDuration must be >= %s", time.Second.String())
	}
	if dispatcher == nil {
		return nil, fmt.Errorf("dispatcher is nil")
	}
	return &Watcher{
		collection:   collection,
		pollDuration: pollDuration,
		dispatcher:   dispatcher,
	}, nil
}

func (watcher *Watcher) Watch(ctx context.Context) error {
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()
	log.Infof(ctx, "starting watch: %s", watcher.collection.Path)

	it := watcher.collection.Where("state", "==", jobSubmitted).Snapshots(ctx)
	ticker := time.Tick(watcher.pollDuration)
	for {
		select {
		case <-ticker:
			q, err := it.Next()
			if err != nil {
				it.Stop()
				return err
			}
			if err := watcher.dispatch(ctx, q); err != nil {
				it.Stop()
				return err
			}
		case <-ctx.Done():
			it.Stop()
			log.Info(ctx, "shutting down watch: %s", watcher.collection.Path)
			return nil
		}
	}
}
func (watcher *Watcher) dispatch(ctx context.Context, snap *firestore.QuerySnapshot) error {
	docs, err := snap.Documents.GetAll()
	if err != nil {
		return err
	}
	for _, doc := range docs {
		if err := watcher.dispatcher.Dispatch(ctx, doc); err != nil {
			return err
		}
	}
	return nil
}
