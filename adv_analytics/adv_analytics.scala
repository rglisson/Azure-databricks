// Connect data from Azure Blob Storage - Data source
val containerName = "<>" 
val storageAccountName = "<>" 
val sas = "<>" 
val config = "fs.azure.sas." + containerName+ "." + storageAccountName + ".blob.core.windows.net" 

// Mount file
dbutils.fs.mount( 
  source = "wasbs://<containername>@<storageaccountname>.blob.core.windows.net/<csv-name>", 
  mountPoint = "/mnt/adtech/impression/csv/train.csv/", 
  extraConfigs = Map(config -> sas)) 

// Read CSV files of our adtech dataset
val df = spark.read
  .option("header", true)
  .option("inferSchema", true)
  .csv("/mnt/adtech/impression/csv/train.csv/")



//Create schema
df.printSchema()

// Create Parquet files from our Spark DataFrame
df.coalesce(4)
  .write
  .mode("overwrite")
  .parquet("/mnt/adtech/impression/parquet/train.csv")

//Explore logs

//# Create Spark DataFrame reading the recently created Parquet files
%python
impression = spark.read.parquet("/mnt/adtech/impression/parquet/train.csv/")
//Create temporary view
impression.createOrReplaceTempView("impression")

//sql queries
%sql
-- Calculate CTR by Banner Position
select banner_pos,
sum(case when click = 1 then 1 else 0 end) / (count(1) * 1.0) as CTR
from impression 
group by 1 
order by 1

%sql
-- Calculate CTR by Hour of the day
select substr(hour, 7) as hour,
sum(case when click = 1 then 1 else 0 end) / (count(1) * 1.0) as CTR
from impression 
group by 1 
order by 1
