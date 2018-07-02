import java.io.*;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.csv.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.simple.JSONValue;

/**
 * Created by Micah on 7/1/2018.
 */
public class UploadFromFile {

    static final String MUSHROOM_TOPIC_NAME = "mushrooms";

    /**
     * args[0] = bootstrap servers
     * args[1] = mushroom filepath
     * @param args
     */
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, args[0]);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //open the file
        try (CSVParser reader = CSVFormat.DEFAULT.withHeader().parse(new BufferedReader(new FileReader(args[1])))){

            //stream the file to Kafka
            try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)){
                for (CSVRecord mushroom : reader){
                    producer.send(
                            new ProducerRecord<>(MUSHROOM_TOPIC_NAME, JSONValue.toJSONString(mushroom.toMap())));
                }
            }
        } catch (IOException e){
            System.err.println(e);
        }
    }

}
