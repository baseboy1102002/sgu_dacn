package com.sgu.schedulerApp.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EventStudentId implements Serializable {

//    @Column(name = "id_sk")
    private int eventId;

//    @Column(name = "id_sv")
    private int studentId;

}
