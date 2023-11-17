package com.sgu.schedulerApp.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClassDto {
    private int id;
    private String name;
    private String code;
    private String facultyCode;
    private String facultyName;
}
