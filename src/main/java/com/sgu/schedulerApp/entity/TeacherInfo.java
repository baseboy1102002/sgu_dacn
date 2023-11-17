package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "giang_vien")
@NoArgsConstructor
@Getter
@Setter
public class TeacherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ma_gv")
    private String teacherCode;

    @OneToOne
    @JoinColumn(name = "id_tai_khoan")
    private User user;
}
