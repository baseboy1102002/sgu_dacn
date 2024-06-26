package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.dto.StatisticDto;
import com.sgu.schedulerApp.entity.EventStudent;
import com.sgu.schedulerApp.entity.EventStudentId;
import com.sgu.schedulerApp.entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface EventStudentRepository extends JpaRepository<EventStudent, EventStudentId> {

    long countByEventStudentId_EventId(int eventId);

    EventStudent findByEventStudentId_EventIdAndEventStudentId_StudentId(int eventId, int studentId);

    @Query(value = "select ev from EventStudent ev where ev.event.id =:eventId")
    List<EventStudent> getAllStudentAttendEvent(int eventId);

    @Query(value = "select ev from EventStudent ev where ev.event.id =:eventId and ev.checkAttended=true")
    List<EventStudent> getAllCheckedAttendStudent(int eventId);

    EventStudent findByEventStudentId_EventIdAndStudentInfo_StudentCode(int eventId, String studentCode);

    @Modifying
    @Query("update EventStudent ev set ev.checkAttended=false where ev.event.id=:eventId")
    void resetCheckAttendEvent(int eventId);

    @Query(name = "statistic_query", nativeQuery = true)
    List<StatisticDto> getStatisticForAdminDashboard(String yearNo, String facultyCode, LocalDate currentDate, LocalTime currentTime);

}
