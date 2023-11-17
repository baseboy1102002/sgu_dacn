package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.EventDto;
import com.sgu.schedulerApp.dto.FilterDto;
import com.sgu.schedulerApp.dto.StudentInfoDto;
import com.sgu.schedulerApp.entity.Event;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public interface EventService {
    Page<EventDto> findAll(int pagenum);

    Page<EventDto> search(FilterDto filterDto, String keyword, int pagenum);

    EventDto findById(int id);

    Boolean attendEvent(int eventId);

    void dismissEvent(int eventId);

    Boolean checkValidAttend(LocalDate date, LocalTime endTime, LocalTime startTime, int studentId);

    Page<EventDto> searchTest(FilterDto filterDto, String keyword, int pagenum);

    EventDto saveEvent(EventDto eventDto);

    List<StudentInfoDto> getAllStudentAttendEvent(int eventId);

    String getEventName(int id);

    void deleteEvent(int id);

    public List<StudentInfoDto> getAllCheckedAttendStudent(int eventId);

    public HashMap checkAttendEvent(int eventId, String studentCode);

}