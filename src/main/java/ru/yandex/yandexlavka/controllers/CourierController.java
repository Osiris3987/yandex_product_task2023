package ru.yandex.yandexlavka.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.model.CourierMetaInfoResponseDTO;
import ru.yandex.yandexlavka.model.CourierRequestDTO;
import ru.yandex.yandexlavka.model.CourierResponseDTO;
import ru.yandex.yandexlavka.service.CourierService;
import ru.yandex.yandexlavka.service.PaymentRatingService;

import java.util.List;

@RestController
@RequestMapping("/couriers")
@AllArgsConstructor
public class CourierController {

    private final CourierService courierService;
    private final PaymentRatingService paymentRatingService;

    @PostMapping
    public List<CourierResponseDTO> postCouriers(@RequestBody List<CourierRequestDTO> courierRequestDTO) {
        return courierService.createCouriers(courierRequestDTO);
    }

    @GetMapping("/{courierId}")
    public CourierResponseDTO getCourierById(@PathVariable long courierId) {
        return new CourierResponseDTO(courierService.findCourierById(courierId));
    }

    @GetMapping
    public List<CourierResponseDTO> getCouriers(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "1") int limit
    ) {
        return courierService.findAllCouriers(offset, limit);
    }

    @GetMapping("/meta-info/{courierId}")
    public CourierMetaInfoResponseDTO getCouriersPaymentAndRating(
            @PathVariable long courierId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return paymentRatingService.getCouriersPaymentAndRating(courierId, startDate, endDate);
    }
}

