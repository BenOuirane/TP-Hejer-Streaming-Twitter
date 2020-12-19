
import com.amazonaws.auth.{AWSStaticCredentialsProvider, DefaultAWSCredentialsProviderChain}
import org.apache.spark.{SparkConf, rdd}
import org.apache.spark.streaming.twitter.TwitterUtils
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder
import com.amazonaws.services.kinesis.model.{PutRecordsRequest, PutRecordsRequestEntry, PutRecordsResult}

import java.nio.ByteBuffer
import java.util

object TestPush {


  def main(args: Array[String]): Unit = {



    val twitterCredentials = new Array[String](4);

    //consumerKey
    twitterCredentials(0) = "";
    //consumerSecret
    twitterCredentials(1) = "";

    //accessToken
    twitterCredentials(2) = "";
    //accessTokenSecret
    twitterCredentials(3) = "";


    val appName = "TweeterStream"
    val conf = new SparkConf().setAppName(appName).setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(1))
    val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) =
      twitterCredentials.take(4)

    val filters = args.takeRight(args.length - 4)

    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)
    val auth = new OAuthAuthorization(cb.build)

    val tweets = TwitterUtils.createStream(ssc, Some(auth), filters)
    val englishTweets = tweets.filter(_.getLang() == "en")

    englishTweets.print()
    ssc.start()
    ssc.awaitTermination()








    val credentials = new DefaultAWSCredentialsProviderChain
    val kinesisClient = AmazonKinesisClientBuilder.standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials.getCredentials))
      .withRegion("us-east-1").build()

    val putRecordsRequest = new PutRecordsRequest
    putRecordsRequest.setStreamName("tf-teraform-kinesis-kinesis-stream")
   val putRecordsRequestEntryList = new util.ArrayList[PutRecordsRequestEntry]
    englishTweets.foreachRDD { rdd =>
      rdd.foreach { s =>
        val putRecordsRequestEntry = new PutRecordsRequestEntry
        putRecordsRequestEntry.setData(ByteBuffer.wrap(s.getText.getBytes))
        putRecordsRequestEntry.setPartitionKey(String.format("test"))
        putRecordsRequest.setRecords(
          new java.util.ArrayList[PutRecordsRequestEntry] {
            {
              add(putRecordsRequestEntry)
            }
          }
        )
      }
    }

   putRecordsRequest.setRecords(putRecordsRequestEntryList)
    val putRecordsResult: PutRecordsResult = kinesisClient.putRecords(putRecordsRequest)
    System.out.println("Put Result" + putRecordsResult)

  }
}
