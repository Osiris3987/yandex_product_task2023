package ru.yandex.yandexlavka.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderRequestDTO {
    private Float weight;
    @JsonProperty("regions")
    private Integer region;
    @JsonProperty("delivery_time")
    private List<String> deliveryDate;
    @JsonProperty("cost")
    private Float deliveryCost;
}
