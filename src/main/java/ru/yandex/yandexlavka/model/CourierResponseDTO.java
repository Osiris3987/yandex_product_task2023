package ru.yandex.yandexlavka.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.enums.CourierType;
import ru.yandex.yandexlavka.util.TimeParser;

import java.util.List;
@Getter
@Setter
public class CourierResponseDTO {
    @JsonProperty("courier_id")
    private Long courierId;
    @JsonProperty("courier_type")
    private CourierType type;
    private List<Integer> regions;
    @JsonProperty("working_hours")
    private List<String> workingHours;

    public CourierResponseDTO(Long courierId, CourierType type, List<Integer> regions, List<String> workingHours) {
        this.courierId = courierId;
        this.type = type;
        this.regions = regions;
        this.workingHours = workingHours;
    }
    public CourierResponseDTO(Courier courier){
        this.courierId = courier.getCourierId();
        this.type = courier.getType();
        this.regions = courier.getRegions();
        this.workingHours = courier.getWorkingHours().stream().map(TimeParser::mapPeriodToString).toList();
    }
}
