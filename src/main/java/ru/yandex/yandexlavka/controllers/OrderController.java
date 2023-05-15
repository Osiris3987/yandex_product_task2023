package ru.yandex.yandexlavka.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.model.CompleteOrderRequestDTO;
import ru.yandex.yandexlavka.model.CompleteOrderResponseDTO;
import ru.yandex.yandexlavka.model.OrderRequestDTO;
import ru.yandex.yandexlavka.model.OrderResponseDTO;
import ru.yandex.yandexlavka.service.OrderService;
import java.util.List;

@RestController
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public List<OrderResponseDTO> postOrders(@RequestBody List<OrderRequestDTO> orderRequestDTO){
        return orderService.createOrders(orderRequestDTO);
    }
    @GetMapping("/orders/{id}")
    public CompleteOrderResponseDTO getOrderById(@PathVariable long id){
        return orderService.findOrderById(id);
    }
    @GetMapping("/orders")
    public List<CompleteOrderResponseDTO> findAllOrders(@RequestParam(required = false, defaultValue = "0") int offset,
                                                @RequestParam(required = false, defaultValue = "1") int limit) {
        return orderService.findAllOrders(offset, limit);
    }
    @PostMapping("/orders/complete")
    public List<CompleteOrderResponseDTO> postCompleteOrders(@RequestBody List<CompleteOrderRequestDTO> completeOrderRequestDTO){
        return orderService.createCompleteOrders(completeOrderRequestDTO);
    }
}
