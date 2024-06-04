package me.sj.study.stock.service;

import me.sj.study.stock.domain.entity.Stock;
import me.sj.study.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockPessimisticService implements StockService {
    private final StockRepository stockRepository;

    public StockPessimisticService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    @Override
    public synchronized void decrease(long id, long quantity) {
        Stock stock = stockRepository.findByIdPessimistic(id);
        stock.decrease(1);

        stockRepository.saveAndFlush(stock);
    }
}
