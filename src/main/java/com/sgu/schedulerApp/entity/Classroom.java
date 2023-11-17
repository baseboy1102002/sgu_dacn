package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "lop")
@NoArgsConstructor
@Getter
@Setter
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ten_lop")
    private String name;

    @Column(name = "ma_lop")
    private String code;

    @OneToMany(mappedBy = "classroom")
    private List<Event> events;

    @Column(name = "trang_thai")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "id_khoa")
    private Faculty faculty;
}
