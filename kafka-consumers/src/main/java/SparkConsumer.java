import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Micah on 7/4/2018.
 */
public class SparkConsumer {

    /**
     * args[0]: broker list
     * args[1]: topic name (only handling single topics atm)
     * @param args
     */
    public static void main(String[] args) throws InterruptedException{


        JavaStreamingContext sc = new JavaStreamingContext(new SparkConf(), Durations.seconds(2));

        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", args[0]);

        Set<String> topics = new HashSet<>();
        topics.add(args[1]);

        JavaPairInputDStream<String, String> directKafkaStream = KafkaUtils.createDirectStream(
                sc, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);

        directKafkaStream.foreachRDD(rdd -> {
            rdd.values().collect().stream().forEach(s ->{
                System.out.println(s);
            });
        });

        sc.start();
        sc.awaitTermination();
    }
}
