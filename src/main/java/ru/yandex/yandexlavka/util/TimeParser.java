package ru.yandex.yandexlavka.util;

import ru.yandex.yandexlavka.entity.Period;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeParser {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;
    private static final DateTimeFormatter formatterToString = DateTimeFormatter.ofPattern("HH:mm");

    public static String mapPeriodToString(Period period) {
        return period.getFr().format(formatterToString) + "-" + period.getT().format(formatterToString);
    }

    public static Period parsePeriod(String input) {
        String[] times = input.split("-");
        LocalTime from = LocalTime.parse(times[0], formatter);
        LocalTime to = LocalTime.parse(times[1], formatter);
        return new Period(from, to);
    }
}
