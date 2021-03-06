package edu.knoldus

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.apache.spark.SparkConf

object GlobalObject extends App {

  Logger.getLogger("org").setLevel(Level.OFF)

  val conf = new SparkConf()
    .setMaster("local[4]")
    .setAppName("Custom Receiver")

  val streamingContext = new StreamingContext(conf, Milliseconds(2100))
  val customReceiverStream = streamingContext.receiverStream(new CustomReceiver)

  customReceiverStream.foreachRDD { data =>
    println(data.collect().toList)

  }

  val numberOfValues = customReceiverStream.count()

  numberOfValues.foreachRDD(result => println("Number of records are : " + result.collect().mkString("\n")))

  streamingContext.start()
  streamingContext.awaitTermination()

}
