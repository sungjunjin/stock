package me.sj.study.stock.service;

import me.sj.study.stock.domain.entity.Stock;
import me.sj.study.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockBasicService implements StockService {
    private final StockRepository stockRepository;

    public StockBasicService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    @Override
    public void decrease(long id, long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
