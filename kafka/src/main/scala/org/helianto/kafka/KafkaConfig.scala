package org.helianto.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.{EnableKafka, KafkaListener}
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core._
import java.util.HashMap
import java.util.Map
import java.util.concurrent.CountDownLatch

@Configuration
@EnableKafka class KafkaConfig {

  @Value("${helianto.kafka.server}") private val kafkaServer: String = null

  @Bean private[kafka] def kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory[Integer, String] = {
    val factory = new ConcurrentKafkaListenerContainerFactory[Integer, String]
    factory.setConsumerFactory(consumerFactory)
    factory
  }

  @Bean def consumerFactory = new DefaultKafkaConsumerFactory[Integer, String](consumerConfigs)

  @Bean def consumerConfigs: java.util.Map[String, AnyRef] = {
    val props = new java.util.HashMap[String, AnyRef]
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
    props
  }

  @Bean def listener = new Listener

  @Bean def producerFactory: ProducerFactory[Integer, String] = {
    return new DefaultKafkaProducerFactory[Integer, String](producerConfigs)
  }

  @Bean def producerConfigs: java.util.Map[String, AnyRef] = {
    val props = new java.util.HashMap[String, AnyRef]
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
    props
  }

  @Bean def kafkaTemplate = new KafkaTemplate[Integer, String](producerFactory)

}

class Listener {

  final private val latch1: CountDownLatch = new CountDownLatch(1)

  @KafkaListener(id = "foo", topics = Array("annotated1"))
  def listen1(foo: String) = latch1.countDown

}
