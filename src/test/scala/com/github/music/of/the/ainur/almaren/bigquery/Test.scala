package com.github.music.of.the.ainur.almaren.bigquery
import org.apache.spark.sql.{AnalysisException, Column, DataFrame, SaveMode, SparkSession}
import org.scalatest._
import org.apache.spark.sql.functions._
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.bigquery.BigQuery.BigQueryImplicit
import org.scalatest.funsuite.AnyFunSuite

class Test extends AnyFunSuite with BeforeAndAfter {
  val almaren = Almaren("bigQuery-almaren")
  val spark: SparkSession = almaren.spark
    .master("local[*]")
    .config("spark.ui.enabled", "false")
    .config("spark.sql.shuffle.partitions", "1")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")
  val bigQueryDf: DataFrame = spark.read.parquet("src/test/resources/data/bigQueryTestTable.parquet")

  val gcpToken: String = sys.env.getOrElse("GCP_TOKEN", throw new Exception("GCP_TOKEN environment variable is not set"))
  println(s"GCP Token: $gcpToken")
  spark.conf.set("gcpAccessToken", gcpToken)
  spark.conf.set("viewsEnabled","true")
  spark.conf.set("materializationDataset","nabu_spark")
  //creating config map

  val configMap: Map[String, String] = Map("parentProject" -> "modak-nabu",
    "project" -> "modak-nabu",
    "dataset" -> "nabu_spark")
  val df: DataFrame = almaren.builder
    .sourceBigQuery("customer", configMap)
    .batch

  df.show()

  val configMap1: Map[String, String] = Map("parentProject" -> "modak-nabu",
    "project" -> "modak-nabu")
  val df1: DataFrame = almaren.builder
    .sourceBigQuery("SELECT * FROM `nabu_spark.customer` ", configMap1)
    .batch

  test(bigQueryDf, df, "Read bigQuery Test")
  def test(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    testCount(df1, df2, name)
    testCompare(df1, df2, name)
  }
  def testCount(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    val count1 = df1.count()
    val count2 = df2.count()
    val count3 = spark.emptyDataFrame.count()
    test(s"Count Test:$name should match") {
      assert(count1 == count2)
    }
    test(s"Count Test:$name should not match") {
      assert(count1 != count3)
    }
  }
  // Doesn't support nested type and we don't need it :)
  def testCompare(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    val diff = compare(df1, df2)
    test(s"Compare Test:$name should be zero") {
      assert(diff == 0)
    }
    test(s"Compare Test:$name, should not be able to join") {
      assertThrows[AnalysisException] {
        compare(df2, spark.emptyDataFrame)
      }
    }
  }
  private def compare(df1: DataFrame, df2: DataFrame): Long =
    df1.as("df1").join(df2.as("df2"), joinExpression(df1), "leftanti").count()
  private def joinExpression(df1: DataFrame): Column =
    df1.schema.fields
      .map(field => col(s"df1.${field.name}") <=> col(s"df2.${field.name}"))
      .reduce((col1, col2) => col1.and(col2))
  after {
    spark.stop
  }
}