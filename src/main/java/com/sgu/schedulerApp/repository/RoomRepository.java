package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByStatusTrue();

    @Query(value = "select r from Room r where (:roomCode is null or r.code like %:roomCode%) and r.department.code=:departmentCode and r.status=true")
    List<Room> findByKeywordAndDepartmentCode(String roomCode, String departmentCode);

    Room findByCodeAndDepartment_CodeAndStatusTrue(String roomCode, String departmentCode);

    @Query("select r from Room r where not exists" +
            " (select e.room from Event e where e.id !=:eventId and e.date =:date and e.startTime <:endTime and e.endTime >:startTime and r.id=e.room.id)" +
            " and (:departmentCode is null or r.department.code=:departmentCode) and r.status=true ")
    List<Room> findRoomNotOccupied(LocalDate date, LocalTime startTime, LocalTime endTime, String departmentCode, int eventId);

    boolean existsByCodeAndDepartment_CodeAndStatusTrue(String roomCode, String departmentCode); // when add or update room

    boolean existsByIdAndEvents_DateGreaterThanOrEvents_DateEqualsAndEvents_StartTimeGreaterThanEqual(int roomId, LocalDate currentDay1, LocalDate currentDay2, LocalTime currentTime); // when delete room

}