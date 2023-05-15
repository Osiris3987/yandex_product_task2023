package ru.yandex.yandexlavka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.util.TimeParser;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CompleteOrderResponseDTO {
    @JsonProperty("order_id")
    private Long orderId;
    private Float weight;
    @JsonProperty("regions")
    private Integer region;
    @JsonProperty("delivery_hours")
    private List<String> deliveryHours;
    @JsonProperty("cost")
    private Float deliveryCost;
    @JsonProperty("completed_time")
    private LocalDateTime completeTime;
    public CompleteOrderResponseDTO(Order order){
        this.orderId = order.getOrderId();
        this.weight = order.getWeight();
        this.region = order.getRegion();
        this.deliveryHours = order.getDeliveryTime().stream().map(TimeParser::mapPeriodToString).toList();
        this.deliveryCost = order.getDeliveryCost();
        this.completeTime = order.getCompletedTime();
    }
}
