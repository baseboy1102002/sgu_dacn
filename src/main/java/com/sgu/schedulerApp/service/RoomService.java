package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.RoomDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomService {
    List<RoomDto> findAll();

    List<RoomDto> findRoomNotOccupied(LocalDate date, LocalTime startTime, LocalTime endTime, String departmentCode, int eventId);
}
