package me.sj.study.stock.facade;

import me.sj.study.stock.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;

    private final StockService stockService;

    private final Logger log = LoggerFactory.getLogger(this.toString());

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(long id, long quantity) {
        RLock lock = redissonClient.getLock(String.valueOf(id));

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                log.info("lock 획득 실패");
            }

            stockService.decrease(id, quantity);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
