package ru.yandex.yandexlavka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteOrderRequestDTO {
    @JsonProperty("completed_time")
    private String completeTime;
    @JsonProperty("courier_id")
    private Long courierId;
    @JsonProperty("order_id")
    private Long orderId;
}
