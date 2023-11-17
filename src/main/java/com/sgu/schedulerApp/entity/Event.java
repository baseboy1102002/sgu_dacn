package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "su_kien")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ten_su_kien")
    private String name;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String description;

    @Column(name = "ngay", columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "bat_dau", columnDefinition = "TIME")
    private LocalTime startTime;

    @Column(name = "ket_thuc", columnDefinition = "TIME")
    private LocalTime endTime;

    @Column(name = "sl_cho")
    private Integer numOfSeat;

    @ManyToOne
    @JoinColumn(name = "id_phong")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "id_khoa")
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "id_lop")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_tao")
    private User user;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventStudent> students;

//    @ManyToMany
//    @JoinTable(
//            name = "sv_tham_du_sk",
//            joinColumns = @JoinColumn(name = "id_sk"),
//            inverseJoinColumns = @JoinColumn(name = "id_sv")
//    )
//    private List<StudentInfo> students;
}
