#configure
storage_account_name = "<>" 
storage_account_access_key = "<>" 

file_location = "wasbs://<container>@<storage_name>.blob.core.windows.net/<csv file name>" 
file_type = "csv" 

spark.conf.set("fs.azure.account.key."+storage_account_name+".blob.core.windows.net", storage_account_access_key) 

#read the file
df = spark.read.format(file_type).option("inferSchema", "true").option("header", "true").load(file_location) 

#display the file (all columns)
display(df.select("*")) 

