package com.sgu.schedulerApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sv_sk")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventStudent {

    @EmbeddedId
    private EventStudentId eventStudentId;

    @ManyToOne
    @JoinColumn(name = "id_sk")
    @MapsId("eventId")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "id_sv")
    @MapsId("studentId")
    private StudentInfo studentInfo;

    @Column(name = "diem_danh")
    private Boolean checkAttended;

}
