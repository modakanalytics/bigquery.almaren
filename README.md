# BigQuery Connector

```
libraryDependencies += "com.github.music-of-the-ainur" %% "bigquery-almaren" % "0.0.1-2-4"
```

BigQuery Connector was implemented using [https://github.com/GoogleCloudDataproc/spark-bigquery-connector](https://github.com/GoogleCloudDataproc/spark-bigquery-connector). The *BigQuery Connector* just works on BigQuery Cloud.
For all the options available for the connector check on this [link](https://github.com/GoogleCloudDataproc/spark-bigquery-connector).

```
spark-shell --master "local[*]" --packages "com.github.music-of-the-ainur:almaren-framework_2.11:0.2.8-$SPARK_VERSION,com.github.music-of-the-ainur:bigquery-almaren_2.11:0.2.5-2-4" --repositories https://repo.boundlessgeo.com/main/
```


## Source and Target

### Source 
#### Parameteres

| Parameters | Description             |
|------------|-------------------------|

#### Example


```scala
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.bigquery.BigQuery.BigQueryImplicit

val almaren = Almaren("App Name")

almaren.builder.sourceBigQuery("collection","zkHost1:2181,zkHost2:2181",Map("field_names" -> "first_name,last_name","rows" -> 100))

almaren.builder.targetBigQuery("collection","zkHost1:2181,zkHost2:2181",options)

```



### Target:
#### Parameters

| Parameters | Description             |
|------------|-------------------------|

#### Example

```scala
import com.github.music.of.the.ainur.almaren.bigquery.BigQuery.BigQueryImplicit
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.Almaren
import org.apache.spark.sql.SaveMode

val almaren = Almaren("App Name")

almaren.builder
    .sourceSql("""SELECT sha2(concat_ws("",array(*)),256) as id,*,current_timestamp from deputies""")
    .coalesce(30)
    .targetBigQuery("deputies","cloudera:2181,cloudera1:2181,cloudera2:2181/bigquery",Map("batch_size" -> "100000","commit_within" -> "10000"),SaveMode.Overwrite)
    .batch
```

