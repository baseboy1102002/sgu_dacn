package com.sgu.schedulerApp.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FacultyDto {

    private int id;
    private String name;
    private String code;
    List<ClassDto> classes;
    private int totalEventHeld;

    public FacultyDto(String name, String code, int totalEventHeld) {
        this.name = name;
        this.code = code;
        this.totalEventHeld = totalEventHeld;
    }
}
