# BigQuery Connector

[![Build-Status](https://github.com/modakanalytics/bigquery.almaren/actions/workflows/bigquery-almaren-githubactions.yml/badge.svg)](https://github.com/modakanalytics/bigquery.almaren/actions/workflows/bigquery-almaren-githubactions.yml)

BigQuery Connector was implemented using [https://github.com/GoogleCloudDataproc/spark-bigquery-connector](https://github.com/GoogleCloudDataproc/spark-bigquery-connector).
For more details check the following [link](https://github.com/GoogleCloudDataproc/spark-bigquery-connector).

To add Bigquery Almaren dependency to your sbt build:

```
libraryDependencies += "com.github.music-of-the-ainur" %% "bigquery-almaren" % "0.0.7-3.2"
```

To run in spark-shell:

```
spark-shell --master "local[*]" --packages "com.github.music-of-the-ainur:almaren-framework_2.12:0.9.8-3.2,com.github.music-of-the-ainur:bigquery-almaren_2.12:0.0.7-3.2"
```

### Connector Usage

#### Maven / Ivy Package Usage
The connector is also available from the
[Maven Central](https://mvnrepository.com/artifact/com.github.music-of-the-ainur)
repository. It can be used using the `--packages` option or the
`spark.jars.packages` configuration property. Use the following value

| version                    | Connector Artifact                                              |
|----------------------------|-----------------------------------------------------------------|
| Spark 3.3.x and scala 2.13 | `com.github.music-of-the-ainur:bigquery-almaren_2.13:0.0.7-3.3` |
| Spark 3.3.x and scala 2.12 | `com.github.music-of-the-ainur:bigquery-almaren_2.12:0.0.7-3.3` |
| Spark 3.2.x and scala 2.12 | `com.github.music-of-the-ainur:bigquery-almaren_2.12:0.0.7-3.2` |
| Spark 3.1.x and scala 2.12 | `com.github.music-of-the-ainur:bigquery-almaren_2.12:0.0.7-3.1` |
| Spark 2.4.x and scala 2.12 | `com.github.music-of-the-ainur:bigquery-almaren_2.12:0.0.7-2.4` |
| Spark 2.4.x and scala 2.11 | `com.github.music-of-the-ainur:bigquery-almaren_2.11:0.0.7-2.4` |

## Source and Target

### Source 
#### Parameteres

| Parameters | Description             |
|------------|-------------------------|
| table          | The BigQuery table which is present in a dataset in the format [[project:]dataset.]table       |
| options    |  Description  |
|-------------| -------------|
| parentProject   | The Google Cloud resource hierarchy resembles the file system which manages entities hierarchically . The Google Cloud Project ID of the table.  |
| project | The Google Cloud Project ID of the table. A project organizes all your Google Cloud resources .For example, all of your Cloud Storage buckets and objects, along with user permissions for accessing them, reside in a project.                |
| dataset      |  A dataset is contained within a specific project. Datasets are top-level containers that are used to organize and control access to your tables and views        |
|query      |  Standard SQL SELECT query. (Table name should be in grave accent)  |


#### Example 1


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


You can run any Standard SQL SELECT query on BigQuery and fetch its results directly to a Spark Dataframe.        
In order to use this feature the following configurations MUST be set:
* `viewsEnabled` must be set to `true`.
* `materializationDataset` must be set to a dataset where the GCP user has table
  creation permission.
### Example 2
```scala
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.bigquery.BigQuery.BigQueryImplicit
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit

val almaren = Almaren("App Name")

spark.conf.set("gcpAccessToken","token")
spark.conf.set("viewsEnabled","true")
spark.conf.set("materializationDataset","<dataset>")

val df =  almaren
         .builder
         .sourceBigQuery("query",Map("parentProject"->"project_name","project"->"project_name"))
         .batch

df.show(false)
```


### Target:
#### Parameters

| Parameters | Description             |
|------------|-------------------------|
| table          | The BigQuery table which is present in a dataset in the format [[project:]dataset.]table       |
| options    |  Description |
|-------------| -------------|
| parentProject   | The Google Cloud resource hierarchy resembles the file system which manages entities hierarchically . The Google Cloud Project ID of the table.   |
| project | The Google Cloud Project ID of the table. A project organizes all your Google Cloud resources .For example, all of your Cloud Storage buckets and objects, along with user permissions for accessing them, reside in a project.                 |
| dataset      | A dataset is contained within a specific project. Datasets are top-level containers that are used to organize and control access to your tables and views          |
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


