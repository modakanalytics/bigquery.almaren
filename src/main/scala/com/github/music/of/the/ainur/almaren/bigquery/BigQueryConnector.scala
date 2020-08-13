package com.github.music.of.the.ainur.almaren.bigquery

import org.apache.spark.sql.{DataFrame,SaveMode}
import com.github.music.of.the.ainur.almaren.Tree
import com.github.music.of.the.ainur.almaren.builder.Core
import com.github.music.of.the.ainur.almaren.state.core.{Target,Source}
import com.google.cloud.spark.bigquery._

private[almaren] case class SourceBigQuery(table:String, options:Map[String,String]) extends Source {
  def source(df: DataFrame): DataFrame = {
    logger.info(s"table:{$table}, options:{$options}")
    df.sparkSession.read.format("bigquery")
      .options(options)
      .load(table)
  }
}

private[almaren] case class TargetBigQuery(table:String, options:Map[String,String],saveMode:SaveMode) extends Target {
  def target(df: DataFrame): DataFrame = {
    logger.info(s"table:{$table}, options:{$options}")
    df.write.format("bigquery")
      .options(options)
      .mode(saveMode)
      .save(table)
    df
  }
}

private[almaren] trait BigQueryConnector extends Core {
  def targetBigQuery(table:String, options:Map[String,String] = Map(),saveMode:SaveMode = SaveMode.ErrorIfExists): Option[Tree] =
     TargetBigQuery(table,options,saveMode)

  def sourceBigQuery(table:String, options:Map[String,String] = Map()): Option[Tree] =
    SourceBigQuery(table,options)
}

object BigQuery {
  implicit class BigQueryImplicit(val container: Option[Tree]) extends BigQueryConnector
}
