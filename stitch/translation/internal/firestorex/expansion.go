package firestorex

import "cloud.google.com/go/firestore"

type Expansion struct {
	Configuration map[string]*firestore.DocumentRef `firestore:"configuration"`
	Description   string                            `firestore:"description"`
	Host          string                            `firestore:"host"`
	Input         *firestore.DocumentRef            `firestore:"input"`
	Name          string                            `firestore:"name"`
	Output        *firestore.DocumentRef            `firestore:"output"`
	Tags          []string                          `firestore:"tags"`
}
