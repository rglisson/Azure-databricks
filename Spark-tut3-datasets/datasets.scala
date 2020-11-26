// Create a dataset: option 1 -- range of 100 numbers to create a Dataset.
val range100 = spark.range(100)
range100.collect()

//Second option: read a data file from external source
val df = spark.read.json("/databricks-datasets/samples/people/people.json")

// First, define a case class that represents a type-specific Scala JVM Object
case class Person (name: String, age: Long)

// Read the JSON file, convert the DataFrames into a type-specific JVM Scala object
// Person. At this stage Spark, upon reading JSON, created a generic
// DataFrame = Dataset[Rows]. By explicitly converting DataFrame into Dataset
// results in a type-specific rows or collection of objects of type Person
val ds = spark.read.json("/databricks-datasets/samples/people/people.json").as[Person]

// define a case class that represents the device data.
case class DeviceIoTData (
  battery_level: Long,
  c02_level: Long,
  cca2: String,
  cca3: String,
  cn: String,
  device_id: Long,
  device_name: String,
  humidity: Long,
  ip: String,
  latitude: Double,
  longitude: Double,
  scale: String,
  temp: Long,
  timestamp: Long
)

// read the JSON file and create the Dataset from the ``case class`` DeviceIoTData
// ds is now a collection of JVM Scala objects DeviceIoTData
val ds = spark.read.json("/databricks-datasets/iot/iot_devices.json").as[DeviceIoTData]

// display the dataset table just read in from the JSON file
display(ds)

// Using the standard Spark commands, take() and foreach(), print the first
// 10 rows of the Datasets.
ds.take(10).foreach(println(_))
Print first 10 rows of a dataset

// filter out all devices whose temperature exceed 25 degrees and generate
// another Dataset with three fields that of interest and then display
// the mapped Dataset
val dsTemp = ds.filter(d => d.temp > 25).map(d => (d.temp, d.device_name, d.cca3))
display(dsTemp)

// Apply higher-level Dataset API methods such as groupBy() and avg().
// Filter temperatures > 25, along with their corresponding
// devices' humidity, compute averages, groupBy cca3 country codes,
// and display the results, using table and bar charts
val dsAvgTmp = ds.filter(d => {d.temp > 25}).map(d => (d.temp, d.humidity, d.cca3)).groupBy($"_3").avg()

// display averages as a table, grouped by the country
display(dsAvgTmp)

// Select individual fields using the Dataset method select()
// where battery_level is greater than 6. Note this high-level
// domain specific language API reads like a SQL query
display(ds.select($"battery_level", $"c02_level", $"device_name").where($"battery_level" > 6).sort($"c02_level"))

// registering your Dataset as a temporary table to which you can issue SQL queries
ds.createOrReplaceTempView("iot_device_data")

%sql 
select cca3, count (distinct device_id) as device_id from iot_device_data group by cca3 order by device_id desc limit 100
