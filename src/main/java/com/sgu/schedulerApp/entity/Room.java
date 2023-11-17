package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "phong_hoc")
@NoArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ma_phong")
    private String code;

    @OneToMany(mappedBy = "room")
    private List<Event> events;

    @Column(name = "trang_thai")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "id_co_so")
    private Department department;

}
