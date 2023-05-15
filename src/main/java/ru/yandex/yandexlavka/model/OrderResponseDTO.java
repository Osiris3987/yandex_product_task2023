package ru.yandex.yandexlavka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long order_id;
    private Float weight;
    private Integer regions;
    private List<String> delivery_hours;
    private Float cost;
}

