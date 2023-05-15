package ru.yandex.yandexlavka.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Table(name = "period")
public class Period {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalTime fr;
    private LocalTime t;

    public Period(LocalTime fr, LocalTime t) {
        this.fr = fr;
        this.t = t;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getFr() {
        return fr;
    }

    public void setFr(LocalTime from) {
        this.fr = from;
    }

    public LocalTime getT() {
        return t;
    }

    public void setT(LocalTime to) {
        this.t = to;
    }
}
