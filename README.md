# Kafka Example Showcase: Message Producer and Consumer with Error Handling

### Description

This Kafka example showcase demonstrates a simple yet powerful implementation of a message producer and consumer, highlighting
the robustness of the system when dealing with transient errors. The project provides a RESTful API with endpoints for creating 
transactions and customers, publishing them to Kafka topics, and consuming them with error handling.

### Usage/Installation/Configuration

- written with java 21
- gradle 8.2.1
- spring boot 3.1.2
- checkStyle plugin - **The purpose of the CheckStyle plugin is to identify and flag coding style violations**

To run the application and see it's behavior, first execute **docker-compose up** to spin up the zookeeper and kafka to creat topics.
To do so you'll need:
- docker
- docker-compose

There is a rest controller to expose resources to test, they are:
- /api/v1/producer/transaction - to create a transaction, publish it to topic and consume it.
- /api/v1/producer/retry/transaction - to create a transaction to be retried.
- /api/v1/producer/transaction-to-dead-letter - to create a transaction that won't be retried and goes directly to deadLetter
- /api/v1/producer/customer - to create a customer, publish it to topic and consume it.
- /api/v1/producer/retry/customer - to create a customer to be retried.
- /api/v1/producer/customer-to-dead-letter - to create customer that won't be retried and goes directly to deadLetter.

**e.g. using CURL** curl -X POST http://localhost:8080/api/v1/producer/transaction

The app also has OpenApi integration that can help you trigger a request to follow the app's behavior.
swagger-ui - http://localhost:8080/swagger-ui/index.html

### Why this project?
As a developer myself, I've embarked on a journey to explore the vast possibilities of Apache Kafka - the leading
distributed streaming platform. During my quest, I encountered the need for a resource that goes beyond basic concepts;
a centralized and well-organized repository offering clear explanations, best practices, and practical examples that
truly unlock Kafka's full potential.

I searched for a one-stop solution where developers like me could find comprehensive documentation and real-world use
cases, empowering us to build sophisticated software applications with kafka integration with confidence. However, such a resource seemed
elusive, and that's when the idea for this repo was born.

This project aims to be a comprehensive guide for Apache Kafka integration, providing theoretical knowledge, code samples, 
and tutorials for both beginners and experienced users. I aim to provide not only theoretical knowledge but also hands-on 
code samples and tutorials, enabling developers to grasp complex concepts and apply them in real-world scenarios effectively.

### What You'll Find Here:
- Configuring spring kafka
- Kafka consumer factory
- Kafka producer
- Custom serialization and deserialization
- Dealing with poison pill
- Recover from transient errors
- Integration test using testcontainers library

### First things first, let's start with configuration

