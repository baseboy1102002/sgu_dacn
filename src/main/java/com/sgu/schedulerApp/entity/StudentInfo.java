package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "sinh_vien")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ma_sv")
    private String studentCode;

    @OneToOne
    @JoinColumn(name = "id_tai_khoan")
    private User user;

    @OneToMany(mappedBy = "studentInfo")
    private List<EventStudent> events;

//    public List<EventStudent> eventById(int eventId) {
//        return this.events.stream().filter(i -> i.getEvent().getId()==eventId).collect(Collectors.toList());
//    }

//    @ManyToMany(mappedBy = "students")
//    private List<Event> events;
}
