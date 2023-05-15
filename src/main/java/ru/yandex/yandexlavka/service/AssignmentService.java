package ru.yandex.yandexlavka.service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.repository.CourierRepository;
import ru.yandex.yandexlavka.repository.OrderRepository;


import java.util.*;

@Service
@AllArgsConstructor
public class AssignmentService {
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;
    public void postAssignOrders(){

    }
}
