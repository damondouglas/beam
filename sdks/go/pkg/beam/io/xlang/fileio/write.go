// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements.  See the NOTICE file distributed with
// this work for additional information regarding copyright ownership.
// The ASF licenses this file to You under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License.  You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Package fileio contains cross-language transforms for expanding the Java SDK FileReadSchemaTransformProvider and
// FileWriteSchemaTransformProvider contained in the org.apache.beam.sdk.io.fileschematransform package.
// These transforms only work on runners that support cross-language transforms.
package fileio

import (
	"fmt"
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/xlangx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/typex"
	"reflect"
)

const (
	inputRowTupleTag    = "input"
	outputRowTupleTag   = "output"
	writeURN            = "beam:schematransform:org.apache.beam:file_write:v1"
	serviceGradleTarget = ":sdks:java:io:file-schema-transform:expansion-service:runExpansionService"

	formatAvro    = "avro"
	formatCsv     = "csv"
	formatJson    = "json"
	formatParquet = "parquet"
	formatXml     = "xml"

	// CompressionAuto configures an automatic determination of the compression type based on filename extension.
	CompressionAuto Compression = "AUTO"

	// CompressionBZip2 configures a BZIP compression.
	CompressionBZip2 Compression = "BZIP2"

	// CompressionDeflate configures a deflate compression.
	CompressionDeflate Compression = "DEFLATE"

	// CompressionGzip configures a gzip compression.
	CompressionGzip Compression = "GZIP"

	// CompressionLzo configures a LZO compression using LZO codec.
	CompressionLzo Compression = "LZO"

	// CompressionLzop configures an LZOP compression using LZOP codec.
	CompressionLzop Compression = "LZOP"

	// CompressionSnappy configures a Google Snappy compression.
	CompressionSnappy Compression = "SNAPPY"

	// CompressionUnCompressed results in no compression.
	CompressionUnCompressed Compression = "UNCOMPRESSED"

	// CompressionZip configures a Zip compression.
	CompressionZip Compression = "ZIP"

	// CompressionZstd configures a ZStandard compression.
	CompressionZstd Compression = "ZSTD"

	// PredefinedCsvFormatDefault specifies a default CSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#Default
	PredefinedCsvFormatDefault PredefinedCsvFormat = "Default"

	// PredefinedCsvFormatExcel specifies an Excel CSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#Excel
	PredefinedCsvFormatExcel PredefinedCsvFormat = "Excel"

	// PredefinedCsvFormatInformixUnload specifies an InformixUnload default CSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#InformixUnload
	PredefinedCsvFormatInformixUnload PredefinedCsvFormat = "InformixUnload"

	// PredefinedCsvFormatInformixUnloadCsv specifies InformixUnloadCSV default CSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#InformixUnloadCsv
	PredefinedCsvFormatInformixUnloadCsv PredefinedCsvFormat = "InformixUnloadCsv"

	// PredefinedCsvFormatMongoDBCsv specifies a MongoDBCSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#MongoDBCsv
	PredefinedCsvFormatMongoDBCsv PredefinedCsvFormat = "MongoDBCsv"

	// PredefinedCsvFormatMongoDBTsv specifies a MongoDBTSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#MongoDBTsv
	PredefinedCsvFormatMongoDBTsv PredefinedCsvFormat = "MongoDBTsv"

	// PredefinedCsvFormatMySQL specifies a MySQL CSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#MySQL
	PredefinedCsvFormatMySQL PredefinedCsvFormat = "MySQL"

	// PredefinedCsvFormatOracle specifies an Oracle CSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#Oracle
	PredefinedCsvFormatOracle PredefinedCsvFormat = "Oracle"

	// PredefinedCsvFormatPostgreSQLCsv specifies a PostgreSQL CSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#PostgreSQLCsv
	PredefinedCsvFormatPostgreSQLCsv PredefinedCsvFormat = "PostgreSQLCsv"

	// PredefinedCsvFormatPostgreSQLText specifies a PostgreSQLText format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#PostgreSQLText
	PredefinedCsvFormatPostgreSQLText PredefinedCsvFormat = "PostgreSQLText"

	// PredefinedCsvFormatRFC4180 specifies a RFC4180 CSV format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#RFC4180
	PredefinedCsvFormatRFC4180 PredefinedCsvFormat = "RFC4180"

	// PredefinedCsvFormatTDF specifies a TDF format.
	// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html#TDF
	PredefinedCsvFormatTDF PredefinedCsvFormat = "TDF"

	// ParquetCompressionGzip configures a formatParquet gzip format.
	ParquetCompressionGzip ParquetCompression = "GZIP"

	// ParquetCompressionLzo configures a formatParquet lzo format.
	ParquetCompressionLzo ParquetCompression = "LZO"

	// ParquetCompressionSnappy configures a formatParquet using Google Snappy compression.
	ParquetCompressionSnappy ParquetCompression = "SNAPPY"

	// ParquetCompressionUncompressed configures a formatParquet without compression.
	ParquetCompressionUncompressed ParquetCompression = "UNCOMPRESSED"

	XmlCharsetUsAscii  XmlCharset = "US-ASCII"
	XmlCharsetIso88591 XmlCharset = "ISO-8859-1"
	XmlCharsetUTF8     XmlCharset = "UTF-8"
	XmlCharsetUTF16    XmlCharset = "UTF-16"
	XmlCharsetUTF16BE  XmlCharset = "UTF-16BE"
	XmlCharsetUTF16LE  XmlCharset = "UTF-16LE"
)

