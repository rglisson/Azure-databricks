#view files
%fs ls /databricks-datasets/structured-streaming/events/

inputPath = "/databricks-datasets/structured-streaming/events/"

# Define the schema to speed up processing
jsonSchema = StructType([ StructField("time", TimestampType(), True), StructField("action", StringType(), True) ])

from pyspark.sql.types import StructType
from pyspark.sql.types import StructField
from pyspark.sql.types import TimestampType
from pyspark.sql.types import StringType

streamingInputDF = (
  spark
    .readStream
    .schema(jsonSchema)               # Set the schema of the JSON data
    .option("maxFilesPerTrigger", 1)  # Treat a sequence of files as a stream by picking one file at a time
    .json(inputPath)
)

streamingCountsDF = (
  streamingInputDF
    .groupBy(
      streamingInputDF.action,
      window(streamingInputDF.time, "1 hour"))
    .count()
)

from pyspark.sql.window import Window
from pyspark.sql.functions import * # for window() function

#start streaming
query = (
  streamingCountsDF
    .writeStream
    .format("memory")        # memory = store in-memory table (for testing only)
    .queryName("counts")     # counts = name of the in-memory table
    .outputMode("complete")  # complete = all the counts should be in the table
    .start()
)

#Query the stream
%sql 
select action, date_format(window.end, "MMM-dd HH:mm") as time, count from counts order by time, action
