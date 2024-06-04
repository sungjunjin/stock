package me.sj.study.stock.service;

import me.sj.study.stock.domain.entity.Stock;
import me.sj.study.stock.facade.RedissonLockStockFacade;
import me.sj.study.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedissonLockStockFacadeTest {

    @Autowired
    RedissonLockStockFacade stockService;

    @Autowired
    StockRepository stockRepository;

    @BeforeEach
    public void beforeEach() {
        Stock stock = new Stock(1L, 100);
        stockRepository.save(stock);
    }

    @AfterEach
    public void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    public void 재고감소_검증() {
        long stockId = 1L;

        stockService.decrease(stockId, 1);

        Stock stock = stockRepository.findById(stockId).orElseThrow();

        assertThat(stock.getQuantity()).isEqualTo(99);
    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        long stockId = 1L;
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(stockId, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock stock = stockRepository.findById(stockId).orElseThrow();
        assertThat(stock.getQuantity()).isEqualTo(0);

    }
}