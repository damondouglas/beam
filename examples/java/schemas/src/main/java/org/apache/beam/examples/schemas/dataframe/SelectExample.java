package org.apache.beam.examples.schemas.dataframe;

import org.apache.beam.examples.schemas.model.javabeanschema.Simple;
import org.apache.beam.sdk.schemas.transforms.DropFields;
import org.apache.beam.sdk.schemas.transforms.Select;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;

import java.util.Objects;

import static org.apache.beam.sdk.values.TypeDescriptors.*;

public class SelectExample extends PTransform<PCollection<Simple>, PCollection<KV<String, Integer>>> {
    @Override
    public PCollection<KV<String, Integer>> expand(PCollection<Simple> input) {
        return input
                .apply("Select aString, anInteger", Select.fieldNames("aString", "anInteger"))
                .apply("To KV", MapElements.into(kvs(strings(), integers())).via(row -> {
                    Row safeRow = Objects.requireNonNull(row);
                    return KV.of(safeRow.getString("aString"), safeRow.getInt32("anInteger"));
                }));
    }
}
