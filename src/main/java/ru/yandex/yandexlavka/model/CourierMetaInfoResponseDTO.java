package ru.yandex.yandexlavka.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.yandexlavka.enums.CourierType;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@Data
public class CourierMetaInfoResponseDTO {
    @JsonProperty("courier_id")
    private Long courierId;
    @JsonProperty("courier_type")
    private CourierType type;
    private List<Integer> regions;
    @JsonProperty("working_hours")
    private List<String> workingHours;
    private int rating;
    private int earnings;
}
