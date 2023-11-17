package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.config.security.MyUserDetails;
import com.sgu.schedulerApp.dto.EventDto;
import com.sgu.schedulerApp.dto.FilterDto;
import com.sgu.schedulerApp.dto.StudentInfoDto;
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
import org.springframework.http.HttpStatus;
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

    @Override
    public Page<EventDto> findAll(int pagenum) {
        Pageable pageable = PageRequest.of(pagenum-1,8);
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(event -> modelMapper.map(event, EventDto.class));

    }

    @Override
    public Page<EventDto> search(FilterDto filterDto, String keyword, int pagenum) {
        filterDto.setDepartmentCode(filterDto.getDepartmentCode().isBlank() ? null:filterDto.getDepartmentCode());
        filterDto.setRoomCode(filterDto.getRoomCode().isBlank() ? null:filterDto.getRoomCode());
        filterDto.setFacultyCode(filterDto.getFacultyCode().isBlank() ? null:filterDto.getFacultyCode());
        filterDto.setClassCode(filterDto.getClassCode().isBlank() ? null:filterDto.getClassCode());

        Pageable pageable = PageRequest.of(pagenum-1,8);
        Page<Event> events = eventRepository.search(filterDto, keyword, pageable);
        return events.map(event -> modelMapper.map(event, EventDto.class));
    }

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
    public Boolean attendEvent(int eventId) {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
        StudentInfo student = studentRepository.findById(studentId).get();
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            long currentAttendCount = eventStudentRepository.countByEventStudentId_EventId(eventId);
            if (currentAttendCount >= event.getNumOfSeat()) {
                return false;
            }
            if (!checkValidAttend(event.getDate(), event.getEndTime(), event.getStartTime(), studentId)) {
                EventStudent eventStudent = new EventStudent();
                eventStudent.setEvent(event);
                eventStudent.setStudentInfo(student);
                eventStudent.setCheckAttended(false);
                eventStudent.setEventStudentId(new EventStudentId(eventId, studentId));
                eventStudentRepository.save(eventStudent);
                return true;
            } else return false;
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy sự kiện với id:"+eventId, eventId);
    }

    @Override
    public void dismissEvent(int eventId) {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
        EventStudent eventStudent = eventStudentRepository.findByEventStudentId_EventIdAndEventStudentId_StudentId(eventId, studentId);
        if (eventStudent != null) {
            eventStudentRepository.delete(eventStudent);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND,
                    "Người dùng với id: "+studentId+" chưa tham gia sự kiện với id:"+eventId+" để có thể hủy tham gia",
                    eventId);
    }

    @Override
    public Boolean checkValidAttend(LocalDate date, LocalTime endTime, LocalTime startTime, int studentId) {
        return eventRepository.existsByDateAndStartTimeLessThanAndEndTimeGreaterThanAndStudents_EventStudentId_StudentId(
                date, endTime, startTime,studentId);
    }

    @Override
    public Page<EventDto> searchTest(FilterDto filterDto, String keyword, int pagenum) {
        filterDto.setDepartmentCode(filterDto.getDepartmentCode().isBlank() ? null:filterDto.getDepartmentCode());
        filterDto.setRoomCode(filterDto.getRoomCode().isBlank() ? null:filterDto.getRoomCode());
        filterDto.setFacultyCode(filterDto.getFacultyCode().isBlank() ? null:filterDto.getFacultyCode());
        filterDto.setClassCode(filterDto.getClassCode().isBlank() ? null:filterDto.getClassCode());

        int studentId = 0;
        if (filterDto.getIsOnlyAttainable()) {
            Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
        }

        Pageable pageable = PageRequest.of(pagenum-1,8);
        Page<Event> events = eventRepository.searchTest(filterDto, keyword, studentId, pageable);
        return events.map(event -> modelMapper.map(event, EventDto.class));
    }

    @Override
    @Transactional
    public EventDto saveEvent(EventDto eventDto) {
        Optional<Event> optionalEvent = eventRepository.findById(eventDto.getId());
        Event event;
        if (optionalEvent.isPresent()) {
            event = optionalEvent.get();
            if (!event.getDate().equals(eventDto.getDate()) || !event.getStartTime().equals(eventDto.getStartTime()) || !event.getEndTime().equals(eventDto.getEndTime())) {
                if (checkValidCreateEvent(eventDto.getDate(), eventDto.getRoomCode(), eventDto.getEndTime(), eventDto.getStartTime(), eventDto.getId()))
                    return null;
            }
        } else {
            if (checkValidCreateEvent(eventDto.getDate(), eventDto.getRoomCode(), eventDto.getEndTime(), eventDto.getStartTime(), eventDto.getId()))
                return null;
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
        Room room = roomRepository.findByCodeAndDepartment_Code(eventDto.getRoomCode(), eventDto.getDepartmentCode().isBlank()? null:eventDto.getDepartmentCode());
        if (room != null) {
            event.setRoom(room);
        }
        Faculty faculty = null;
        if (!eventDto.getFacultyCode().isBlank()) {
            faculty = facultyRepository.findByCode(eventDto.getFacultyCode());
        }
        event.setFaculty(faculty);
        Classroom classroom = null;
        if (!eventDto.getClassroomCode().isBlank()) {
            classroom = classroomRepository.findByCode(eventDto.getClassroomCode());
        }
        event.setClassroom(classroom);
        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventDto.class);
    }

    public boolean checkValidCreateEvent(LocalDate date, String roomCode, LocalTime endTime, LocalTime startTime, int eventId) {
        return eventRepository.existsByDateAndRoom_CodeAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(date,
                roomCode, endTime, startTime, eventId);
    }

    public Page<EventDto> getUserEventsPageable(int pagenum, String timeType) {
        Pageable pageable = PageRequest.of(pagenum-1,8);
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> currentUserRole = ((MyUserDetails) principal).getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        Page<Event> events;
        if (currentUserRole.contains("STUDENT")) {
            int studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
            if (timeType.equals("past")) {
                LocalDate currentDate = LocalDate.now();
                events = eventRepository.getStudentEventsInPast(studentId, currentDate, pageable);
            } else {
                LocalDate currentDate = timeType.equals("all") ? null : LocalDate.now();
                events = eventRepository.getStudentEvents(studentId, currentDate, pageable);
            }
        } else {
            int userId = ((MyUserDetails) principal).getUser().getId();
            if (timeType.equals("past")) {
                LocalDate currentDate = LocalDate.now();
                events = eventRepository.getUserEventsInPast(userId, currentDate, pageable);
            } else {
                LocalDate currentDate = timeType.equals("all") ? null : LocalDate.now();
                events = eventRepository.getUserEvents(userId, currentDate, pageable);
            }
        }
        return events.map(e -> modelMapper.map(e, EventDto.class));
    }

    public List<EventDto> getUserEvents(String timeType) {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> currentUserRole = ((MyUserDetails) principal).getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        List<Event> events;
        if (currentUserRole.contains("STUDENT")) {
            int studentId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
            if (timeType.equals("past")) {
                LocalDate currentDate = LocalDate.now();
                events = eventRepository.getStudentEventsInPast(studentId, currentDate);
            } else {
                LocalDate currentDate = timeType.equals("all") ? null : LocalDate.now();
                events = eventRepository.getStudentEvents(studentId, currentDate);
            }
        } else {
            int userId = ((MyUserDetails) principal).getUser().getId();
            if (timeType.equals("past")) {
                LocalDate currentDate = LocalDate.now();
                events = eventRepository.getUserEventsInPast(userId, currentDate);
            } else {
                LocalDate currentDate = timeType.equals("all") ? null : LocalDate.now();
                events = eventRepository.getUserEvents(userId, currentDate);
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
            EventDto eventDto = modelMapper.map(event, EventDto.class);
            List<EventStudent> eventStudents = eventStudentRepository.getAllStudentAttendEvent(id);
            eventRepository.deleteById(id);
            if (!eventStudents.isEmpty()) {
                List<String> toEmails = eventStudents.stream().map(ev -> ev.getStudentInfo().getEmail()).toList();
                List<String> studentNames = eventStudents.stream().map(ev -> ev.getStudentInfo().getUser().getFullName()).toList();
                emailService.sendBulkEmails(toEmails, studentNames, eventDto);
            }
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy sự kiện với id:"+id, id);
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
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy sự kiện với id:"+eventId, eventId);
    }
}
