package com.jupitertools.springdynamicpropertyresolver.integrationtests.testcontainers;

import com.jupitertools.springdynamicpropertyresolver.DynamicTestProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RedisContainerInitializationTest.class)
@Testcontainers
class RedisContainerInitializationTest {

	private static final Integer REDIS_PORT = 6379;

	@Container
	private static GenericContainer redis = new GenericContainer("redis:latest")
			.withExposedPorts(REDIS_PORT);

	@DynamicTestProperty
	private static TestPropertyValues props() {
		return TestPropertyValues.of("spring.redis.host=" + redis.getContainerIpAddress(),
		                             "spring.redis.port=" + redis.getMappedPort(REDIS_PORT));
	}

	@Value("${spring.redis.port}")
	private String port;

	@Value("${spring.redis.host}")
	private String host;

	@Test
	void container() {
		assertThat(host).isNotNull();
		assertThat(port).isNotNull();
	}
}
