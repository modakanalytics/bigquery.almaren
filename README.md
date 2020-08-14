# BigQuery Connector

[![Build Status](https://travis-ci.com/modakanalytics/bigquery.almaren.svg?branch=master)](https://travis-ci.com/modakanalytics/bigquery.almaren)

```
libraryDependencies += "com.github.music-of-the-ainur" %% "bigquery-almaren" % "0.0.1-2-4"
```

BigQuery Connector was implemented using [https://github.com/GoogleCloudDataproc/spark-bigquery-connector](https://github.com/GoogleCloudDataproc/spark-bigquery-connector). The *BigQuery Connector* just works on BigQuery Cloud.
For all the options available for the connector check on this [link](https://github.com/GoogleCloudDataproc/spark-bigquery-connector).

```
spark-shell --master "local[*]" --packages "com.github.music-of-the-ainur:almaren-framework_2.11:0.2.8-$SPARK_VERSION,com.github.music-of-the-ainur:bigquery-almaren_2.11:0.0.1-2-4"
```

## Source and Target

### Source 
#### Parameteres

| Parameters | Description             |
|------------|-------------------------|
| table          | The BigQuery table in the format [[project:]dataset.]table       |
| options    |  Description(Value)|
|-------------| -------------|
| parentProject   | The Google Cloud Project ID of the table to bill for the export. (Optional. Defaults to the project of the Service Account being used)  |
| project | The Google Cloud Project ID of the table. (Optional. Defaults to the project of the Service Account being used)                  |
| dataset      |  The dataset containing the table. (Optional unless omitted in table)          |


#### Example


```scala
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.bigquery.BigQuery.BigQueryImplicit
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit

val almaren = Almaren("App Name")

spark.conf.set("gcpAccessToken","token")

val df =  almaren
         .builder
         .sourceBigQuery("dataset.table",Map("parentProject"->"project_name","project"->"project_name"))
         .batch

df.show(false)
```



### Target:
#### Parameters

| Parameters | Description             |
|------------|-------------------------|
| table          | The BigQuery table in the format [[project:]dataset.]table       |
| options    |  Description(Value)|
|-------------| -------------|
| parentProject   | The Google Cloud Project ID of the table to bill for the export. (Optional. Defaults to the project of the Service Account being used)  |
| project | The Google Cloud Project ID of the table. (Optional. Defaults to the project of the Service Account being used)                  |
| dataset      |  The dataset containing the table. (Optional unless omitted in table)          |
| temporaryGcsBucket      |  The GCS bucket that temporarily holds the data before it is loaded to BigQuery. Required unless set in the Spark configuration (spark.conf.set(...)).          |

#### Example

```scala
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.bigquery.BigQuery.BigQueryImplicit
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import org.apache.spark.sql.SaveMode

val almaren = Almaren("App Name")

spark.conf.set("gcpAccessToken","token")

almaren.builder
    .sourceSql("""SELECT sha2(concat_ws("",array(*)),256) as id,*,current_timestamp from deputies""")
    .coalesce(30)
    .targetBigQuery("dataset.table",Map("parentProject"->"project_name","project"->"project_name","temporaryGcsBucket"->"bucket"),SaveMode.Overwrite)
    .batch
```