var autoStartupAddress = xlangx.UseAutomatedJavaExpansionService(serviceGradleTarget)

// WriteOption applies additional configuration details to a file write transform.
type WriteOption interface {
	apply(configuration *writeConfiguration)
}

// Compression type for the target files. Compression is also a WriteOption.
type Compression string

func (opt Compression) apply(configuration *writeConfiguration) {
	configuration.Compression = opt
}

// PredefinedCsvFormat defines expected CSV formats based on the Apache Creative Commons CSV project.
// See https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.Predefined.html.
type PredefinedCsvFormat string

// ParquetCompression defines the formatParquet compression codec.
type ParquetCompression string

// XmlCharset defines the formatXml that maps to java.nio.charset.Charset; required by underlying XmlIO.write.
type XmlCharset string

// WriteResult is the output from write that stores the resulting file name written to the filesystem.
type WriteResult struct {
	FileName string `beam:"fileName"`
}

func WithNumShards(v int32) WriteOption {
	return numShardsOpt(v)
}

func WithShardNameTemplate(v string) WriteOption {
	return shardNameTemplateOpt(v)
}

// WithWriteExpansionAddr configures a file write transform using an expansion host at addr.
func WithWriteExpansionAddr(addr string) WriteOption {
	return writeExpansionServiceAddrOpt(addr)
}

// WriteAvro an input beam.PCollection with files sharing a filenamePrefix. No WithWriteExpansionAddr defaults to an
// automatic expansion service execution. File extensions defaults to avro.
func WriteAvro(s beam.Scope, filenamePrefix string, input beam.PCollection, opts ...WriteOption) (beam.PCollection, error) {
	config := &writeConfiguration{
		Format:         formatAvro,
		FilenamePrefix: filenamePrefix,
	}
	output, err := tryWrite(s, config, input, opts...)
	if err != nil {
		return beam.PCollection{}, err
	}
	return output[outputRowTupleTag], nil
}

// WriteCsv an input beam.PCollection with files sharing a filenamePrefix. No WithWriteExpansionAddr defaults to an
// automatic expansion service execution. WriteCsv requires additional CsvWrite details.
// File extensions defaults to csv.
func WriteCsv(s beam.Scope, filenamePrefix string, config *CsvWrite, input beam.PCollection, opts ...WriteOption) (beam.PCollection, error) {
	c := &writeConfiguration{
		Format:           formatCsv,
		FilenamePrefix:   filenamePrefix,
		CsvConfiguration: config,
	}
	output, err := tryWrite(s, c, input, opts...)
	if err != nil {
		return beam.Impulse(s), err
	}
	return output[outputRowTupleTag], nil
}

