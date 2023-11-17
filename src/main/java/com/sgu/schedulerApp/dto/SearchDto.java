package com.sgu.schedulerApp.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SearchDto {
    private String keyword;
    private FilterDto filterDto;
}
