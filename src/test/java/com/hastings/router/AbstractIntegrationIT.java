package com.hastings.router;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Initializes the It test classes that extend this. Us this to create tables, insert test data etc.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {EnterpriseRouterApplication.class}, webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationIT.Initializer.class)
public abstract class AbstractIntegrationIT {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractIntegrationIT.class);

    private static final int DYNAMO_PORT = 8000;

//    public static GenericContainer dynamoDb = new GenericContainer("amazon/dynamodb-local").withExposedPorts(DYNAMO_PORT);

//    static {
//        dynamoDb.start();
//    }

//    public abstract void createTable();

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            LOG.debug("In Initialize - creating dynamodb instance");

            String endpoint = "";
//            String endpoint = String.format("aws.dynamodb.endpoint=http://%s:%s",
//                    dynamoDb.getContainerIpAddress(),
//                    dynamoDb.getMappedPort(DYNAMO_PORT));

            TestPropertyValues.of(endpoint).applyTo(configurableApplicationContext);
        }

    }
}