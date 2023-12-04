package com.sgu.schedulerApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatisticDto {
    private int monthNo;
    private int totalEvent;
    private int totalEnroll;
}