The application.yml (or application.properties) file is a configuration file commonly used in Java applications, including
those using the Spring Framework. When configuring a Kafka cluster in a Java application, the application.yml file serves
several important needs. For more details, see [kafka documentation](https://kafka.apache.org/documentation/#consumerconfigs)

### Configuring spring kafka

In the real world, we'll probably end up adding new consumers/producers or removing them. For this reason and also for
following SOLID principles where classes would be open to extension but close to modification I strongly recommend
make the configuration class as simple as possible. The idea is to place in here code that we can share it whenever we need.

That said, the Kafkaconfig.java has only 3 methods:

* validator: method returns an instance of the Validator interface, which is responsible for validating the incoming Kafka
message payloads. By default, Spring Boot uses the LocalValidatorFactoryBean, which is part of the Bean Validation API (JSR 380),
to perform the validation.

* configureKafkaListeners: which implements the KafkaListenerConfigurer interface, is used to configure the Kafka listeners
in the Spring Boot application. By calling registrar.setValidator(validator()), the validator is associated with the Kafka
listeners, allowing message payloads to be automatically validated before being processed by the listeners.

* consumerConfigs: Kafka consumers require various configuration properties to define their behavior, such as connection 
settings, consumer group ID, deserialization settings, and more. The consumerConfigs() method provides a way to encapsulate
and customize these configurations.

### Kafka consumer config

The configuration is designed using the Factory Method pattern, and this design choice offers several benefits.

Factory Method Design Pattern:
The Factory class must implement the ConsumerFactoryCreator<YourClassType> interface, which serves as the Factory 
Method. The Factory Method pattern allows the class to defer the instantiation of a specific factory to its 
subclasses (implementations) while providing a common interface for creating consumer factories. The method createConsumerFactory() 
is the Factory Method, and it creates a ConsumerFactory instance for messages of type you need.

Benefits of the Factory Method pattern in this context:

a. Encapsulation: The Factory Method pattern encapsulates the logic of creating ConsumerFactory instances within the
java class you need. Subclasses can override the method to provide specialized implementations without affecting the core 
logic of the factory.

b. Extensibility: By implementing the Factory Method pattern, the design becomes more extensible. If there is a need to 
create different types of ConsumerFactory instances for other message types, one can easily implement the ConsumerFactoryCreator 
interface for those message types.

c. Flexibility: The Factory Method pattern allows for flexible instantiation of objects. The factory method can accept a
set of configuration properties as input and produce customized ConsumerFactory instances accordingly.

d. Maintainability: Separating the creation of ConsumerFactory instances from the rest of the code enhances code maintainability. 
Changes to the creation logic can be localized to the factory method and its subclasses, making it easier to manage and 
understand.

e. Code Reusability: The Factory Method pattern promotes code reusability by providing a common interface for creating 
objects. If multiple classes need to create ConsumerFactory instances, they can utilize the factory method without 
duplicating the instantiation logic.

In conclusion, by utilizing the Factory Method pattern, provides a robust Kafka configuration, encapsulating the creation
of ConsumerFactory instances for processing messages. This design choice enhances the flexibility, maintainability, and 
extensibility of the code, making it easier to adapt the configuration for different message types and maintain the 
codebase in the long term.

### Kafka producer

Design Pattern: Message Publisher (Publish-Subscribe)

The Message Publisher design pattern is a variation of the Publish-Subscribe pattern. It enables loose coupling between 
components that produce messages (publishers) and those that consume messages (subscribers). The pattern allows publishers 
to publish messages without having direct knowledge of their subscribers. Subscribers, in turn, register interest in specific 
message types and receive messages when they become available.

Benefits: Loose Coupling. The pattern promotes loose coupling between components, as publishers and subscribers don't need 
to be aware of each other's existence. This allows for better maintainability and scalability of the system.

Flexibility: Publishers and subscribers can be added, modified, or removed independently without affecting each other, 
facilitating system flexibility and adaptability.

Asynchronous Processing: Asynchronous message publishing allows decoupling the message production from message consumption, 
enabling better performance and responsiveness.

Scalability: The decoupled nature of the pattern makes it easier to scale the system horizontally, as new instances of publishers 
and subscribers can be added as needed.

Event-Driven: The pattern enables an event-driven architecture, where actions and reactions are based on the occurrence of 
events (messages).

Generic Interface: The use of a generic interface (MessagePublisher<T>) allows the same pattern to be used for different 
message types, promoting code reusability.

Overall, the Message Publisher pattern helps to build a more flexible, scalable, and maintainable system by decoupling message 
production and consumption logic. It is particularly useful in distributed systems, microservices architectures, and event-driven 
applications.

### Custom serialization and deserialization

The class has a constructor that takes a Class<T> targetType as an argument, allowing the deserializer to know the type 
it needs to convert the incoming Kafka message data. 

Deserialization Logic:
The deserialize() method is overridden from the Deserializer interface. It takes the Kafka topic and the message data (byte[]) 
as inputs and returns the deserialized object of type T. The deserialization is performed using the ObjectMapper from Jackson
library, which is a popular Java library for handling JSON data.

In the deserialize() method:

An ObjectMapper is created with specific configurations using the Jackson2ObjectMapperBuilder. It is configured with a JavaTimeModule 
to handle Java 8 time types (e.g., LocalDate, LocalDateTime).
If the received data is null, the method logs a message and returns null. Otherwise, the ObjectMapper is used to read the 
JSON data from the byte[] and deserialize it into an object of type T.
Exception Handling:
If an IOException occurs during the deserialization process, the method catches it, logs an error message indicating the 
failure, and throws a SerializationException.

Benefits of the Generic Type and Factory Method Patterns in this context:

a. Generic Type (Parameterized Type):
The use of a generic type T in the CustomDeserializer class allows the deserializer to handle messages of different types, 
making it reusable and flexible. This eliminates the need to create multiple deserializer classes for different message types.

b. Factory Method Pattern:
Although not explicitly mentioned in the code, the CustomDeserializer class follows a simple form of the Factory Method pattern
by providing a constructor that takes the targetType. It allows for the creation of custom deserializers for different target
types by instantiating the class with the appropriate Class<T>.

c. Reusability and Flexibility:
The combination of the generic type and factory method patterns enhances code reusability and flexibility. The deserializer 
can be used for various target types without needing to modify the core deserialization logic.

d. Clean and Maintainable Code:
By using the generic type and factory method patterns, the deserialization logic is concise and focused. Any changes or 
improvements to the deserialization process can be localized to this class, making the code more maintainable and easier
to understand.

In conclusion, the deserializer and serializer class offers a generic and flexible solution for deserializing/deserializing 
Kafka messages into objects of different types. By utilizing the generic type and factory method patterns, the class provides
clean and maintainable serialization/deserialization logic, allowing developers to handle various data types with ease.

### Dealing with poison pill (dead letter)

In the provided code, there is no explicit handling of poison pills. Working on it!

### Recover from transient errors
The Listener deals with transient errors using the Spring Kafka @RetryableTopic annotation. The @RetryableTopic annotation 
is part of Spring for Apache Kafka and provides a mechanism for handling transient errors that occur during message processing.

Let's look at how the code deals with transient errors using @RetryableTopic:

@RetryableTopic Annotation: The annotation is used on the Kafka listener methods (listenTransaction and listenCustomer) to enable 
retry behavior for those topics.

Retry Configuration: The @RetryableTopic annotation is configured with the following properties:

attempts = "3": Specifies the maximum number of retry attempts. In this case, it allows three retry attempts.

backoff = @Backoff(delay = 1000, multiplier = 3.0): Specifies the backoff configuration for retry attempts. It means that 
if a retry is needed, there will be a delay of 1000 milliseconds (1 second) before the first retry, and then the delay will 
be multiplied by 3.0 for each subsequent retry attempt.

exclude = SerializationException.class: Specifies that the retry mechanism should exclude SerializationException from being retried. 
This means that if a SerializationException occurs during message processing, it will not trigger a retry, and it will be handled 
differently.

Dead-Letter Topic Handling: The @RetryableTopic annotation also works in conjunction with the Dead-Letter Topic (DltStrategy.ALWAYS_RETRY_ON_ERROR). 
The DltStrategy configuration ensures that if the retries are exhausted, and the message processing still fails, the message will be
sent to the Dead-Letter Topic for further analysis and handling.

Here's a summary of how transient errors are handled:

When a transient error occurs during message processing (e.g., network issues, temporary unavailability of resources, etc.), 
the Kafka listener will attempt to retry the message processing up to the specified maximum attempts (3 attempts in this case) 
with the defined backoff delay.

If the message processing is successful within the maximum retry attempts, the message will be successfully processed, and 
the retry process will stop.

If the message processing still fails after exhausting the retry attempts, the DltStrategy.ALWAYS_RETRY_ON_ERROR configuration 
will ensure that the message is sent to the Dead-Letter Topic for further analysis or manual intervention.

By using the @RetryableTopic annotation and the Dead-Letter Topic strategy, the provided code can handle transient errors in a 
resilient manner, automatically retrying message processing and providing a safety net to capture messages that couldn't be 
processed even after retries. This ensures that transient issues are mitigated, and the application can continue to process 
messages effectively in most cases. However, keep in mind that the success of the retry mechanism also depends on the reason 
for the transient errors and the ability of the system to recover from such errors.

To more information, see [Hands On: Error Handling(https://developer.confluent.io/courses/kafka-streams/hands-on-error-handling/)]

### Integration test by Testcontainers library

Testcontainers provides a powerful and efficient approach to integration testing, helping developers build reliable, reproducible, 
and isolated test environments. By leveraging containerization technology, Testcontainers simplifies the management of dependencies
and promotes consistent and automated integration testing in CI/CD workflows.

### Contributing 

Contributions to this kafka project are welcome! If you find any issues, have suggestions for improvements, or would like to add new 
features, please create a pull request or open an issue on the GitHub repository.

### Acknowledgments
Kafka-example was developed by Fabiano Armando as a showcase of skills and to provide a useful tool or start point for others.
Thank you to the open-source community for the inspiration and valuable resources.