package cl.toncs.st.rest;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class BaseTestContainer {

    private static final MySQLContainer mySQLContainer = (MySQLContainer)new MySQLContainer("mysql:5.7")
        .withReuse(true)
        .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));

    static {
        mySQLContainer.start();
    }

    // connect spring app to mysql container
    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

}