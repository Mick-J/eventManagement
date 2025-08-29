package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.DTO.EventDTO;
import com.event_management.eventmanagement.model.Event;
import com.event_management.eventmanagement.model.EventType;
import com.event_management.eventmanagement.repository.EventRepository;
import com.event_management.eventmanagement.service.EventService;
import com.event_management.eventmanagement.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepo;
    private final EventService eventService;


    public EventController(EventRepository eventRepo, EventService eventService) {
        this.eventService = eventService;
        this.eventRepo = eventRepo;
    }

    @GetMapping({"", "/"})
    public String getEvent(Model model) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Event> event_list = eventRepo.findAll(sort);
        model.addAttribute("event_list", event_list);
        return "events/index";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ApiMessage> addNewEventType() {
        Event event = eventService.addNewEvent(1+eventService.getLastEventId());
        return  ResponseEntity.ok(new ApiMessage(
                "success",
                "Add success",
                "New company blueprint added successfully",
                Map.of("id", event.getId().toString())
        ));
    }

    @GetMapping("/add")
    public ResponseEntity<?> addEvent() {
        Event newEvent = eventService.addNewEvent(eventService.getLastEventId());
        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Add success",
                "Event added Successfully",
                Map.of("id", newEvent.getId().toString())
        ));
    }

    @GetMapping("/edit")
    public String editEvent(@RequestParam Integer id, Model model) {
            EventDTO eventDTO = eventService.prepareEventForEdit(id, model);

            if (eventDTO == null) {
                return "redirect:/events";
            }

            model.addAttribute("eventDTO", eventDTO);
            model.addAttribute("hasTempusDominus", true);
            model.addAttribute("hasFontAwesome", true);
            model.addAttribute("hasPopper", true);

            return "events/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editEvent(@Valid @ModelAttribute EventDTO eventDTO, BindingResult result) throws BindException {

        eventService.updateEvent(eventDTO, result);
        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Update success",
                "Event updated successfully",
                Collections.emptyMap()
        ));
    }

    @GetMapping("/display")
    public String displayEvent(@RequestParam Integer id, Model model) {
        return eventService.displayEvent(id, model);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteEvent(@RequestParam Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Delete success",
                "Event deleted successfully",
                Collections.emptyMap()
        ));
    }
}