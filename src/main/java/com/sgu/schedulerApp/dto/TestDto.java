package com.sgu.schedulerApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TestDto {
    LocalDate date;
    int roomId;
    LocalTime startTime;
    LocalTime endTime;
    int studentId;
}
