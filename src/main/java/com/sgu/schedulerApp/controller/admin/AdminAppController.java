package com.sgu.schedulerApp.controller.admin;

import com.sgu.schedulerApp.dto.FacultyDto;
import com.sgu.schedulerApp.dto.StatisticDto;
import com.sgu.schedulerApp.service.EventService;
import com.sgu.schedulerApp.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAppController {

    private final EventService eventService;
    private final FacultyService facultyService;

    @GetMapping(value = "/")
    public String home() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping(value = "/dashboard")
    public String getDashboardView(Model model) {
        long totalEventHeld = eventService.countTotalEventHeld("2023", null);
        long totalEnroll = eventService.countTotalEnroll("2023", null);
        List<StatisticDto> statisticData = eventService.getStatisticForAdminDashboard("2023", null);
        List<FacultyDto> statisticFacultyData = eventService.getStatisticFacultyData("2023");
        List<FacultyDto> faculties = facultyService.findAll();
        model.addAttribute("totalEventHeld", totalEventHeld);
        model.addAttribute("totalEnroll", totalEnroll);
        model.addAttribute("statisticData", statisticData);
        model.addAttribute("statisticFacultyData", statisticFacultyData);
        model.addAttribute("faculties", faculties);
        return "adminpage/index";
    }
}
