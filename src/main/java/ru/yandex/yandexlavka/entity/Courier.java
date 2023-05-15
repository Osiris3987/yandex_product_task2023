package ru.yandex.yandexlavka.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.yandexlavka.enums.CourierType;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "courier_id")
    private Long courierId;
    @Column(columnDefinition = "TEXT")
    private CourierType type;
    @ElementCollection
    private List<Integer> regions;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Period> workingHours;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // почему mappedBy = "courier" испортил работу с бд!!!
    private List<Order> orders;

    private Integer weight;
    private Integer orderCapacity;

    public Courier(CourierType type, List<Integer> regions, List<Period> workingHours) {
        this.type = type;
        this.regions = regions;
        this.workingHours = workingHours;
        this.weight = type.getWeight();
        this.orderCapacity = type.getOrderCapacity();
    }

    public void assignOrder(Order order) {
        orders.add(order);
    }
}
