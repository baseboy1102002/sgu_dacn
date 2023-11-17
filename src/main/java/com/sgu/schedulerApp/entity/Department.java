package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "co_so")
@NoArgsConstructor
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ten_cs")
    private String name;
    @Column(name = "ma_cs")
    private String code;

    @Column(name = "trang_thai")
    private boolean status;

    @OneToMany(mappedBy = "department")
    private List<Room> rooms;

}
