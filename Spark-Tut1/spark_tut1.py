# Take a look at the file system
display(dbutils.fs.ls("/databricks-datasets/samples/docs/"))

# Spark command
textFile = spark.read.text("/databricks-datasets/samples/docs/README.md")

# Count the lines of the readme file
textFile.count()
