import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


val taxiDF = spark.read.option("header", "true").csv("/user/jharden/taxi_sample_merged_2_csv.csv")

//val sqlContext = new SQLContext(sc)


case class Trip(
	rowkey: String,
	pu_year: Double,
	pu_month: Double,
	pu_day: Double,
	pu_hour: Double,
	pu_minute: Double,
	do_year: Double,
	do_month: Double,
	do_day: Double,
	do_hour: Double,
	do_minute: Double,
	passenger_count: Double,
	trip_dist: Double,
	store_fwd_flag: String,
	payment_type: Int,
  trip_type: Int,
  RatecodeID: Int,
	fare_amt: Double,
	extra: Double,
	mta_tax: Double,
	tip: Double,
	tolls: Double,
	improvement_surcharge: Double,
	total_amt: Double,
	pu_borough: String,
	pu_zone: String,
	pu_service_zone: String,
	do_borough: String,
	do_zone: String,
	do_service_zone: String)

class RichRow(row: org.apache.spark.sql.Row) {
	def getAs[T](field: String): Option[T] = {
		if (row.isNullAt(row.fieldIndex(field))) {
			None
		} else {
			Some(row.getAs[T](field))
		}
	}
}
  /*
      We can also map the DF to a table and query against it.
      
       val query = "SELECT count(*) as Number_Rows FROM TAXI_DATA "
  */
  	  
       println("\n\nTaxi Data Analysis by Month (Currently just January)")
       val taxiDF = taxiDF       	 
  	//  taxiDF.select("tip_amount").alias("Highest Tips").select("do_borough").alias("Drop Off Borough").show()
       taxiDF.groupBy("do_borough").agg(max("tip_amount")).orderBy(desc("max(tip_amount)")).show()


  	 /*
  	  println("\nHighest Tips by Drop Off Borough")
	    spark.sql("SELECT max(tip_amount) as Highest_Tips, do_borough as Drop_Off FROM TAXI_DATA group by do_borough order by max(tip_amount) desc").show()
 	  */
     
      println("\nHighest Tips by Drop Off Borough")
      val totTripDF = taxiDF
      totTripDF.groupBy("do_borough").agg(round(sum("total_amount"),2)).orderBy(desc("round(sum(total_amount), 2)")).show()


/*
      println("\nSum of the Total Trip Cost by Drop Off Borough")
      spark.sql("SELECT round(sum(total_amount)) as Total_Cost, do_borough as Drop_Off FROM TAXI_DATA group by do_borough order by sum(total_amount) desc").show()
  */   

   
      println("\nTotal Passenger Count")
      val passCountDF = taxiDF     
      passCountDF.agg(sum("passenger_count")).show()

      /*SQL Version

      spark.sql("SELECT sum(passenger_count) as Total_Passengers FROM TAXI_DATA ").show()
    
      */

      val totDistDF = taxiDF
      println("\nTotal Trip Distance")
      totDistDF.agg(round(sum("trip_distance"), 2)).show()

      /*
      spark.sql("SELECT round(sum(trip_distance)) as Total_Distance FROM TAXI_DATA ").show()
      */


      val distByBorDF = taxiDF
      println("\nTrip Distance by Dropoff Borough")      
      distByBorDF.groupBy("do_borough").agg(round(sum("trip_distance"), 2)).orderBy(desc("round(sum(trip_distance), 2)")).show()


      /*
      spark.sql("SELECT round(sum(trip_distance)) as Total_Distance, do_borough as Drop_Off FROM TAXI_DATA group by do_borough order by sum(trip_distance) desc").show()
      */

      val tollCostDF = taxiDF
      println("\nTotal Toll Cost")    
      tollCostDF.agg(round(sum("tolls_amount"), 2)).show()

     /*
      spark.sql("SELECT round(sum(tolls_amount)) as Total_Toll_Cost FROM TAXI_DATA ").show()
    */

      val totTollDF = taxiDF
      println("\nTotal Toll Amount by Dropoff Borough")
      totTollDF.groupBy("do_borough").agg(round(sum("tolls_amount"), 2)).orderBy(desc("round(sum(tolls_amount), 2)")).show()

     
     // spark.sql("SELECT round(sum(tolls_amount)) as Total_Tolls, do_borough as Drop_Off FROM TAXI_DATA group by do_borough order by sum(tolls_amount) desc").show()
      

       val tripByBoDF = taxiDF
      println("\nTrips by Dropoff Borough")      
      tripByBoDF.groupBy("do_borough").agg(count("*")).orderBy(desc("count(1)")).show()


      //spark.sql("SELECT count(*) as Trip_Count, do_borough as Drop_Off FROM TAXI_DATA group by do_borough order by count(*) desc").show()
    

      
      println("\nTop Ten Trips Where Distance = 0")
      val zeroDistDF = taxiDF     
      zeroDistDF.filter("trip_distance < 0.001").groupBy("do_borough", "RatecodeID", "trip_distance").agg(count("*")).orderBy(desc("count(1)")).show(30)


     // spark.sql("SELECT count(*) as Trip_Count, trip_distance, do_borough, RatecodeID FROM TAXI_DATA where trip_distance < 0.1 group by do_borough, RatecodeID, trip_distance  order by count(*) desc limit 10").show()
 

      println("\nMost Trips by hour ")     
      val mostTripsDF = taxiDF
      mostTripsDF.groupBy("pu_hour").agg(count("*")).orderBy(desc("count(1)")).show(24)

      /*
      spark.sql("SELECT count(*) as Trip_Count, pu_hour as Hour FROM TAXI_DATA group by pu_hour order by count(*) desc").show()
      */
    println("\n\n")
   
   // sc.stop()


/*
taxiDF.show()
*/