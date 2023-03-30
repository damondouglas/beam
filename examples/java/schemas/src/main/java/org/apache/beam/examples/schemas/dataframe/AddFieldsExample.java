package org.apache.beam.examples.schemas.dataframe;

import org.apache.beam.examples.schemas.model.javabeanschema.Simple;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.transforms.AddFields;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;

public class AddFieldsExample extends PTransform<PCollection<Simple>, PCollection<Row>> {
    @Override
    public PCollection<Row> expand(PCollection<Simple> input) {
        return input.apply("Add aLong Field", AddFields.<Simple>create().field("aLong", Schema.FieldType.INT64));
    }
}
