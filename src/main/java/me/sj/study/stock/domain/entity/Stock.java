package me.sj.study.stock.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "quantity")
    private long quantity;

    public Stock() {

    }

    public Stock(long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }

    public void decrease(long quantity) {
        if(this.quantity - quantity < 0) {
            throw new RuntimeException("재고는 0개 미만이 될 수 없습니다");
        }

        this.quantity -= quantity;
    }
}
