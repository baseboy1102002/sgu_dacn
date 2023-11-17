package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "khoa")
@NoArgsConstructor
@Getter
@Setter
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ten_khoa")
    private String name;

    @Column(name = "ma_khoa")
    private String code;

    @Column(name = "trang_thai")
    private boolean status;

    @OneToMany(mappedBy = "faculty")
    private List<Event> events;

    @OneToMany(mappedBy = "faculty")
    private List<Classroom> classrooms;
}
