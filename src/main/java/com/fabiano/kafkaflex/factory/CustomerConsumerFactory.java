package com.fabiano.kafkaflex.factory;

import com.fabiano.kafkaflex.config.KafkaConfig;
import com.fabiano.kafkaflex.custom.CustomDeserializer;
import com.fabiano.kafkaflex.model.Customer;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class CustomerConsumerFactory extends KafkaConfig implements ConsumerFactoryCreator<Customer> {

  @Autowired
  public CustomerConsumerFactory(KafkaProperties kafkaProperties) {
    super(kafkaProperties);
  }

  @Override
  public <K> ConsumerFactory<K, Customer> createConsumerFactory(Map<String, Object> props) {
    DefaultKafkaConsumerFactory<K, Customer> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
    consumerFactory.setValueDeserializer(new CustomDeserializer<>(Customer.class));
    return consumerFactory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Customer> customerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Customer> containerFactory =
        new ConcurrentKafkaListenerContainerFactory<>();
    containerFactory.setConsumerFactory(createConsumerFactory(consumerConfigs()));
    return containerFactory;
  }
}
