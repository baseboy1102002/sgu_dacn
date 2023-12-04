package com.sgu.schedulerApp.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DepartmentDto {

    private int id;
    private String name;
    private String code;
    private List<RoomDto> rooms;
}
