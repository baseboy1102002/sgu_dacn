package com.sgu.schedulerApp.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StudentInfoDto {

    private String studentInfoStudentCode;
    private String studentInfoUserFullName;
    private Boolean checkAttended;

}
