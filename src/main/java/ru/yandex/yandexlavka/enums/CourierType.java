package ru.yandex.yandexlavka.enums;

public enum CourierType {
    FOOT(10, 2),
    BIKE(20, 4),
    CAR(40, 7);

    private final int weight;
    private final int orderCapacity;

    CourierType(int weight, int orderCapacity) {
        this.weight = weight;
        this.orderCapacity = orderCapacity;
    }

    public int getWeight() {
        return weight;
    }

    public int getOrderCapacity() {
        return orderCapacity;
    }
}
