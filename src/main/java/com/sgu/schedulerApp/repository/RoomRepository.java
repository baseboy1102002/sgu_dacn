package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByStatusTrue();

    @Query(value = "select r from Room r where r.code=:roomCode and (:departmentCode is null or r.department.code=:departmentCode)")
    Room findByCodeAndDepartment_Code(String roomCode, String departmentCode);

    @Query("select r from Room r where not exists" +
            " (select e.room from Event e where e.id !=:eventId and e.date =:date and e.startTime <:endTime and e.endTime >:startTime and r.id=e.room.id)" +
            " and (:departmentCode is null or r.department.code=:departmentCode)")
    List<Room> findRoomNotOccupied(LocalDate date, LocalTime startTime, LocalTime endTime, String departmentCode, int eventId);
}