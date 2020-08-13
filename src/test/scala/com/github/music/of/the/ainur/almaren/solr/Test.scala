package com.github.music.of.the.ainur.almaren.bigquery

import org.apache.spark.sql.SaveMode
import org.scalatest._
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.bigquery.BigQuery.BigQueryImplicit

class Test extends FunSuite with BeforeAndAfter {

  val almaren = Almaren("App Test")

  val spark = almaren.spark
    .master("local[*]")
    .config("spark.ui.enabled","false")
    .config("spark.sql.shuffle.partitions", "1")
    .getOrCreate()
  
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  // Create twitter table with data
  val jsonData = bootstrap


  //write tests

  test("number of records should match") {
//    assert(inputCount == bigqueryDataCount)
  }

  // Check if ids match
  //val diff = jsonData.as("json").join(bigqueryData.as("bigquery"), $"json.id" <=> $"bigquery.id","leftanti").count()
  test("match records") {
 //   assert(diff == 0)
  }

  def bootstrap = {
    val res = spark.read.json("src/test/resources/sample_data/twitter_search_data.json")
    res.createTempView("twitter")
    res
  }

  after {
    spark.stop
  }
}
