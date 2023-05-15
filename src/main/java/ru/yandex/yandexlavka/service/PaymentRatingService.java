package ru.yandex.yandexlavka.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.exceptions.CourierNotFoundException;
import ru.yandex.yandexlavka.exceptions.InvalidTimeFormatException;
import ru.yandex.yandexlavka.model.CourierMetaInfoResponseDTO;
import ru.yandex.yandexlavka.repository.CourierRepository;
import ru.yandex.yandexlavka.util.TimeParser;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentRatingService {

    private final CourierRepository courierRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CourierMetaInfoResponseDTO getCouriersPaymentAndRating(Long courierId, String startDate, String endDate) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new CourierNotFoundException("Courier not found"));

        List<Order> orders = courier.getOrders();
        int coefficient = switch (courier.getType()) {
            case FOOT -> 1;
            case BIKE -> 2;
            case CAR -> 3;
        };
        try {
            int payment = (int) orders.stream()
                    .filter(order -> courierMatchesCondition(order, courierId, startDate, endDate))
                    .mapToDouble(Order::getDeliveryCost).sum() * coefficient;

            int rating = (int) ratingCalculation(orders, courierId, startDate, endDate, coefficient);
            return new CourierMetaInfoResponseDTO(courierId, courier.getType(), courier.getRegions(),
                    courier.getWorkingHours().stream().map(TimeParser::mapPeriodToString).toList(),
                    rating, payment);
        } catch (DateTimeParseException | ArithmeticException e) {
            throw new InvalidTimeFormatException("Your input is invalid");
        }
    }

    private boolean courierMatchesCondition(Order order, Long courierId, String startDate, String endDate) {
        LocalDate orderDate = order.getCompletedTime().toLocalDate();
        LocalDate startDateObj = LocalDate.parse(startDate, formatter);
        LocalDate endDateObj = LocalDate.parse(endDate, formatter);

        return order.getCourier().getCourierId().equals(courierId)
                && !orderDate.isBefore(startDateObj)
                && !orderDate.isAfter(endDateObj);
    }

    private long ratingCalculation(List<Order> orders, Long courierId, String startDate, String endDate, int coefficient) {
        return orders.stream()
                .filter(order ->
                        order.getCourier().getCourierId().equals(courierId)
                                && !order.getCompletedTime().toLocalDate().atStartOfDay().isAfter(LocalDate.parse(endDate, formatter).atStartOfDay())
                                && !order.getCompletedTime().toLocalDate().atStartOfDay().isBefore(LocalDate.parse(startDate, formatter).atStartOfDay())
                )
                .count() / calculateHoursBetween(LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter)) * coefficient;
    }

    private long calculateHoursBetween(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();
        Duration duration = Duration.between(startDateTime, endDateTime);
        return duration.toHours();
    }
}
