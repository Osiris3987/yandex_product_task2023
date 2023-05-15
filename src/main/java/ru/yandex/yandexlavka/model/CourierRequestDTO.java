package ru.yandex.yandexlavka.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.yandexlavka.enums.CourierType;

import java.util.List;
@Getter
@Setter
public class CourierRequestDTO {
    @JsonProperty("courier_type")
    private CourierType type;
    private List<Integer> regions;
    @JsonProperty("working_hours")
    private List<String> workingHours;
}
