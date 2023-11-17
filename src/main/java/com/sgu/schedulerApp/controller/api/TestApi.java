package com.sgu.schedulerApp.controller.api;

import com.sgu.schedulerApp.dto.*;
import com.sgu.schedulerApp.entity.Event;
import com.sgu.schedulerApp.entity.StudentInfo;
import com.sgu.schedulerApp.service.EventService;
import com.sgu.schedulerApp.service.RoomService;
import com.sgu.schedulerApp.service.impl.EventServiceImpl;
import com.sgu.schedulerApp.service.impl.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestApi {

    private final EventService eventService;
    private final RoomService roomService;

//    @GetMapping(value = "/test")
//    public ResponseEntity<Boolean> searchEvent(@RequestBody TestDto json) {
//        Boolean rs =  eventService.checkValidAttend(json.getDate(), json.getEndTime(),
//                json.getStartTime(), json.getStudentId());
//        return new ResponseEntity<>(rs, HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/search")
//    public ResponseEntity<List<EventDto>> search(@RequestBody SearchDto json) {
//        Page<EventDto> events = eventService.searchTest(json.getFilterDto(), json.getKeyword(), 1);
//        return new ResponseEntity<>(events.getContent(), HttpStatus.OK);
//    }

    @GetMapping(value = "/room")
    public ResponseEntity<List<RoomDto>> findRoomByCustom(@RequestParam LocalDate date, @RequestParam LocalTime startTime,
                                                          @RequestParam LocalTime endTime, @RequestParam String departmentCode, @RequestParam int eventId) {
        List<RoomDto> roomDtos = roomService.findRoomNotOccupied(date, startTime, endTime, departmentCode, eventId);
        return new ResponseEntity<>(roomDtos, HttpStatus.OK);
    }

}
