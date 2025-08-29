package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.DTO.EventTypeDTO;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.model.Event;
import com.event_management.eventmanagement.model.EventType;
import com.event_management.eventmanagement.repository.EventTypeRepository;
import com.event_management.eventmanagement.service.EventService;
import com.event_management.eventmanagement.service.EventTypeService;
import com.event_management.eventmanagement.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/eventType")
public class EventTypeController {

    private final EventTypeRepository eventTypeRepo;
    private final EventTypeService eventTypeService;
    private final EventService eventService;

    public EventTypeController(EventTypeRepository eventTypeRepo,
                               EventTypeService eventTypeService, EventService eventService) {
        this.eventTypeRepo = eventTypeRepo;
        this.eventTypeService = eventTypeService;
        this.eventService = eventService;
    }

    @GetMapping({"", "/"})
    public String getEventTypeList(Model model) {
        EventService.addEventTypeListModel(model, eventTypeRepo);
        return "/eventType/index";
    }

    @GetMapping("/edit")
    public String editEventType(@RequestParam Integer id, Model model) {
        Optional<EventTypeDTO> dtoOptional = eventTypeService.getEventTypeDTOById(id);

        if (dtoOptional.isEmpty()) {
            return "redirect:/eventType";
        }

        model.addAttribute("eventTypeDTO", dtoOptional.get());
//        model.addAttribute("id", id);

        return "/eventType/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editEventType(
            @RequestParam Integer id,
            @Valid @ModelAttribute EventTypeDTO eventTypeDTO) {
        eventTypeService.updateEventType(id, eventTypeDTO);
        return ResponseEntity.ok(new ApiMessage(
                "success",
                "EventType Updated",
                "EventType updated successfully",
                Collections.emptyMap()));
    }

    @GetMapping("/display")
    public String displayEventType(@RequestParam Integer id, Model model) {
       return eventTypeService.displayEventType(id, model);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ApiMessage> addNewEventType() {
        EventType eventType = eventTypeService.addNewEventType(1 + eventTypeService.getLastEventTypeId());
        return  ResponseEntity.ok(new ApiMessage(
                "success",
                "Add success",
                "New company blueprint added successfully",
                Map.of("id", eventType.getId().toString())
        ));
    }

    @GetMapping("/add")
    public ResponseEntity<?> addEventType() {
         EventType newEventType = eventTypeService.addNewEventType(eventTypeService.getLastEventTypeId());
         return ResponseEntity.ok(new ApiMessage(
                 "success",
                 "EventType added",
                 "EventType added successfully",
                 Map.of("id", newEventType.getId().toString())
         ));
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<ApiMessage> deleteEventType(@RequestParam Integer id) {
        return eventTypeService.deleteEventTypeById(id);
    }
}