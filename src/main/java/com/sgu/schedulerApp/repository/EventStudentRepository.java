package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.EventStudent;
import com.sgu.schedulerApp.entity.EventStudentId;
import com.sgu.schedulerApp.entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventStudentRepository extends JpaRepository<EventStudent, EventStudentId> {

    long countByEventStudentId_EventId(int eventId);
    EventStudent findByEventStudentId_EventIdAndEventStudentId_StudentId(int eventId, int studentId);

    @Query(value = "select ev from EventStudent ev where ev.event.id =:eventId")
    List<EventStudent> getAllStudentAttendEvent(int eventId);

    @Query(value = "select ev from EventStudent ev where ev.event.id =:eventId and ev.checkAttended=true")
    List<EventStudent> getAllCheckedAttendStudent(int eventId);

    EventStudent findByEventStudentId_EventIdAndStudentInfo_StudentCode(int eventId, String studentCode);

}
