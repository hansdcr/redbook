package org.example.kafkademo01.admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.util.*;

public class AdminSample {
  public static final String TOPIC_NAME = "hans_topic";

  public static void main(String[] args) throws Exception {
    // AdminClient adminClient = AdminSample.adminClient();

    // createTopic();

    topicLists();
  }

  /*
     获取Topic列表
  */
  public static void topicLists() throws Exception {
    AdminClient adminClient = adminClient();
    ListTopicsResult listTopicsResult = adminClient.listTopics();
    Set<String> names = listTopicsResult.names().get();
    names.stream().forEach(System.out::println);

    //    // 是否查看internal选项
    //    ListTopicsOptions options = new ListTopicsOptions();
    //    options.listInternal(true);
    //    //        ListTopicsResult listTopicsResult = adminClient.listTopics();
    //    ListTopicsResult listTopicsResult = adminClient.listTopics(options);
    //    Set<String> names = listTopicsResult.names().get();
    //    Collection<TopicListing> topicListings = listTopicsResult.listings().get();
    //    KafkaFuture<Map<String, TopicListing>> mapKafkaFuture =
    // listTopicsResult.namesToListings();
    //    // 打印names
    //    names.stream().forEach(System.out::println);
    //    // 打印topicListings
    //    topicListings.stream()
    //        .forEach(
    //            (topicList) -> {
    //              System.out.println(topicList);
    //            });
  }

  /*
     创建Topic实例
  */
  public static void createTopic() {
    AdminClient adminClient = adminClient();
    // 副本因子
    Short rs = 1;
    NewTopic newTopic = new NewTopic(TOPIC_NAME, 1, rs);
    CreateTopicsResult topics = adminClient.createTopics(Arrays.asList(newTopic));
    System.out.println("CreateTopicsResult : " + topics);
  }

  /*
     设置AdminClient
  */
  public static AdminClient adminClient() {
    Properties properties = new Properties();
    properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

    AdminClient adminClient = AdminClient.create(properties);
    return adminClient;
  }
}
