package me.sj.study.stock.facade;

import me.sj.study.stock.repository.RedisLockRepository;
import me.sj.study.stock.service.StockService;
import org.springframework.stereotype.Service;

@Service
public class LettuceLockStockFacade {
    private final RedisLockRepository redisLockRepository;

    private final StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(long id, long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }
}
