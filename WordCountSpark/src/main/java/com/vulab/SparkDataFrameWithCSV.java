package com.vulab;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class SparkDataFrameWithCSV {

	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("wordCount").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		SQLContext sqlContext = new SQLContext(sc);
		DataFrame df = sqlContext.read()
		    .format("com.databricks.spark.csv")
		    .option("inferSchema", "true")
		    .option("header", "true")
		    .load("cars.csv");
		
		df.printSchema();
		df.select("model").show();
		df.select("make").show();
		
		df.registerTempTable("cars");
		
		DataFrame sqlDF = sqlContext.sql("SELECT make,model FROM cars");
		sqlDF.show();
		
		StructType customSchema = new StructType(new StructField[] {
			    new StructField("name", DataTypes.StringType, true, Metadata.empty()),
			    new StructField("city", DataTypes.StringType, true, Metadata.empty()),
			    new StructField("age", DataTypes.IntegerType, true, Metadata.empty())
			});
		DataFrame df2 = sqlContext.read()
			    .format("com.databricks.spark.csv")
			    .schema(customSchema)
			    .option("header", "true")
			    .load("people.csv");
		
		df2.registerTempTable("peopledb");		
		DataFrame sqlDF2 = sqlContext.sql("SELECT name,age FROM peopledb");
		sqlDF2.show();

	}

}
