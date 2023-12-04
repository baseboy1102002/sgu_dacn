package com.sgu.schedulerApp.controller.api;

import com.sgu.schedulerApp.dto.*;
import com.sgu.schedulerApp.service.EventService;
import com.sgu.schedulerApp.service.FacultyService;
import com.sgu.schedulerApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppAPI {

    private final EventService eventService;
    private final RoomService roomService;
    private final FacultyService facultyService;


    @GetMapping(value = "/room")
    public ResponseEntity<List<RoomDto>> findRoomByCustom(@RequestParam LocalDate date, @RequestParam LocalTime startTime,
                                                          @RequestParam LocalTime endTime, @RequestParam String departmentCode, @RequestParam int eventId) {
        List<RoomDto> roomDtos = roomService.findRoomNotOccupied(date, startTime, endTime, departmentCode, eventId);
        return new ResponseEntity<>(roomDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/statistic")
    public ResponseEntity<Object> getStatisticData(@RequestParam String yearNo, @RequestParam String facultyCode, @RequestParam String facultyId) {
        long totalEventHeld = eventService.countTotalEventHeld(yearNo, facultyCode.isBlank() ? null:facultyCode);
        long totalEnroll = eventService.countTotalEnroll(yearNo, facultyCode.isBlank() ? null:facultyCode);
        List<StatisticDto> statisticData = eventService.getStatisticForAdminDashboard(yearNo, facultyId.isBlank() ? null:facultyId);
        List<FacultyDto> statisticFacultyData = eventService.getStatisticFacultyData(yearNo);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "totalEventHeld", totalEventHeld,
                "totalEnroll", totalEnroll,
                "statisticData", statisticData,
                "statisticFacultyData", statisticFacultyData
        ));
    }
}
