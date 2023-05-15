package ru.yandex.yandexlavka.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.exceptions.CourierNotFoundException;
import ru.yandex.yandexlavka.exceptions.EmptyParameterException;
import ru.yandex.yandexlavka.exceptions.InvalidTimeFormatException;
import ru.yandex.yandexlavka.model.CourierRequestDTO;
import ru.yandex.yandexlavka.model.CourierResponseDTO;
import ru.yandex.yandexlavka.repository.CourierRepository;
import ru.yandex.yandexlavka.util.TimeParser;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {
    private final CourierRepository courierRepository;

    public List<CourierResponseDTO> createCouriers(List<CourierRequestDTO> couriers) {
        try {
            List<Courier> courierEntities = couriers.stream()
                    .map(this::buildCourier)
                    .toList();
            return courierRepository
                    .saveAll(courierEntities)
                    .stream()
                    .map(this::buildResponse)
                    .collect(Collectors.toList()
                    );
        } catch (NullPointerException ex){
            throw new EmptyParameterException("you cannot input null");
        }
    }

    public Courier findCourierById(long courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> new CourierNotFoundException("No such courier exists")
                );
    }

    public List<CourierResponseDTO> findAllCouriers(int offset, int limit) {
        return courierRepository.findAll(PageRequest.of(offset, limit)).getContent()
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    private CourierResponseDTO buildResponse(Courier courier) {
        return new CourierResponseDTO(
                courier.getCourierId(),
                courier.getType(),
                courier.getRegions(),
                courier.getWorkingHours().stream().map(TimeParser::mapPeriodToString).collect(Collectors.toList())
        );
    }

    private Courier buildCourier(CourierRequestDTO courierRequestDTO) {
        if (courierRequestDTO.getRegions().stream().anyMatch(Objects::isNull))
            throw new EmptyParameterException("Your input cannot be null");
        try {
            return new Courier(
                    courierRequestDTO.getType(), courierRequestDTO.getRegions(),
                    courierRequestDTO.getWorkingHours().stream().map(TimeParser::parsePeriod).toList()
            );
        } catch (DateTimeParseException | NullPointerException ex) {
            throw new InvalidTimeFormatException("Invalid time format provided");
        }
    }
}