// WriteJson an input beam.PCollection with files sharing a filenamePrefix. No WithWriteExpansionAddr defaults to an
// automatic expansion service execution.
// File extensions default to json.
func WriteJson(s beam.Scope, filenamePrefix string, input beam.PCollection, opts ...WriteOption) (beam.PCollection, error) {
	config := &writeConfiguration{
		Format:         formatJson,
		FilenamePrefix: filenamePrefix,
	}
	output, err := tryWrite(s, config, input, opts...)
	if err != nil {
		return beam.Impulse(s), err
	}
	return output[outputRowTupleTag], nil
}

// WriteParquet an input beam.PCollection with files sharing a filenamePrefix. No WithWriteExpansionAddr defaults to an
// automatic expansion service execution. WriteParquet requires additional ParquetWrite details.
// File extensions default to parquet.
func WriteParquet(s beam.Scope, filenamePrefix string, config *ParquetWrite, input beam.PCollection, opts ...WriteOption) (beam.PCollection, error) {
	c := &writeConfiguration{
		Format:               formatParquet,
		FilenamePrefix:       filenamePrefix,
		ParquetConfiguration: config,
	}
	output, err := tryWrite(s, c, input, opts...)
	if err != nil {
		return beam.Impulse(s), err
	}
	return output[outputRowTupleTag], nil
}

// WriteXml an input beam.PCollection with files sharing a filenamePrefix. No WithWriteExpansionAddr defaults to an
// automatic expansion service execution. WriteXml requires additional XmlWrite details.
// Files' extension defaults to xml.
func WriteXml(s beam.Scope, filenamePrefix string, config *XmlWrite, input beam.PCollection, opts ...WriteOption) (beam.PCollection, error) {
	c := &writeConfiguration{
		Format:           formatXml,
		FilenamePrefix:   filenamePrefix,
		XmlConfiguration: config,
	}
	output, err := tryWrite(s, c, input, opts...)
	if err != nil {
		return beam.Impulse(s), err
	}
	return output[outputRowTupleTag], nil
}

type numShardsOpt int32

func (opt numShardsOpt) apply(configuration *writeConfiguration) {
	configuration.NumShards = int32(opt)
}

type shardNameTemplateOpt string

func (opt shardNameTemplateOpt) apply(configuration *writeConfiguration) {
	configuration.ShardNameTemplate = string(opt)
}

func WithFilenameSuffix(v string) WriteOption {
	return filenameSuffixOpt(v)
}

type filenameSuffixOpt string

func (opt filenameSuffixOpt) apply(configuration *writeConfiguration) {
	configuration.FilenameSuffix = string(opt)
}

type writeExpansionServiceAddrOpt string

func (opt writeExpansionServiceAddrOpt) apply(configuration *writeConfiguration) {
	configuration.expansionAddress = string(opt)
}

// writeConfiguration configures a struct-based DoFn that writes to a file or object system.
// writeConfiguration is based on org.apache.beam.sdk.io.fileschematransform.FileWriteSchemaTransformConfiguration.
// See https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/io/fileschematransform/FileWriteSchemaTransformConfiguration.html
type writeConfiguration struct {
	expansionAddress string

	// Format (required) of the target file or object system. See available const values such as formatAvro, formatJson, etc.
	Format string `beam:"format"`

	// FilenamePrefix (required) is a common prefix to use for all generated filenames.
	FilenamePrefix string `beam:"filenamePrefix"`

	// Compression (optional) of all generated shard files. By default, appends the respective extension to the filename.
	// See available const values such as CompressionAuto, CompressionGzip, CompressionZip, etc.
	Compression Compression `beam:"compression"`

	// NumShards (optional) configures the number of output shards produced; a value of 1 disables sharding.
	NumShards int32 `beam:"numShards"`

	// ShardNameTemplate (optional) specifies the given shard name template for naming output files.
	// See https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/io/ShardNameTemplate.html for
	// expected values.
	ShardNameTemplate string `beam:"shardNameTemplate"`

	// FilenameSuffix (optional) configures the filename suffix for written files; defaults to Format.
	FilenameSuffix string `beam:"filenameSuffix"`

	// CsvConfiguration (required if formatCsv) provides additional details related to writing CSV formatted files.
	CsvConfiguration *CsvWrite `beam:"csvConfiguration"`

	// ParquetConfiguration (required if formatParquet) provides additional details related to writing Parquet formatted files.
	ParquetConfiguration *ParquetWrite `beam:"parquetConfiguration"`

	// XMLConfiguration (required if formatXml) provides additional details related to writing XML formatted files.
	XmlConfiguration *XmlWrite `beam:"xmlConfiguration"`
}

