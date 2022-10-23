package org.example.kafkademo01.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProducerSample {
  public static final String TOPIC_NAME = "hans_topic";
  public static final String KAFKA_HOST = "127.0.0.1:9092";

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    //    producerSend();
    //    producerSyncSend();
    producerSendWithCallback();
  }

  /*
     Producer异步发送演示
  */
  public static void producerSend() {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST);
    properties.put(ProducerConfig.ACKS_CONFIG, "all");
    properties.put(ProducerConfig.RETRIES_CONFIG, "0");
    properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
    properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
    properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");

    properties.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    properties.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");

    // Producer的主对象
    Producer<String, String> producer = new KafkaProducer<>(properties);

    // 消息对象 - ProducerRecoder
    for (int i = 0; i < 10; i++) {
      ProducerRecord<String, String> record =
          new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);

      producer.send(record);
    }

    // 所有的通道打开都需要关闭
    producer.close();
  }

  /*
     Producer异步阻塞发送
  */
  public static void producerSyncSend()
      throws ExecutionException, InterruptedException, ExecutionException {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST);
    properties.put(ProducerConfig.ACKS_CONFIG, "all");
    properties.put(ProducerConfig.RETRIES_CONFIG, "0");
    properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
    properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
    properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");

    properties.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    properties.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");

    // Producer的主对象
    Producer<String, String> producer = new KafkaProducer<>(properties);

    // 消息对象 - ProducerRecoder
    for (int i = 0; i < 10; i++) {
      String key = "key-" + i;
      ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, key, "value-" + i);

      Future<RecordMetadata> send = producer.send(record);
      RecordMetadata recordMetadata = send.get();
      System.out.println(
          key
              + " , partition : "
              + recordMetadata.partition()
              + " , offset : "
              + recordMetadata.offset());
    }

    // 所有的通道打开都需要关闭
    producer.close();
  }

  /*
     Producer异步发送带回调函数
  */
  public static void producerSendWithCallback() {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST);
    properties.put(ProducerConfig.ACKS_CONFIG, "all");
    properties.put(ProducerConfig.RETRIES_CONFIG, "0");
    properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
    properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
    properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");

    properties.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    properties.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");

    // Producer的主对象
    Producer<String, String> producer = new KafkaProducer<>(properties);

    // 消息对象 - ProducerRecoder
    for (int i = 0; i < 10; i++) {
      ProducerRecord<String, String> record =
          new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);

      producer.send(
          record,
          new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
              System.out.println(
                  "partition : "
                      + recordMetadata.partition()
                      + " , offset : "
                      + recordMetadata.offset());
            }
          });
    }

    // 所有的通道打开都需要关闭
    producer.close();
  }
}
