package com.sgu.schedulerApp.controller.admin;

import com.sgu.schedulerApp.dto.DepartmentDto;
import com.sgu.schedulerApp.dto.RoomDto;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.service.DepartmentService;
import com.sgu.schedulerApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/departments-and-rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final DepartmentService departmentService;
    @GetMapping
    public String getRoomView(Model model, @RequestParam("departmentCode") Optional<String> departmentCode,
                               @RequestParam("keyword") Optional<String> roomCode) {
        List<DepartmentDto> departmentDtos = departmentService.findAllWithCodeOptional(departmentCode.orElseGet(()->""));
        departmentDtos.forEach(d -> {
            List<RoomDto> roomDtos = roomService.findByDepartmentCode(roomCode.orElseGet(()->""), d.getCode());
            d.setRooms(roomDtos);
        });
        model.addAttribute("departments", departmentDtos);
        model.addAttribute("departmentSelect", departmentService.findAll());
        return "adminpage/room";
    }

    @GetMapping(value = {"/edit-room/{id}", "/edit-room/", "/edit-room"})
    public String getEditRoomView(Model model, @PathVariable Optional<Integer> id,
                                  @RequestParam(value = "departmentCode", required = false) String departmentCode) {
        if (id.isPresent()) {
            model.addAttribute("isUpdate", true);
            if (!model.containsAttribute("roomDto"))
                model.addAttribute("roomDto", roomService.findById(id.get()));
        } else {
            model.addAttribute("isUpdate", false);
            if (!model.containsAttribute("roomDto"))
                model.addAttribute("roomDto", new RoomDto());
        }
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("departmentCode", departmentCode != null ? departmentCode:"");
        return "adminpage/edit-room";
    }

    @GetMapping(value = {"/edit-department/{id}", "/edit-department/", "/edit-department"})
    public String getEditDepartmentView(Model model, @PathVariable Optional<Integer> id) {
        if (id.isPresent()) {
            model.addAttribute("isUpdate", true);
            if (!model.containsAttribute("departmentDto"))
                model.addAttribute("departmentDto", departmentService.findById(id.get()));
        } else {
            model.addAttribute("isUpdate", false);
            if (!model.containsAttribute("departmentDto"))
                model.addAttribute("departmentDto", new DepartmentDto());
        }
        return "adminpage/edit-department";
    }

    @PostMapping(value = "/edit-room")
    public String saveRoom(@ModelAttribute("roomDto") RoomDto roomDto, RedirectAttributes redirectAttributes) {
        try {
            RoomDto savedRoom = roomService.saveRoom(roomDto);
            int savedRoomId = savedRoom.getId();
            redirectAttributes.addFlashAttribute("alert", "success");
            redirectAttributes.addFlashAttribute("message", "Lưu phòng học thành công!");
            return  "redirect:/admin/departments-and-rooms/edit-room/"+savedRoomId;
        } catch (CustomErrorException e) {
            redirectAttributes.addFlashAttribute("alert", "danger");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("roomDto", roomDto);
            return "redirect:/admin/departments-and-rooms/edit-room" + (roomDto.getId()!=0 ? "/"+roomDto.getId() : "");
        }
    }

    @PostMapping(value = {"/delete-room/{id}", "/delete-room/", "/delete-room"})
    private String deleteRoom(@PathVariable Optional<Integer> id, RedirectAttributes redirectAttributes) {
        if (id.isPresent()) {
            try {
                roomService.deleteRoom(id.get());
                redirectAttributes.addFlashAttribute("alert", "success");
                redirectAttributes.addFlashAttribute("message", "Xóa bỏ phòng thành công!");
            } catch (CustomErrorException e) {
                redirectAttributes.addFlashAttribute("alert", "danger");
                redirectAttributes.addFlashAttribute("message", e.getMessage());
            }
        } else
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không thể thực hiện yêu cầu do không đủ dữ liệu để xác định");
        return "redirect:/admin/departments-and-rooms";
    }

    @PostMapping(value = "/edit-department")
    public String saveDepartment(@ModelAttribute("departmentDto") DepartmentDto departmentDto, RedirectAttributes redirectAttributes) {
        try {
            DepartmentDto savedDepartment = departmentService.saveDepartment(departmentDto);
            int departmentId = savedDepartment.getId();
            redirectAttributes.addFlashAttribute("alert", "success");
            redirectAttributes.addFlashAttribute("message", "Lưu cơ sở thành công!");
            return  "redirect:/admin/departments-and-rooms/edit-department/"+departmentId;
        } catch (CustomErrorException ex) {
            redirectAttributes.addFlashAttribute("alert", "danger");
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            redirectAttributes.addFlashAttribute("roomDto", departmentDto);
            return "redirect:/admin/departments-and-rooms/edit-department" + (departmentDto.getId()!=0 ? "/"+departmentDto.getId() : "");
        }
    }

    @PostMapping(value = {"/delete-department/{id}", "/delete-department/", "/delete-department"})
    public String deleteDepartment(@PathVariable Optional<Integer> id, RedirectAttributes redirectAttributes) {
        if (id.isPresent()) {
            try {
                departmentService.deleteDepartment(id.get());
                redirectAttributes.addFlashAttribute("alert", "success");
                redirectAttributes.addFlashAttribute("message", "Xóa bỏ cơ sở thành công!");
            } catch (CustomErrorException ex) {
                redirectAttributes.addFlashAttribute("alert", "danger");
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
            }
        } else
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không thể thực hiện yêu cầu do không đủ dữ liệu để xác định");
        return "redirect:/admin/departments-and-rooms";
    }
}
