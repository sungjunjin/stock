package me.sj.study.stock.repository;

import me.sj.study.stock.domain.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
