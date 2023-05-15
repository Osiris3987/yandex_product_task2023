package ru.yandex.yandexlavka.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.entity.Courier;
import ru.yandex.yandexlavka.entity.Order;
import ru.yandex.yandexlavka.exceptions.*;
import ru.yandex.yandexlavka.model.CompleteOrderRequestDTO;
import ru.yandex.yandexlavka.model.CompleteOrderResponseDTO;
import ru.yandex.yandexlavka.model.OrderRequestDTO;
import ru.yandex.yandexlavka.model.OrderResponseDTO;
import ru.yandex.yandexlavka.repository.CourierRepository;
import ru.yandex.yandexlavka.repository.OrderRepository;
import ru.yandex.yandexlavka.util.TimeParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    public List<OrderResponseDTO> createOrders(List<OrderRequestDTO> orderRequestDTOList) {
        try {
            List<Order> orderList = orderRequestDTOList.stream()
                    .map(this::buildOrder)
                    .collect(Collectors.toList());
            return orderRepository.saveAll(orderList).stream()
                    .map(this::buildResponse)
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new InvalidTimeFormatException("Invalid time format provided");
        }
    }

    public CompleteOrderResponseDTO findOrderById(long orderId) {
        return buildCompleteResponse(orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"))
        );
    }

    public List<CompleteOrderResponseDTO> findAllOrders(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by("orderId").ascending());
        return orderRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(this::buildCompleteResponse)
                .collect(Collectors.toList());
    }

    public CompleteOrderResponseDTO postCompleteOrder(CompleteOrderRequestDTO completeOrderRequestDTO) {
        if (completeOrderRequestDTO.getCourierId() == null) {
            throw new CourierNotFoundException("Courier ID cannot be null");
        }
        if (completeOrderRequestDTO.getCompleteTime() == null) {
            throw new EmptyParameterException("Your input cannot be null");
        }
        if (completeOrderRequestDTO.getOrderId() == null) {
            throw new EmptyParameterException("Your input cannot be null");
        }

        Order order = orderRepository.findById(completeOrderRequestDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("No such order exists"));

        if (order.getCourier() != null) {
            // Если заказ уже имеет курьера, сравниваем с данными входного запроса
            if (!order.getCourier().getCourierId().equals(completeOrderRequestDTO.getCourierId())) {
                throw new OrderAlreadyHasCourierException("Order is already assigned to another courier");
            } else {
                // Заказ уже выполнен с текущим курьером, возвращаем успешный ответ
                return new CompleteOrderResponseDTO(order);
            }
        }

        Courier courier = courierRepository.findById(completeOrderRequestDTO.getCourierId())
                .orElseThrow(() -> new CourierNotFoundException("No such courier exists"));

        if (courier.getRegions().contains(order.getRegion()) && order.getCourier() == null) {
            order.setCompletedTime(LocalDateTime.parse(completeOrderRequestDTO.getCompleteTime()));
            order.setCourier(courier);
            courier.assignOrder(order);
            orderRepository.save(order);
            return new CompleteOrderResponseDTO(order);
        } else {
            throw new RegionsNotMatchException("The courier cannot deliver in this region");
        }
    }

    public List<CompleteOrderResponseDTO> createCompleteOrders(List<CompleteOrderRequestDTO> completeOrderRequestDTOS) {
        return completeOrderRequestDTOS
                .stream()
                .map(this::postCompleteOrder)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO buildResponse(Order order) {
        return new OrderResponseDTO(
                order.getOrderId(),
                order.getWeight(),
                order.getRegion(),
                order.getDeliveryTime().stream().map(TimeParser::mapPeriodToString).toList(),
                order.getDeliveryCost()
        );
    }

    private CompleteOrderResponseDTO buildCompleteResponse(Order order){
        return new CompleteOrderResponseDTO(order);
    }

    private Order buildOrder(OrderRequestDTO orderRequestDTO){
        if(orderRequestDTO.getWeight() == null || orderRequestDTO.getRegion() == null || orderRequestDTO.getDeliveryCost() == null)
            throw new EmptyParameterException("You cannot input null");
        try {
            return new Order(orderRequestDTO.getWeight(), orderRequestDTO.getRegion(), orderRequestDTO.getDeliveryDate().stream().map(TimeParser::parsePeriod).toList(), orderRequestDTO.getDeliveryCost());
        } catch (NullPointerException e){
            throw new InvalidTimeFormatException("Date cannot be null");
        }
    }
}