func (configuration writeConfiguration) isValidErr() error {
	// check incompatible format with extra configuration details
	format := "invalid configuration: format: %s incompatible with non-nil %T"
	if configuration.Format != formatCsv && configuration.CsvConfiguration != nil {
		return fmt.Errorf(format, configuration.Format, configuration.CsvConfiguration)
	}
	if configuration.Format != formatParquet && configuration.ParquetConfiguration != nil {
		return fmt.Errorf(format, configuration.Format, configuration.ParquetConfiguration)
	}
	if configuration.Format != formatXml && configuration.XmlConfiguration != nil {
		return fmt.Errorf(format, configuration.Format, configuration.XmlConfiguration)
	}

	// check format with required extra configuration details
	format = "invalid configuration: format: %s requires a non-nil %T"
	if configuration.Format == formatCsv && configuration.CsvConfiguration == nil {
		return fmt.Errorf(format, configuration.Format, configuration.CsvConfiguration)
	}
	if configuration.Format == formatParquet && configuration.ParquetConfiguration == nil {
		return fmt.Errorf(format, configuration.Format, configuration.ParquetConfiguration)
	}
	if configuration.Format == formatXml && configuration.XmlConfiguration == nil {
		return fmt.Errorf(format, configuration.Format, configuration.XmlConfiguration)
	}
	return nil
}

// CsvWrite configures details for writing CSV formatted data to a file or object system.
// CsvWrite is based on the FileWriteSchemaTransformConfiguration.CsvConfiguration.
// See https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/io/fileschematransform/FileWriteSchemaTransformConfiguration.CsvConfiguration.html
type CsvWrite struct {
	// PredefinedCsvFormat (required) specifies the Apache CSV creative commons predefined format. See available const values
	// such as PredefinedCsvFormatPostgreSQLCsv, PredefinedCsvFormatDefault, etc.
	PredefinedCsvFormat PredefinedCsvFormat `beam:"predefinedCsvFormat"`
}

// ParquetWrite provides additional details related to writing Parquet formatted files.
type ParquetWrite struct {
	// CompressionCodecName (required) defines the ParquetCompression used when writing Parquet files. See available const values
	// such as ParquetCompressionGzip, ParquetCompressionSnappy, etc.
	CompressionCodecName ParquetCompression `beam:"compressionCodecName"`

	// RowGroupSize (required) configures the row-group size; if not set or zero, a default is used by the underlying writer.
	RowGroupSize int32 `beam:"rowGroupSize"`
}

// XmlWrite provides additional details related to writing XML formatted files.
type XmlWrite struct {

	// RootElement (required) sets the enclosing root element for the generated XML files.
	RootElement string `beam:"rootElement"`

	// Charset (required) is the charset used to write XML files.
	Charset XmlCharset `beam:"charset"`
}

// tryWrite an input beam.PCollection to file or object systems, configured by a writeConfiguration that calls
// xlschema.TryTransform. Forwards any error received from beam.TryCrossLanguage. WriteOption opts applies optional
// details to the writeConfiguration. Providing no WithWriteExpansionAddr WriteOption defaults to an automatic
// execution of an expansion service.
func tryWrite(s beam.Scope, configuration *writeConfiguration, input beam.PCollection, opts ...WriteOption) (map[string]beam.PCollection, error) {

	configuration.expansionAddress = autoStartupAddress
	configuration.Compression = CompressionUnCompressed
	configuration.FilenameSuffix = configuration.Format

	for _, opt := range opts {
		opt.apply(configuration)
	}

	if err := configuration.isValidErr(); err != nil {
		return nil, err
	}

	pl := beam.CrossLanguagePayload(configuration)

	namedInput := map[string]beam.PCollection{
		inputRowTupleTag: input,
	}

	outputTypes := map[string]typex.FullType{
		outputRowTupleTag: typex.New(reflect.TypeOf(WriteResult{})),
	}

	output, err := beam.TryCrossLanguage(s.Scope(writeURN), writeURN, pl, configuration.expansionAddress, namedInput, outputTypes)
	if err != nil {
		return nil, err
	}
	return output, nil
}
