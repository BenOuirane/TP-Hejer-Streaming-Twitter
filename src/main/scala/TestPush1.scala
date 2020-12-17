import com.amazonaws.auth.{AWSStaticCredentialsProvider, DefaultAWSCredentialsProviderChain}

object TestPush1 {
  def main(args: Array[String]): Unit = {

    import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder
    import com.amazonaws.services.kinesis.model.PutRecordsRequest
    import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry
    import com.amazonaws.services.kinesis.model.PutRecordsResult
    import java.nio.ByteBuffer
    import java.util

    val credentials = new DefaultAWSCredentialsProviderChain
    val kinesisClient = AmazonKinesisClientBuilder.standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials.getCredentials))
      .withRegion("us-east-1").build()

    val putRecordsRequest = new PutRecordsRequest
    putRecordsRequest.setStreamName("tf-teraform-kinesis-kinesis-stream")
    val putRecordsRequestEntryList = new util.ArrayList[PutRecordsRequestEntry]
    for (i <- 0 until 100) {
      val myString : String = "hajerPatronnnnnnnnnnnef"+i+";"+i+"\n"
      val putRecordsRequestEntry = new PutRecordsRequestEntry
      putRecordsRequestEntry.setData(ByteBuffer.wrap(myString.getBytes))
      putRecordsRequestEntry.setPartitionKey(String.format("test"))
      putRecordsRequestEntryList.add(putRecordsRequestEntry)
    }

    putRecordsRequest.setRecords(putRecordsRequestEntryList)
   val putRecordsResult: PutRecordsResult = kinesisClient.putRecords(putRecordsRequest)
    System.out.println("Put Result" + putRecordsResult)
  }
}
