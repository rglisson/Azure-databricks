val df1 = spark.read  
.option("header","true")  
.option("inferSchema", "true")  
.csv("<paste your link here>")  
display(df1)
