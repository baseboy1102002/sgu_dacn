package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.RoomDto;
import com.sgu.schedulerApp.entity.Room;
import com.sgu.schedulerApp.repository.RoomRepository;
import com.sgu.schedulerApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RoomDto> findAll() {
        List<Room> rooms = roomRepository.findByStatusTrue();
        return rooms.stream().map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDto> findRoomNotOccupied(LocalDate date, LocalTime startTime, LocalTime endTime, String departmentCode, int eventId) {
        departmentCode = departmentCode.isBlank()? null:departmentCode;
        List<Room> rooms = roomRepository.findRoomNotOccupied(date, startTime, endTime, departmentCode, eventId);
        return rooms.stream().map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }
}
