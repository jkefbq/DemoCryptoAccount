package com.asettracker.tg.database.service;

import com.asettracker.tg.main.database.entity.BagEntity;
import com.asettracker.tg.main.database.repository.BagRepository;
import com.asettracker.tg.main.database.service.BagDbService;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Sql(value = "/sql/truncate-bags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class TestBagDbService {

    @Autowired
    BagDbService bagDbService;
    @MockitoSpyBean
    BagRepository bagRepository;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Container
    private static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>("redis:7-alpine")
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void setPostgres(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @DynamicPropertySource
    static void setRedis(
            DynamicPropertyRegistry registry
    ) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @BeforeEach
    public void cleanCache() {
        cacheManager.resetCaches();
    }

    @AfterEach
    public void resetBagRepo() {
        Mockito.clearInvocations(bagRepository);
        reset(bagRepository);
    }

    @Test
    public void createBagTest() {
        assertEquals(0, bagRepository.count());

        var bag = new BagEntity(ThreadLocalRandom.current().nextLong());
        bagDbService.createBag(bag);

        verify(bagRepository, times(1)).save(any());
        assertEquals(1, bagRepository.count());
        assertTrue(bagDbService.findBagByChatId(bag.getChatId()).isPresent());
        assertNotNull(bag.getAssets());
    }

    @Test
    @Sql("/sql/insert-bags.sql")
    public void updateBagTest() {
        var countBefore = bagRepository.count();
        var bag = bagRepository.findByChatId(1L).orElseThrow();
        bag.setAssetCount(9999);

        bagDbService.updateBag(bag);

        var countAfter = bagRepository.count();
        verify(bagRepository, times(1)).save(any());
        assertEquals(countBefore, countAfter);
    }

    @Test
    @Sql("/sql/insert-bags.sql")
    public void findByChatIdTest_withoutCache() {
        final var chatId = 1L;
        var bag = bagDbService.findBagByChatId(chatId);

        verify(bagRepository, times(1)).findByChatId(any());
        assertTrue(bag.isPresent());
    }

    @Test
    @Sql("/sql/insert-bags.sql")
    public void findByChatId_cacheTest() throws InterruptedException {
        final var chatId = 1L;

        var bagEntity = bagDbService.findBagByChatId(chatId);
        bagDbService.findBagByChatId(chatId);
        bagDbService.findBagByChatId(chatId);
        bagDbService.findBagByChatId(chatId);

        assertEquals(1, redisTemplate.keys("*").size());
        assertTrue(bagEntity.isPresent());
        verify(bagRepository, times(1)).findByChatId(chatId);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SpringLiquibase springLiquibase(DataSource dataSource) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setChangeLog("classpath:/db/changelog/changelog-master.yaml");
            liquibase.setDataSource(dataSource);
            return liquibase;
        }
    }
}
