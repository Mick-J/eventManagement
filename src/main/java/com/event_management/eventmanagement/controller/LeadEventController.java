package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.DTO.LeadEventDTO;
import com.event_management.eventmanagement.model.EventType;
import com.event_management.eventmanagement.model.LeadEvent;
import com.event_management.eventmanagement.service.LeadService;
import com.event_management.eventmanagement.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/leadEvent")
public class LeadEventController {

    private final LeadService leadService;

    public LeadEventController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping({"", "/"})
    public String getLeadEventList(Model model) {
        List<LeadEvent> leadEventList = leadService.getLeadList();
        model.addAttribute("leadEventList", leadEventList);
        return "leadEvent/index";
    }

    @GetMapping("/edit")
    public String editLeadEvent( @RequestParam Integer id, Model model) {
        Optional<LeadEventDTO> LeadDTOOptional = leadService.getLeadEventDTOById(id);
        if (LeadDTOOptional.isEmpty()) {
            return "redirect:/leadEvent";
        }

        model.addAttribute("leadEventDTO", LeadDTOOptional.get());
        return "/leadEvent/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editLeadEvent(@RequestParam Integer id,
                                           @RequestParam MultipartFile leadBCardImgFile,
                                           @Valid @ModelAttribute LeadEventDTO leadEventDTO)
    {
        leadService.updateLeadEvent(id, leadBCardImgFile, leadEventDTO);
        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Updated success",
                "Lead updated successfully",
                Collections.emptyMap()
        ));
    }

    @GetMapping("/display")
    public String displayLeadEvent(@RequestParam Integer id, Model model) {
        return leadService.displayLeadEvent(id, model);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ApiMessage> addNewEventType() {
        LeadEvent leadEvent = leadService.addNewLeadEvent(1+ leadService.getLastLeadEventId());
        return  ResponseEntity.ok(new ApiMessage(
                "success",
                "Add success",
                "New company blueprint added successfully",
                Map.of("id", leadEvent.getId().toString())
        ));
    }

    @GetMapping("/add")
    public ResponseEntity<?> addLeadEvent() {
         LeadEvent leadEvent = leadService.addNewLeadEvent(leadService.getLastLeadEventId());
         return ResponseEntity.ok(new ApiMessage(
                 "success",
                 "Added success",
                 "Add new dummy leadEvent",
                 Map.of("id", leadEvent.getId().toString())
         ));
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<ApiMessage> deleteLeadEvent(@RequestParam Integer id) {
        return leadService.deleteLeadEventById(id);
    }

}