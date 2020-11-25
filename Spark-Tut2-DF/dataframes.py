#Display sample data
display(dbutils.fs.ls("/databricks-datasets/samples/population-vs-price/data_geo.csv"))


# Use the Spark CSV datasource with options specifying:
# - First line of file is a header
# - Automatically infer the schema of the data
data = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("/databricks-datasets/samples/population-vs-price/data_geo.csv")
data.cache() # Cache data for faster reuse
data = data.dropna() # drop rows with missing values

#view first 10 rows
data.take(10)

#view in tabular format
display(data)

# Register table so it is accessible via SQL Context
data.createOrReplaceTempView("data_geo")

#sql query
%sql
select `State Code`, `2015 median sales price` from data_geo

#sql query #2
%sql
select City, `2014 Population estimate` from data_geo where `State Code` = 'WA';
