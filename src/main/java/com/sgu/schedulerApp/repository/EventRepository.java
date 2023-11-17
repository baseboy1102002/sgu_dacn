package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.dto.FilterDto;
import com.sgu.schedulerApp.entity.Event;
import com.sgu.schedulerApp.entity.StudentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("select e from Event e left join e.faculty left join e.classroom where (:keyword is null or concat(e.name, e.description) like %:keyword%) and" +
            " (:#{#filter.facultyCode} is null or e.faculty.code=:#{#filter.facultyCode}) and" +
            " (:#{#filter.classCode} is null or e.classroom.code=:#{#filter.classCode}) and" +
            " (:#{#filter.departmentCode} is null or e.room.department.code=:#{#filter.departmentCode}) and" +
            " (:#{#filter.roomCode} is null or e.room.code=:#{#filter.roomCode}) and" +
            " ((:#{#filter.startTime} is null and :#{#filter.endTime} is null) or e.date between :#{#filter.startTime} and :#{#filter.endTime})")
    Page<Event> search(@Param("filter")FilterDto filterDto, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select rs.id, rs.ten_su_kien, rs.mo_ta, rs.ngay, rs.sl_cho, rs.id_nguoi_tao, rs.id_lop, rs.id_khoa, rs.bat_dau, rs.ket_thuc, rs.id_phong" +
            " from (select sk.id, sk.ten_su_kien, sk.mo_ta, sk.ngay, sk.sl_cho, sk.id_nguoi_tao, sk.id_lop, sk.id_khoa, sk.bat_dau, sk.ket_thuc, sk.id_phong" +
            " from su_kien as sk left join lop as l on l.id = sk.id_lop left join khoa as k on k.id = sk.id_khoa" +
            " join phong_hoc as ph on ph.id = sk.id_phong join co_so as cs on cs.id = ph.id_co_so" +
            " where (:keyword is null or concat(sk.ten_su_kien, sk.mo_ta) like %:keyword% escape '')" +
            " and (:#{#filter.facultyCode} is null or k.ma_khoa=:#{#filter.facultyCode})" +
            " and (:#{#filter.classCode} is null or l.ma_lop=:#{#filter.classCode})" +
            " and (:#{#filter.departmentCode} is null or cs.ma_cs=:#{#filter.departmentCode})" +
            " and (:#{#filter.roomCode} is null or ph.ma_phong=:#{#filter.roomCode})" +
            " and ((:#{#filter.startTime} is null and :#{#filter.endTime} is null) or sk.ngay between :#{#filter.startTime} and :#{#filter.endTime})) rs" +
            " where not exists (select 1 from (select * from sv_sk svsk left join su_kien sk on sk.id=svsk.id_sk and svsk.id_sv=:studentId) tb" +
            " where tb.bat_dau<rs.ket_thuc and tb.ket_thuc>rs.bat_dau and tb.ngay=rs.ngay and rs.id != tb.id)",
            countQuery = "select count(1) from (select rs.id, rs.ten_su_kien, rs.mo_ta, rs.ngay, rs.sl_cho, rs.id_nguoi_tao, rs.id_lop, rs.id_khoa, rs.bat_dau, rs.ket_thuc, rs.id_phong" +
                    " from (select sk.id, sk.ten_su_kien, sk.mo_ta, sk.ngay, sk.sl_cho, sk.id_nguoi_tao, sk.id_lop, sk.id_khoa, sk.bat_dau, sk.ket_thuc, sk.id_phong" +
                    " from su_kien as sk left join lop as l on l.id = sk.id_lop left join khoa as k on k.id = sk.id_khoa" +
                    " join phong_hoc as ph on ph.id = sk.id_phong join co_so as cs on cs.id = ph.id_co_so" +
                    " where (:keyword is null or concat(sk.ten_su_kien, sk.mo_ta) like %:keyword% escape '')" +
                    " and (:#{#filter.facultyCode} is null or k.ma_khoa=:#{#filter.facultyCode})" +
                    " and (:#{#filter.classCode} is null or l.ma_lop=:#{#filter.classCode})" +
                    " and (:#{#filter.departmentCode} is null or cs.ma_cs=:#{#filter.departmentCode})" +
                    " and (:#{#filter.roomCode} is null or ph.ma_phong=:#{#filter.roomCode})" +
                    " and ((:#{#filter.startTime} is null and :#{#filter.endTime} is null) or sk.ngay between :#{#filter.startTime} and :#{#filter.endTime})) rs" +
                    " where not exists (select 1 from (select * from sv_sk svsk left join su_kien sk on sk.id=svsk.id_sk and svsk.id_sv=:studentId) tb" +
                    " where tb.bat_dau<rs.ket_thuc and tb.ket_thuc>rs.bat_dau and tb.ngay=rs.ngay and rs.id != tb.id)) c",
            nativeQuery = true)
    Page<Event> searchTest(@Param("filter")FilterDto filterDto, @Param("keyword") String keyword, int studentId, Pageable pageable);

    Boolean existsByDateAndStartTimeLessThanAndEndTimeGreaterThanAndStudents_EventStudentId_StudentId(
            LocalDate date, LocalTime endTime, LocalTime startTime, int studentId); // for students

    Boolean existsByDateAndRoom_CodeAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
            LocalDate date, String roomCode, LocalTime endTime, LocalTime startTime, int eventId); // for teachers and admin

    @Query(value = "select e from Event e where e.user.id=:userId and (:currentDate is null or e.date>=:currentDate)")
    Page<Event> getUserEvents(int userId, LocalDate currentDate, Pageable pageable);  // for teachers and admins

    @Query(value = "select e from Event e where e.user.id=:userId and e.date<:currentDate")
    Page<Event> getUserEventsInPast(int userId, LocalDate currentDate, Pageable pageable);  // for teachers and admins

    @Query(value = "select e from Event e where e.user.id=:userId and (:currentDate is null or e.date>=:currentDate)")
    List<Event> getUserEvents(int userId, LocalDate currentDate); // for teachers and admins

    @Query(value = "select e from Event e where e.user.id=:userId and e.date<:currentDate")
    List<Event> getUserEventsInPast(int userId, LocalDate currentDate); // for teachers and admins

    @Query(value = "select e from Event e join e.students st where st.studentInfo.id=:studentId and (:currentDate is null or e.date>=:currentDate)")
    Page<Event> getStudentEvents(int studentId, LocalDate currentDate, Pageable pageable);  //for students

    @Query(value = "select e from Event e join e.students st where st.studentInfo.id=:studentId and e.date<:currentDate")
    Page<Event> getStudentEventsInPast(int studentId, LocalDate currentDate, Pageable pageable);  //for students

    @Query(value = "select e from Event e join e.students st where st.studentInfo.id=:studentId and (:currentDate is null or e.date>=:currentDate)")
    List<Event> getStudentEvents(int studentId, LocalDate currentDate);  //for students

    @Query(value = "select e from Event e join e.students st where st.studentInfo.id=:studentId and e.date<:currentDate")
    List<Event> getStudentEventsInPast(int studentId, LocalDate currentDate);  //for students

}
