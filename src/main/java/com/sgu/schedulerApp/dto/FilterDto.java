package com.sgu.schedulerApp.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FilterDto {
    private String facultyCode;
    private String classCode;
    private String departmentCode;
    private String roomCode;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate startTime;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate endTime;

//  for student's view
    private Boolean isOnlyAttainable;
    private Boolean isOnlyNonExpired;
}
