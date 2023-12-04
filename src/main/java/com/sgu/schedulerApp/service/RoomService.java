package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.RoomDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomService {
    List<RoomDto> findAll();

    RoomDto findById(int id);

    List<RoomDto> findByDepartmentCode(String keyword, String departmentCode);

    List<RoomDto> findRoomNotOccupied(LocalDate date, LocalTime startTime, LocalTime endTime, String departmentCode, int eventId);

    RoomDto saveRoom(RoomDto roomDto);

    void deleteRoom(int id);
}
