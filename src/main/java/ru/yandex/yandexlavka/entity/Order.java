package ru.yandex.yandexlavka.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "my_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderId;
    private Float weight;
    private Integer region;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Period> deliveryTime;
    private Float deliveryCost;
    private LocalDateTime completedTime;
    @ManyToOne(cascade = CascadeType.ALL)
    private Courier courier;
    public Order(Float weight, Integer region, List<Period> deliveryTime, Float deliveryCost) {
        this.weight = weight;
        this.region = region;
        this.deliveryTime = deliveryTime;
        this.deliveryCost = deliveryCost;
    }
}
