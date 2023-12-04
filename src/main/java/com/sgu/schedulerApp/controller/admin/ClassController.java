package com.sgu.schedulerApp.controller.admin;

import com.sgu.schedulerApp.dto.ClassDto;
import com.sgu.schedulerApp.dto.DepartmentDto;
import com.sgu.schedulerApp.dto.FacultyDto;
import com.sgu.schedulerApp.dto.RoomDto;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.service.ClassRoomService;
import com.sgu.schedulerApp.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/faculty-and-class")
@RequiredArgsConstructor
public class ClassController {

    private final FacultyService facultyService;
    private final ClassRoomService classRoomService;

    @GetMapping
    public String getClassView(Model model, @RequestParam("facultyCode") Optional<String> facultyCode,
                               @RequestParam("keyword") Optional<String> classCode) {
        List<FacultyDto> facultyDtos = facultyService.findAllWithCodeOptional(facultyCode.orElseGet(()->""));
        facultyDtos.forEach(f->{
            List<ClassDto> classDtos = classRoomService.findByFacultyCode(classCode.orElseGet(()->""), f.getCode());
            f.setClasses(classDtos);
        });
        model.addAttribute("faculties", facultyDtos);
        model.addAttribute("facultySelect", facultyService.findAll());
        return "adminpage/class";
    }

    @GetMapping(value = {"/edit-class/{id}", "/edit-class/", "/edit-class"})
    public String getEditClassView(Model model, @PathVariable Optional<Integer> id,
                                  @RequestParam(value = "facultyCode", required = false) String facultyCode) {
        if (id.isPresent()) {
            model.addAttribute("isUpdate", true);
            if (!model.containsAttribute("classDto"))
                model.addAttribute("classDto", classRoomService.findById(id.get()));
        } else {
            model.addAttribute("isUpdate", false);
            if (!model.containsAttribute("classDto"))
                model.addAttribute("classDto", new ClassDto());
        }
        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("facultyCode", facultyCode != null ? facultyCode:"");
        return "adminpage/edit-class";
    }

    @GetMapping(value = {"/edit-faculty/{id}", "/edit-faculty/", "/edit-faculty"})
    public String getEditFacultyView(Model model, @PathVariable Optional<Integer> id) {
        if (id.isPresent()) {
            model.addAttribute("isUpdate", true);
            if (!model.containsAttribute("facultyDto"))
                model.addAttribute("facultyDto", facultyService.findById(id.get()));
        } else {
            model.addAttribute("isUpdate", false);
            if (!model.containsAttribute("facultyDto"))
                model.addAttribute("facultyDto", new FacultyDto());
        }
        return "adminpage/edit-faculty";
    }

    @PostMapping(value = "/edit-class")
    public String saveClass(@ModelAttribute("classDto") ClassDto classDto, RedirectAttributes redirectAttributes) {
        try {
            ClassDto savedClass = classRoomService.saveClass(classDto);
            int savedClassId = savedClass.getId();
            redirectAttributes.addFlashAttribute("alert", "success");
            redirectAttributes.addFlashAttribute("message", "Lưu Lớp học thành công!");
            return  "redirect:/admin/faculty-and-class/edit-class/"+savedClassId;
        } catch (CustomErrorException e) {
            redirectAttributes.addFlashAttribute("alert", "danger");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("classDto", classDto);
            return "redirect:/admin/faculty-and-class/edit-class" + (classDto.getId()!=0 ? "/"+classDto.getId() : "");
        }
    }

    @PostMapping(value = {"/delete-class/{id}", "/delete-class/", "/delete-class"})
    private String deleteClass(@PathVariable Optional<Integer> id, RedirectAttributes redirectAttributes) {
        if (id.isPresent()) {
            try {
                classRoomService.deleteClass(id.get());
                redirectAttributes.addFlashAttribute("alert", "success");
                redirectAttributes.addFlashAttribute("message", "Xóa Lớp thành công!");
            } catch (CustomErrorException e) {
                redirectAttributes.addFlashAttribute("alert", "danger");
                redirectAttributes.addFlashAttribute("message", e.getMessage());
            }
        } else
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không thể thực hiện yêu cầu do không đủ dữ liệu để xác định");
        return "redirect:/admin/faculty-and-class";
    }

    @PostMapping(value = "/edit-faculty")
    public String saveFaculty(@ModelAttribute("facultyDto") FacultyDto facultyDto, RedirectAttributes redirectAttributes) {
        try {
            FacultyDto savedFaculty = facultyService.saveFaculty(facultyDto);
            int facultyId = savedFaculty.getId();
            redirectAttributes.addFlashAttribute("alert", "success");
            redirectAttributes.addFlashAttribute("message", "Lưu Khoa thành công!");
            return  "redirect:/admin/faculty-and-class/edit-faculty/"+facultyId;
        } catch (CustomErrorException ex) {
            redirectAttributes.addFlashAttribute("alert", "danger");
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            redirectAttributes.addFlashAttribute("facultyDto", facultyDto);
            return "redirect:/admin/faculty-and-class/edit-faculty" + (facultyDto.getId()!=0 ? "/"+facultyDto.getId() : "");
        }
    }

    @PostMapping(value = {"/delete-faculty/{id}", "/delete-faculty/", "/delete-faculty"})
    public String deleteFaculty(@PathVariable Optional<Integer> id, RedirectAttributes redirectAttributes) {
        if (id.isPresent()) {
            try {
                facultyService.deleteFaculty(id.get());
                redirectAttributes.addFlashAttribute("alert", "success");
                redirectAttributes.addFlashAttribute("message", "Xóa Khoa thành công!");
            } catch (CustomErrorException ex) {
                redirectAttributes.addFlashAttribute("alert", "danger");
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
            }
        } else
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Không thể thực hiện yêu cầu do không đủ dữ liệu để xác định");
        return "redirect:/admin/faculty-and-class";
    }
}
