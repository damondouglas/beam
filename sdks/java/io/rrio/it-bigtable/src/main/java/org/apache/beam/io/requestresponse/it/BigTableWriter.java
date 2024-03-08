package org.apache.beam.io.requestresponse.it;

import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.bigtable.v2.Mutation;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.protobuf.ByteString;
import org.apache.beam.io.requestresponse.Caller;
import org.apache.beam.io.requestresponse.SetupTeardown;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.apache.beam.io.requestresponse.UserCodeQuotaException;
import org.apache.beam.io.requestresponse.UserCodeRemoteSystemException;
import org.apache.beam.io.requestresponse.UserCodeTimeoutException;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResult;
import org.apache.beam.sdk.values.KV;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.io.IOException;
import java.util.List;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

class BigTableWriter implements Caller<KV<ByteString, Iterable<Mutation>>, BigtableWriteResult>, SetupTeardown {

    private final BigTableIOUsingRRIO.WriteConfiguration configuration;
    private transient @MonotonicNonNull BigtableDataClient client;

    BigTableWriter(BigTableIOUsingRRIO.WriteConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setup() throws UserCodeExecutionException {
        try {
            this.client = BigtableDataClient.create(configuration.getProjectId(), configuration.getInstanceId());
        } catch (IOException e) {
            throw new UserCodeExecutionException(
                    String.format("error connecting to BigTable: projects/%s/instances/%s",
                            configuration.getProjectId(), configuration.getInstanceId()),
                    e);
        }
    }

    @Override
    public void teardown() throws UserCodeExecutionException {
        if (client != null) {
            client.close();
        }
    }

    @Override
    public BigtableWriteResult call(KV<ByteString, Iterable<Mutation>> request) throws UserCodeExecutionException {
        BulkMutation bulkMutation = BulkMutation.create(configuration.getTableId());
        for (Mutation mutation : request.getValue()) {
            Mutation.SetCell setCell = mutation.getSetCell();
            bulkMutation.add(
                    RowMutationEntry.create(request.getKey())
                            .setCell(setCell.getFamilyName(), setCell.getColumnQualifier(), setCell.getTimestampMicros(), setCell.getValue())
            );
        }
        try {
            checkStateNotNull(client).bulkMutateRows(bulkMutation);
        } catch(ApiException e) {
            StatusCode.Code code = e.getStatusCode().getCode();
            switch (code) {
                case INTERNAL:
                    throw new UserCodeRemoteSystemException(e);
                case RESOURCE_EXHAUSTED:
                    throw new UserCodeQuotaException(e);
                case DEADLINE_EXCEEDED:
                    throw new UserCodeTimeoutException(e);
                default:
                    throw new UserCodeExecutionException(e);
            }
        }

        return BigtableWriteResult.create(bulkMutation.getEntryCount());
    }
}
