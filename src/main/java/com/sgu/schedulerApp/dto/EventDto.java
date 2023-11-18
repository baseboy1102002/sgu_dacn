package com.sgu.schedulerApp.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventDto {
    private int id;
    private String name;
    private String description;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Integer numOfSeat;
    private String roomCode;
    private String departmentName;
    private String departmentCode;
    private String facultyName;
    private String facultyCode;
    private String classroomName;
    private String classroomCode;

//    for students
    public Boolean attendstatus;

//    for teachers
    public Boolean createStatus;

    public Boolean isOutDated;
}
