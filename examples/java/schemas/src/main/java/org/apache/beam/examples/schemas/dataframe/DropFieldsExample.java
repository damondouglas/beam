package org.apache.beam.examples.schemas.dataframe;

import org.apache.beam.examples.schemas.model.javabeanschema.Simple;
import org.apache.beam.sdk.schemas.transforms.DropFields;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;

public class DropFieldsExample extends PTransform<PCollection<Simple>, PCollection<Row>> {
    @Override
    public PCollection<Row> expand(PCollection<Simple> input) {
        return input.apply("Drop ", DropFields.fields("aString", "aBoolean", "aDouble"));
    }
}
