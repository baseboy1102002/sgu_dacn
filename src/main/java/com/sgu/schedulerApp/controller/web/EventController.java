package com.sgu.schedulerApp.controller.web;

import com.sgu.schedulerApp.dto.EventDto;
import com.sgu.schedulerApp.dto.FilterDto;
import com.sgu.schedulerApp.dto.SearchDto;
import com.sgu.schedulerApp.dto.StudentInfoDto;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/event")
public class EventController {

    private final EventService eventService;
    private final FacultyService facultyService;
    private final ClassRoomService classRoomService;
    private final DepartmentService departmentService;
    private final RoomService roomService;


    @GetMapping
    public String getAllEvent(@RequestParam("page") Optional<String> page,
                              @ModelAttribute("searchDto") SearchDto searchDto, Model model) {
        int pagenum = Integer.parseInt(page.orElseGet(() -> "1"));
        FilterDto filterDto = searchDto.getFilterDto() == null ?
                new FilterDto("", "", "", "", null, null, false, true)
                : searchDto.getFilterDto();
        searchDto.setFilterDto(filterDto);
        model.addAttribute("searchDto", searchDto);

        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("classrooms", classRoomService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("rooms", roomService.findAll());

        Page<EventDto> eventDtos = eventService.findAllEventWithSearchAndPaging(filterDto, searchDto.getKeyword(), pagenum);
        model.addAttribute("events", eventDtos.getContent());
        model.addAttribute("totalPage", eventDtos.getTotalPages());
        model.addAttribute("totalItem", eventDtos.getTotalElements());
        model.addAttribute("totalCurrentPage", eventDtos.getNumberOfElements());
        model.addAttribute("page", pagenum);
        return "webpage/home";
    }

    @GetMapping(value = {"/", "/{id}", "/my-event/", "/my-event/{id}"})
    public String getEventDetail(@PathVariable(required = false) Optional<Integer> id, Model model, HttpServletRequest request) {
        if (id.isPresent()) {
            int eventId = id.get();
            model.addAttribute("event", eventService.findById(eventId));
            return "webpage/event-detail";
        }
        else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không đủ dữ liệu để xác định sự kiện");
    }

    @GetMapping(value = {"/edit/{id}", "/edit/", "/edit"})
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public String getEventEdit(Model model, @PathVariable Optional<Integer> id, RedirectAttributes redirectAttributes) {
        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("classrooms", classRoomService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        if (id.isPresent()) {
            EventDto eventDto = eventService.findById(id.get());
            if (LocalDateTime.of(eventDto.getDate(), eventDto.getStartTime()).isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("message", "Sự kiện này đã hết hạn, không thể sửa.");
                redirectAttributes.addFlashAttribute("alert", "warning");
                return "redirect:/event";
            }
            model.addAttribute("editMode", "update");
            model.addAttribute("isUpdate", true);
            model.addAttribute("eventDto", eventDto);
        }
        else {
            model.addAttribute("editMode", "create");
            model.addAttribute("isUpdate", false);
            model.addAttribute("eventDto", new EventDto());
        }
        return "webpage/edit-event";
    }

    @GetMapping(value = "/my-event")
    public String getMySchedule(@RequestParam("page") Optional<String> page,
                                @RequestParam("timeType") Optional<String> timeType, Model model) {
        int pagenum = Integer.parseInt(page.orElseGet(() -> "1"));
        String time = timeType.orElseGet(() -> "all");
        Page<EventDto> eventDtos = eventService.getUserEventsPageable(pagenum, time);
        model.addAttribute("eventPageable", eventDtos.getContent().isEmpty()? null:eventDtos.getContent());
        model.addAttribute("events", eventService.getUserEvents(time));
        model.addAttribute("totalPage", eventDtos.getTotalPages());
        model.addAttribute("page", pagenum);
        model.addAttribute("timeType", time);
        return "webpage/my-schedule";
    }

    @GetMapping(value = {"/attend-list/{id}", "/attend-list/", "/attend-list"})
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public String getAttendList(Model model, @PathVariable Optional<Integer> id) {
        if (id.isPresent()) {
            int eventId = id.get();
            model.addAttribute("eventId", eventId);
            List<StudentInfoDto> students = eventService.getAllStudentAttendEvent(eventId);
            model.addAttribute("students", students.isEmpty() ? null:students);
            model.addAttribute("totalStudent", students.size());
            return "webpage/attend-list";
        } else throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không đủ dữ liệu để xác định sự kiện");
    }

    @GetMapping(value = {"/{id}/students/export-excel", "/students/export-excel"})
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public void exportToExcel(HttpServletResponse response , @PathVariable Optional<Integer> id) throws IOException {
        List<StudentInfoDto> studentInfoDtos = null;
        String eventName = null;
        if (id.isPresent()) {
            int eventId = id.get();
            studentInfoDtos = eventService.getAllStudentAttendEvent(eventId);
            eventName = eventService.getEventName(eventId);
        } else throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không đủ dữ liệu để xác định sự kiện");

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Danhsachthamdu_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        EventStudentsExcelExporter excelExporter = new EventStudentsExcelExporter(studentInfoDtos, eventName);
        excelExporter.exportExcelFile(response);
    }

    @GetMapping(value = {"/check-attend/{id}", "/check-attend/", "/check-attend"})
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public String getCheckAttendance(Model model, @PathVariable Optional<Integer> id) {
        if (id.isPresent()) {
            int eventId = id.get();
            model.addAttribute("eventId", eventId);
            List<StudentInfoDto> studentInfoDtos = eventService.getAllCheckedAttendStudent(eventId);
            model.addAttribute("students", !studentInfoDtos.isEmpty() ? studentInfoDtos:null);
            return "webpage/attend-scanner";
        } else throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không đủ dữ liệu để xác định sự kiện");
    }

    @PostMapping(value = {"/attend/{id}", "/attend/", "/attend"})
    @PreAuthorize("hasAuthority('STUDENT')")
    public String attendEvent(@PathVariable Optional<Integer> id, RedirectAttributes redirectAttributes) {
        if (id.isPresent()) {
            int eventId = id.get();
            EventDto e = eventService.findById(eventId);
            if (LocalDateTime.of(e.getDate(), e.getStartTime()).isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("message", "Sự kiện này đã hết hạn, không thể thao tác.");
                redirectAttributes.addFlashAttribute("alert", "warning");
                return "redirect:/event";
            }
            try {
                eventService.attendEvent(eventId);
                redirectAttributes.addFlashAttribute("alert", "success");
                redirectAttributes.addFlashAttribute("message", "Đăng ký tham gia sự kiện thành công!");
            } catch (CustomErrorException ex) {
                redirectAttributes.addFlashAttribute("alert", "danger");
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
            }
            return "redirect:/event";
        } else
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không đủ dữ liệu để xác định sự kiện");
    }

    @PostMapping(value = {"/dismiss/{id}", "/dismiss/", "/dismiss"})
    @PreAuthorize("hasAuthority('STUDENT')")
    public String dismissEvent(@PathVariable Optional<Integer> id,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        if (id.isPresent()) {
            int eventId = id.get();
            EventDto e = eventService.findById(eventId);
            if (LocalDateTime.of(e.getDate(), e.getStartTime()).isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("message", "Sự kiện này đã hết hạn, không thể thao tác.");
                redirectAttributes.addFlashAttribute("alert", "warning");
                return "redirect:/event";
            }
            eventService.dismissEvent(eventId);
            redirectAttributes.addFlashAttribute("alert", "success");
            redirectAttributes.addFlashAttribute("message", "Đã rút khỏi sự kiện.");
            String reqURI = request.getHeader("Referer");
            return "redirect:"+reqURI;
        } else
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không đủ dữ liệu để xác định sự kiện");
    }

    @PostMapping(value = "/edit")
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public String saveEvent(@ModelAttribute("eventDto") EventDto eventDto, RedirectAttributes redirectAttributes) {
        try {
            EventDto oldEvent = eventDto.getId()!=0 ? eventService.findById(eventDto.getId()):null;
            EventDto savedEvent = eventService.saveEvent(eventDto);
            int savedEventId = savedEvent.getId();
            if (savedEvent.getIsUpdateSchedule()) {
                eventService.sendUpdateScheduleEventEmail(savedEventId, oldEvent, savedEvent);
            }
            redirectAttributes.addFlashAttribute("message", "Lưu sự kiện thành công!");
            redirectAttributes.addFlashAttribute("alert", "success");
            return "redirect:/event/"+savedEventId;
        } catch (CustomErrorException e) {
            redirectAttributes.addFlashAttribute("message", "Không thể lưu sự kiện do bị trùng lịch.");
            redirectAttributes.addFlashAttribute("alert", "error");
            return "redirect:/event/edit";
        }
    }

    @PostMapping(value = {"/delete/{id}", "/delete/", "/delete"})
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public String deleteEvent(@PathVariable Optional<Integer> id,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request) {
        if (id.isPresent()) {
            int eventId = id.get();
            EventDto eventDto = eventService.findById(eventId);
            eventService.deleteEvent(eventId);
            eventService.sendDeleteEventEmail(eventId, eventDto);
            redirectAttributes.addFlashAttribute("alert", "success");
            redirectAttributes.addFlashAttribute("message", "Đã hủy bỏ sự kiện.");
            String reqURI = request.getHeader("Referer");
            if (!reqURI.contains("my-event")) {
                return "redirect:/event";
            } else return "redirect:/event/my-event";
        } else throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không đủ dữ liệu để xác định sự kiện");
    }

    @PostMapping(value = {"/check-attend/{id}", "/check-attend/", "/check-attend"})
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public String checkAttendance(@PathVariable Optional<Integer> id, @RequestParam String studentCode, RedirectAttributes redirectAttributes) {
        if (id.isPresent()) {
            int eventId = id.get();
            try {
                HashMap result = eventService.checkAttendEvent(eventId, studentCode);
                redirectAttributes.addFlashAttribute("alert", result.get("alert"));
                redirectAttributes.addFlashAttribute("message", result.get("message"));
            } catch (CustomErrorException  e) {
                redirectAttributes.addFlashAttribute("alert", "error");
                redirectAttributes.addFlashAttribute("message", e.getMessage());
            }
            return "redirect:/event/check-attend/"+eventId;
        } else throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không đủ dữ liệu để xác định sự kiện");
    }

    @PostMapping(value = "/reset-attend")
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public String resetAttendEvent(@RequestParam String eventId, RedirectAttributes redirectAttributes) {
        eventService.resetAttendEvent(Integer.parseInt(eventId));
        redirectAttributes.addFlashAttribute("alert", "success");
        redirectAttributes.addFlashAttribute("message", "Làm mới lại danh sách điểm danh thành công!");
        return "redirect:/event/check-attend/"+eventId;
    }

}
