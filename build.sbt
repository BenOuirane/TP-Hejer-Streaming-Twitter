name := "tony"

version := "0.1"

scalaVersion := "2.11.11"




libraryDependencies += "com.amazonaws" % "aws-java-sdk-kinesis" % "1.11.880"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-core" % "1.11.880"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-kinesis" % "1.11.880"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-core" % "1.11.880"
libraryDependencies += "org.apache.bahir" %% "spark-streaming-twitter" % "2.3.2"

// https://mvnrepository.com/artifact/org.apache.spark/spark-hive
//libraryDependencies += "org.apache.bahir" %% "spark-streaming-twitter" % "1.6.2"


libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.2.0",
  "org.apache.spark" %% "spark-streaming" % "2.4.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.2.0",
  "org.apache.spark" % "spark-mllib_2.10" % "1.1.0"
)
