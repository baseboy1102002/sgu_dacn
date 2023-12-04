package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.*;
import com.sgu.schedulerApp.entity.Event;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public interface EventService {
//    Page<EventDto> findAll(int pagenum);
//
//    Page<EventDto> search(FilterDto filterDto, String keyword, int pagenum);

    EventDto findById(int id);

    void attendEvent(int eventId);

    void dismissEvent(int eventId);

//    Boolean checkValidAttend(LocalDate date, LocalTime endTime, LocalTime startTime, int studentId);

    Page<EventDto> findAllEventWithSearchAndPaging(FilterDto filterDto, String keyword, int pagenum);

    EventDto saveEvent(EventDto eventDto);

    void sendUpdateScheduleEventEmail(int eventId, EventDto oldEvent,EventDto newEvent);

    List<EventDto> getUserEvents(String timeType);

    Page<EventDto> getUserEventsPageable(int pagenum, String timeType);

    List<StudentInfoDto> getAllStudentAttendEvent(int eventId);

    String getEventName(int id);

    void deleteEvent(int id);

    void sendDeleteEventEmail(int eventId, EventDto eventDto);

    List<StudentInfoDto> getAllCheckedAttendStudent(int eventId);

    HashMap checkAttendEvent(int eventId, String studentCode);

    void resetAttendEvent(int eventId);

    List<StatisticDto> getStatisticForAdminDashboard(String yearNo, String facultyCode);

    long countTotalEventHeld(String yearNo, String facultyCode);

    long countTotalEnroll(String yearNo, String facultyCode);

    List<FacultyDto> getStatisticFacultyData(String yearNo);
}
