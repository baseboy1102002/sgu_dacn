package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "tai_khoan")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ten_dn")
    private String username;

    @Column(name = "mat_khau")
    private String password;

    @Column(name = "ho_ten")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_vai_tro")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Event> events;


    @OneToOne(mappedBy = "user")
    private StudentInfo studentInfo;

    @OneToOne(mappedBy = "user")
    private TeacherInfo teacherInfo;

}
