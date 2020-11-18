// Data source
val containerName = "<>" 
val storageAccountName = "<>" 
val sas = "<>" 
val config = "fs.azure.sas." + containerName+ "." + storageAccountName + ".blob.core.windows.net" 

// Mount file
dbutils.fs.mount( 
  source = "wasbs://<containername>@<storageaccountname>.blob.core.windows.net/<csv-name>", 
  mountPoint = "/mnt/<mount-name>", 
  extraConfigs = Map(config -> sas)) 

// Read file
val df = spark.read 
.option("header","true") 
.option("inferSchema", "true") 
.csv("/mnt/<mount-name>/") 
display(df) 
