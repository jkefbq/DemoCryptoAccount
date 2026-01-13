package com.asettracker.tg.main.database.service;

import com.asettracker.tg.main.database.dto.BagDto;
import com.asettracker.tg.main.database.entity.BagEntity;
import com.asettracker.tg.main.database.mapper.BagMapper;
import com.asettracker.tg.main.database.repository.BagRepository;
import com.asettracker.tg.main.menu.asset_list_menu.UserChoose;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Sql(value = "/sql/truncate-bags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class BagDbServiceTest {

    @Autowired
    BagDbService bagDbService;
    @MockitoSpyBean
    BagRepository bagRepository;
    @MockitoSpyBean
    BagMapper mapper;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:18-alpine")
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
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @BeforeEach
    public void resetBagRepo() {
        Mockito.clearInvocations(bagRepository);
    }

    @Test
    @Transactional
    public void toDtoTest() {
        BagEntity entity = new BagEntity(ThreadLocalRandom.current().nextLong());

        BagDto dto = bagDbService.toDto(entity);

        verify(mapper, times(1)).toDto(entity);
        assertEquals(entity.getChatId(), dto.getChatId());
        assertEquals(entity.getAssets(), dto.getAssets());
        assertEquals(entity.getVersion(), dto.getVersion());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @Transactional
    public void toEntity() {
        BagDto dto = BagDto.builder()
                .assets(new HashMap<>())
                .createdAt(LocalDate.now())
                .chatId(ThreadLocalRandom.current().nextLong())
                .totalCost(BigDecimal.ZERO)
                .assetCount(0)
                .build();

        BagEntity entity = bagDbService.toEntity(dto);

        verify(mapper, times(1)).toEntity(dto);
        assertEquals(entity.getChatId(), dto.getChatId());
        assertEquals(entity.getAssets(), dto.getAssets());
        assertEquals(entity.getVersion(), dto.getVersion());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @Transactional
    public void createBagTest_BagEntity() {
        assertEquals(0, bagRepository.count());

        var bag = new BagEntity(ThreadLocalRandom.current().nextLong());
        bagDbService.createBag(bag);

        verify(bagRepository, times(1)).save(any());
        assertEquals(1, bagRepository.count());
        assertTrue(bagRepository.findByChatId(bag.getChatId()).isPresent());
        assertNotNull(bag.getAssets());
    }

    @Test
    @Transactional
    public void createBagTest_BagDto_withMapper() {
        BagDto bagDto = BagDto.builder()
                .assets(new HashMap<>())
                .createdAt(LocalDate.now())
                .chatId(ThreadLocalRandom.current().nextLong())
                .totalCost(BigDecimal.ZERO)
                .assetCount(0)
                .build();
        var bagEntity = bagDbService.toEntity(bagDto);

        bagDbService.createBag(bagEntity);

        verify(bagRepository, times(1)).save(any());
        assertEquals(1, bagRepository.count());
        assertTrue(bagRepository.findByChatId(bagEntity.getChatId()).isPresent());
        assertNull(bagDto.getVersion());
        assertNotNull(bagEntity.getAssets());
    }

    @Test
    @Transactional
    @Sql("/sql/insert-bags.sql")
    public void updateBagTest_withoutCache() {
        var countBefore = bagRepository.count();
        BagEntity bag = bagRepository.findByChatId(1L).orElseThrow();
        bag.setAssetCount(9999);

        bagDbService.updateBag(bag);

        var countAfter = bagRepository.count();
        verify(bagRepository, times(1)).save(any());
        assertEquals(countBefore, countAfter);
    }

    @Test
    @Transactional
    public void updateBag_cacheTest() {
        BagDto cacheBag = BagDto.builder()
                .assets(new HashMap<>())
                .createdAt(LocalDate.now())
                .chatId(ThreadLocalRandom.current().nextLong())
                .totalCost(BigDecimal.ZERO)
                .assetCount(0)
                .build();
        final String KEY = "bags::" + cacheBag.getChatId();
        redisTemplate.opsForValue().set(KEY, cacheBag);
        bagRepository.save(bagDbService.toEntity(cacheBag));

        BagEntity bagToUpdate = bagRepository.findByChatId(cacheBag.getChatId()).orElseThrow();
        BagDto updatedBag = bagDbService.updateBag(bagToUpdate);

        assertEquals(1, redisTemplate.keys("*").size());
        assertNotEquals(cacheBag, redisTemplate.opsForValue().get(KEY));
        assertEquals(updatedBag, redisTemplate.opsForValue().get(KEY));
        assertNotEquals(updatedBag.getVersion(), bagRepository.findByChatId(cacheBag.getChatId()).get().getVersion());
    }

    @Test
    @Transactional
    @Sql("/sql/insert-bags.sql")
    public void findByChatIdTest_withoutCache() {
        final var chatId = 1L;
        Optional<BagDto> bag = bagDbService.findBagByChatId(chatId);

        verify(bagRepository, times(1)).findByChatId(any());
        assertTrue(bag.isPresent());
        assertEquals(chatId, bag.get().getChatId());
    }

    @Test
    @Transactional
    @Sql("/sql/insert-bags.sql")
    public void findByChatId_cacheTest() {
        final Long chatId = 1L;

        Optional<BagDto> bagEntity1 = bagDbService.findBagByChatId(chatId);
        Optional<BagDto> bagEntity2 = bagDbService.findBagByChatId(chatId);
        bagDbService.findBagByChatId(chatId);
        bagDbService.findBagByChatId(chatId);

        assertTrue(bagEntity1.isPresent());
        assertTrue(bagEntity2.isPresent());
        assertEquals(bagEntity1.get(), bagEntity2.get());
        assertEquals(bagEntity1.get(), redisTemplate.opsForValue().get("bags::1"));
        verify(bagRepository, times(1)).findByChatId(chatId);
    }

    @Test
    @Transactional
    public void addAssetTest() {
        var chatId = ThreadLocalRandom.current().nextLong();
        BigDecimal coinCount = BigDecimal.valueOf(0.32425);
        bagRepository.save(new BagEntity(chatId));

        System.out.println(redisTemplate.keys("*"));
        System.out.println(bagRepository.count());
        System.out.println(bagRepository.findAll());
        bagDbService.addAsset(new UserChoose("BTC", coinCount), chatId);
        System.out.println(bagRepository.findAll());


        assertTrue(bagRepository.findByChatId(chatId).get().getAssets().containsKey("BTC"));
        assertEquals(coinCount, bagRepository.findByChatId(chatId).get().getAssets().get("BTC"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SpringLiquibase springLiquibase(DataSource dataSource) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setChangeLog("classpath:/db/changelog/db.changelog-master.yaml");
            liquibase.setDataSource(dataSource);
            return liquibase;
        }
    }
}
