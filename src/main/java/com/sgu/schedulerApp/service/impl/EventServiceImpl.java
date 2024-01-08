package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.config.security.MyUserDetails;
import com.sgu.schedulerApp.dto.*;
import com.sgu.schedulerApp.entity.*;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.repository.*;
import com.sgu.schedulerApp.service.EmailService;
import com.sgu.schedulerApp.service.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final StudentRepository studentRepository;
    private final EventStudentRepository eventStudentRepository;
    private final RoomRepository roomRepository;
    private final FacultyRepository facultyRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

//    @Override
//    public Page<EventDto> findAll(int pagenum) {
//        Pageable pageable = PageRequest.of(pagenum-1,8);
//        Page<Event> events = eventRepository.findAll(pageable);
//        return events.map(event -> modelMapper.map(event, EventDto.class));
//
//    }
//
//    @Override
//    public Page<EventDto> search(FilterDto filterDto, String keyword, int pagenum) {
//        filterDto.setDepartmentCode(filterDto.getDepartmentCode().isBlank() ? null:filterDto.getDepartmentCode());
//        filterDto.setRoomCode(filterDto.getRoomCode().isBlank() ? null:filterDto.getRoomCode());
//        filterDto.setFacultyCode(filterDto.getFacultyCode().isBlank() ? null:filterDto.getFacultyCode());
//        filterDto.setClassCode(filterDto.getClassCode().isBlank() ? null:filterDto.getClassCode());
//
//        Pageable pageable = PageRequest.of(pagenum-1,8);
//        Page<Event> events = eventRepository.search(filterDto, keyword, pageable);
//        return events.map(event -> modelMapper.map(event, EventDto.class));
//    }

    @Override
    public EventDto findById(int id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            Event e = event.get();
            return modelMapper.map(e, EventDto.class);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy sự kiện với id:"+id, id);
    }

    @Override
    public void attendEvent(int eventId) {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
        StudentInfo student = studentRepository.findById(studentId).get();
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            long currentAttendCount = eventStudentRepository.countByEventStudentId_EventId(eventId);
            if (currentAttendCount >= event.getNumOfSeat()) {
                throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không thể tham gia, sự kiện đang tạm thời đầy chỗ.");
            }
            if (!checkValidAttend(event.getDate(), event.getEndTime(), event.getStartTime(), studentId)) {
                EventStudent eventStudent = new EventStudent();
                eventStudent.setEvent(event);
                eventStudent.setStudentInfo(student);
                eventStudent.setCheckAttended(false);
                eventStudent.setEventStudentId(new EventStudentId(eventId, studentId));
                eventStudentRepository.save(eventStudent);
            } else throw new CustomErrorException(HttpStatus.NOT_FOUND,
                    "Không thể tham gia do sự kiện này trùng với lịch trình của bạn.");
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy sự kiện với id:"+eventId);
    }

    @Override
    public void dismissEvent(int eventId) {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
        EventStudent eventStudent = eventStudentRepository.findByEventStudentId_EventIdAndEventStudentId_StudentId(eventId, studentId);
        if (eventStudent != null) {
            eventStudent.getEvent().getStudents().remove(eventStudent);
            eventStudentRepository.delete(eventStudent);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND,
                    "Người dùng với id: "+studentId+" chưa tham gia sự kiện với id:"+eventId+" để có thể hủy tham gia");
    }

    private Boolean checkValidAttend(LocalDate date, LocalTime endTime, LocalTime startTime, int studentId) {
        return eventRepository.existsByDateAndStartTimeLessThanAndEndTimeGreaterThanAndStudents_EventStudentId_StudentId(
                date, endTime, startTime,studentId);
    }

    @Override
    public Page<EventDto> findAllEventWithSearchAndPaging(FilterDto filterDto, String keyword, int pagenum) {
        filterDto.setDepartmentCode(filterDto.getDepartmentCode().isBlank() ? null:filterDto.getDepartmentCode());
        filterDto.setRoomCode(filterDto.getRoomCode().isBlank() ? null:filterDto.getRoomCode());
        filterDto.setFacultyCode(filterDto.getFacultyCode().isBlank() ? null:filterDto.getFacultyCode());
        filterDto.setClassCode(filterDto.getClassCode().isBlank() ? null:filterDto.getClassCode());

        int studentId = 0;
        if (filterDto.getIsOnlyAttainable()) {
            Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
        }
        Pageable pageable = PageRequest.of(pagenum-1,8, Sort.by(Sort.Direction.DESC, "ngay"));
        Page<Event> events = eventRepository.searchTest(filterDto, keyword, studentId, LocalDate.now(), LocalTime.now(), pageable);
        return events.map(event -> modelMapper.map(event, EventDto.class));
    }

    @Override
    @Transactional
    public EventDto saveEvent(EventDto eventDto) {
        boolean isScheduleUpdated = false;
        Optional<Event> optionalEvent = eventRepository.findById(eventDto.getId());
        Event event;
        if (optionalEvent.isPresent()) {
            event = optionalEvent.get();
            if (checkEventTimesHasChange(event, eventDto)) {
                if (checkValidSaveEvent(eventDto)) {
                    throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE,
                            "Không thể lưu sự kiện do thời điểm tổ chức bị trùng với một sự kiện khác trong hệ thống.");
                } else {
                    isScheduleUpdated = true;
                }
            }
        } else {
            if (checkValidSaveEvent(eventDto))
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE,
                        "Không thể lưu sự kiện do thời điểm tổ chức bị trùng với một sự kiện khác trong hệ thống.");
            event = new Event();
            Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            int createdUserId = ((MyUserDetails) principal).getUser().getId();
            event.setUser(userRepository.findById(createdUserId).get());
            event.setStudents(new ArrayList<EventStudent>());
        }
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setStartTime(eventDto.getStartTime());
        event.setEndTime(eventDto.getEndTime());
        event.setDescription(eventDto.getDescription());
        event.setNumOfSeat(eventDto.getNumOfSeat() != null ? eventDto.getNumOfSeat():250);
        Room room = roomRepository.findByCodeAndDepartment_CodeAndStatusTrue(eventDto.getRoomCode(), eventDto.getDepartmentCode());
        if (room != null) {
            event.setRoom(room);
        }
        Faculty faculty = null;
        if (!eventDto.getFacultyCode().isBlank()) {
            faculty = facultyRepository.findByCodeAndStatusTrue(eventDto.getFacultyCode());
        }
        event.setFaculty(faculty);
        Classroom classroom = null;
        if (!eventDto.getClassroomCode().isBlank()) {
            classroom = classroomRepository.findByCodeAndStatusTrue(eventDto.getClassroomCode());
        }
        event.setClassroom(classroom);
        Event savedEvent = eventRepository.save(event);
        EventDto ev = modelMapper.map(savedEvent, EventDto.class);
        ev.setIsUpdateSchedule(isScheduleUpdated);
        return ev;
    }

    private boolean checkValidSaveEvent(EventDto eventDto) {
        return eventRepository.existsByDateAndRoom_CodeAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(eventDto.getDate(),
                eventDto.getRoomCode(), eventDto.getEndTime(), eventDto.getStartTime(), eventDto.getId());
    }

    private boolean checkEventTimesHasChange(Event oldEvent, EventDto newEvent) {
        return !oldEvent.getDate().equals(newEvent.getDate()) || !oldEvent.getStartTime().equals(newEvent.getStartTime()) || !oldEvent.getEndTime().equals(newEvent.getEndTime());
    }

    @Override
    @Async
    public void sendUpdateScheduleEventEmail(int eventId, EventDto oldEvent,EventDto newEvent) {
        List<EventStudent> eventStudents = eventStudentRepository.getAllStudentAttendEvent(eventId);
        if (!eventStudents.isEmpty()) {
            List<String> toEmails = eventStudents.stream().map(ev -> ev.getStudentInfo().getUser().getEmail()).toList();
            List<String> studentNames = eventStudents.stream().map(ev -> ev.getStudentInfo().getUser().getFullName()).toList();
            emailService.sendEmailsWhenUpdateEventSchedule(toEmails, studentNames, oldEvent, newEvent);
        } else return;
    }

    @Override
    public Page<EventDto> getUserEventsPageable(int pagenum, String timeType) {
        Pageable pageable = PageRequest.of(pagenum-1,6, Sort.by(Sort.Direction.DESC, "date"));
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> currentUserRole = ((MyUserDetails) principal).getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        Page<Event> events;
        if (currentUserRole.contains("STUDENT")) {
            int studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
            if (timeType.equals("past")) {
                LocalDate currentDate = LocalDate.now();
                events = eventRepository.getStudentEventsInPast(studentId, currentDate, LocalTime.now(), pageable);
            } else {
                LocalDate currentDate = timeType.equals("all") ? null : LocalDate.now();
                events = eventRepository.getStudentEvents(studentId, currentDate, LocalTime.now(), pageable);
            }
        } else {
            int userId = ((MyUserDetails) principal).getUser().getId();
            if (timeType.equals("past")) {
                LocalDate currentDate = LocalDate.now();
                events = eventRepository.getUserEventsInPast(userId, currentDate, LocalTime.now(), pageable);
            } else {
                LocalDate currentDate = timeType.equals("all") ? null : LocalDate.now();
                events = eventRepository.getUserEvents(userId, currentDate, LocalTime.now(), pageable);
            }
        }
        return events.map(e -> modelMapper.map(e, EventDto.class));
    }

    @Override
    public List<EventDto> getUserEvents(String timeType) {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> currentUserRole = ((MyUserDetails) principal).getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        List<Event> events;
        if (currentUserRole.contains("STUDENT")) {
            int studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
            if (timeType.equals("past")) {
                LocalDate currentDate = LocalDate.now();
                events = eventRepository.getStudentEventsInPast(studentId, currentDate, LocalTime.now());
            } else {
                LocalDate currentDate = timeType.equals("all") ? null : LocalDate.now();
                events = eventRepository.getStudentEvents(studentId, currentDate, LocalTime.now());
            }
        } else {
            int userId = ((MyUserDetails) principal).getUser().getId();
            if (timeType.equals("past")) {
                LocalDate currentDate = LocalDate.now();
                events = eventRepository.getUserEventsInPast(userId, currentDate, LocalTime.now());
            } else {
                LocalDate currentDate = timeType.equals("all") ? null : LocalDate.now();
                events = eventRepository.getUserEvents(userId, currentDate, LocalTime.now());
            }
        }
        return  events.stream().map(e -> modelMapper.map(e, EventDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<StudentInfoDto> getAllStudentAttendEvent(int eventId) {
        List<EventStudent> eventStudents = eventStudentRepository.getAllStudentAttendEvent(eventId);
        return eventStudents.stream().map(e -> modelMapper.map(e, StudentInfoDto.class)).collect(Collectors.toList());
    }

    @Override
    public String getEventName(int id) {
        return eventRepository.findById(id).get().getName();
    }

    @Override
    @Transactional
    public void deleteEvent(int id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            eventRepository.delete(event.get());
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy sự kiện với id:"+id, id);
    }

    @Override
    @Async
    public void sendDeleteEventEmail(int eventId, EventDto eventDto) {
        List<EventStudent> eventStudents = eventStudentRepository.getAllStudentAttendEvent(eventId);
        if (!eventStudents.isEmpty()) {
            List<String> toEmails = eventStudents.stream().map(ev -> ev.getStudentInfo().getUser().getEmail()).toList();
            List<String> studentNames = eventStudents.stream().map(ev -> ev.getStudentInfo().getUser().getFullName()).toList();
            emailService.sendEmailsWhenDeleteEvent(toEmails, studentNames, eventDto);
        } else return;
    }

    @Override
    public List<StudentInfoDto> getAllCheckedAttendStudent(int eventId) {
        List<EventStudent> eventStudents = eventStudentRepository.getAllCheckedAttendStudent(eventId);
        return eventStudents.stream().map(e -> modelMapper.map(e, StudentInfoDto.class)).collect(Collectors.toList());
    }

    @Override
    public HashMap checkAttendEvent(int eventId, String studentCode) {
        HashMap<String, String> result = new HashMap<>();
        result.put("alert", "warning");
        if (!studentRepository.existsByStudentCode(studentCode)) {
            result.put("message", "Mã số sinh viên không đúng, vui lòng thử lại");
            return result;
        }
        EventStudent eventStudent = eventStudentRepository.findByEventStudentId_EventIdAndStudentInfo_StudentCode(eventId, studentCode);
        if (eventStudent != null) {
            if (eventStudent.getCheckAttended()) {
                result.put("message", "Sinh viên đã được điểm danh trước đó");
                return result;
            } else {
                eventStudent.setCheckAttended(true);
                eventStudentRepository.save(eventStudent);
                result.put("message", "Điểm danh thành công!");
                result.put("alert", "success");
                return result;
            }
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Sinh viên chưa tham gia sự kiện này để điểm danh!", studentCode);
    }

    @Override
    @Transactional
    public void resetAttendEvent(int eventId) {
        eventStudentRepository.resetCheckAttendEvent(eventId);
    }

    @Override
    public List<StatisticDto> getStatisticForAdminDashboard(String yearNo, String facultyCode) {
        return eventStudentRepository.getStatisticForAdminDashboard(yearNo, facultyCode, LocalDate.now(), LocalTime.now());
    }

    @Override
    public long countTotalEventHeld(String yearNo, String facultyCode) {
        return eventRepository.countTotalEventHeld(yearNo, facultyCode, LocalDate.now(), LocalTime.now());
    }

    @Override
    public long countTotalEnroll(String yearNo, String facultyCode) {
        return eventRepository.countTotalEnroll(yearNo, facultyCode, LocalDate.now(), LocalTime.now());
    }

    @Override
    public List<FacultyDto> getStatisticFacultyData(String yearNo) {
        return facultyRepository.getStatisticFacultyData(yearNo, LocalDate.now(), LocalTime.now());
    }
}
