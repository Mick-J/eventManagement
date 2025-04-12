package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.*;
import com.event_management.eventmanagement.repository.*;
import com.event_management.eventmanagement.service.EventService;
import com.event_management.eventmanagement.utils.DateUtils;
import com.event_management.eventmanagement.utils.FormMessage;
import com.event_management.eventmanagement.utils.Message;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepo;
    private final CompanyRepository companyRepo;
    private final EventTypeRepository eventTypeRepo;
    private final UserRepository userRepo;
    private final LeadEventRepository leadEventRepo;
    private final EventService eventService;

    private static final Logger logger = LogManager.getLogger(EventController.class);

    public EventController(EventService eventService,
                           EventRepository eventRepo,
                           CompanyRepository companyRepo,
                           EventTypeRepository eventTypeRepo,
                           UserRepository userRepo,
                           LeadEventRepository leadEventRepo) {
        this.eventService = eventService;
        this.eventRepo = eventRepo;
        this.companyRepo = companyRepo;
        this.eventTypeRepo = eventTypeRepo;
        this.userRepo = userRepo;
        this.leadEventRepo = leadEventRepo;
    }

    @GetMapping({"", "/"})
    public String getEvent(Model model) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Event> event_list = eventRepo.findAll(sort);
        model.addAttribute("event_list", event_list);
        return "events/index";
    }

    @GetMapping("/add")
    public RedirectView addEvent(Model model, RedirectAttributes attributes) {
        EventType eventType = eventTypeRepo.findById(2).orElse(null);
        if (eventType == null) {
            logger.info("Error event Type when adding new event");
            return new RedirectView("/events/index");
        }
        Company company = companyRepo.findById(1).orElse(null);
        if (company == null) {
            logger.info("Error Company when adding new event");
            return new RedirectView("/events/index");
        }
        User userResponsible = userRepo.findById(1).orElse(null);
        if (userResponsible == null) {
            logger.info("Error User responsible when adding new event");
            return new RedirectView("/events/index");
        }

        LocalDate dateBegin = LocalDate.now();
        LocalDate dateEnd = dateBegin.plusDays(1);
        LocalTime timeBegin = LocalTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm");

        Event event = new Event();
        event.setEventTitle("--NEW--");
        event.setEventType(eventType);
        event.setCompany(company);
        event.setEventDateBegin(dateBegin.format(df));
        event.setEventTimeBegin(timeBegin.format(tf));
        event.setEventDateEnd(dateEnd.format(df));
        event.setEventTimeEnd(timeBegin.format(tf));
        event.setEventLocation("--UNKNOWN--");
        event.setEventResponsible(userResponsible);
        event.setCreatedAt(Instant.now());
        event.setUpdatedAt(Instant.now());

        try {
            event = eventRepo.save(event);

            EventService.addEventCompanyListModel(model,companyRepo);
            EventService.addEventTypeListModel(model, eventTypeRepo);
            EventService.addEventUserListModel(model, userRepo);
            EventService.addEventLeadListModel(model, leadEventRepo);

            EventDTO eventDTO = EventService.getNewEventDTO(event);
            attributes.addAttribute("id", event.getId());
            model.addAttribute("id", event.getId());
            model.addAttribute("eventDTO", eventDTO);
            return new RedirectView("/events/edit");

        } catch (Exception ex) {
            logger.info("Error when creating new event with error {} ", ex.getMessage());
        }
        return new RedirectView("/company/index");
    }

    @GetMapping("/edit")
    public String editEvent(Model model, @RequestParam Integer id) {
        Event event = eventRepo.findById(id).orElse(null);
        if (event == null) {
            return "redirect:/events";
        }

        //
        EventDTO eventDTO = EventService.getNewEventDTO(event);
        //
        event.getCompanyAttendeeList().sort(Comparator.comparing(User::getUserFirstname)
                .thenComparing(User::getUserLastname));
        List<Integer> attendeeIdList = event.getCompanyAttendeeList().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        while (attendeeIdList.size() < 20) {
            attendeeIdList.add(-1);
        }

        eventDTO.setCompanyAttendeeIds(attendeeIdList);
        //
        event.getLeadList().sort(Comparator.comparing(LeadEvent::getLeadFirstname)
                .thenComparing(LeadEvent::getLeadLastname));
        List<Integer> leadIdList = event.getLeadList().stream()
                .map(LeadEvent::getId)
                .collect(Collectors.toList());
        while (leadIdList.size() < 20) {
            leadIdList.add(-1);
        }
        eventDTO.setLeadIds(leadIdList);

        EventService.addEventCompanyListModel(model,companyRepo);
        EventService.addEventTypeListModel(model, eventTypeRepo);
        EventService.addEventUserListModel(model, userRepo);
        EventService.addEventLeadListModel(model, leadEventRepo);

        // Add data to model
        model.addAttribute("hasTempusDominus", true);
        model.addAttribute("hasFontAwesome", true);
        model.addAttribute("hasPopper", true);
        //
        try {
            SanitizationUtils.sanitizeObjectFields(eventDTO, "unescape");
        } catch (Exception ex) {
            logger.info("Error while sanitizing {} ", ex.getMessage());
        }
        model.addAttribute("eventDTO", eventDTO);

        return "events/edit";
    }


    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editEvent( @Valid @ModelAttribute EventDTO eventDTO,
                                  BindingResult result
    ) {
        String objectName = "Event";

        Event event = eventRepo.findById(eventDTO.getId()).orElse(null);
        if (event == null) {
            Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                    objectName + " to update not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }

        //
        Company company = companyRepo.findById(eventDTO.getCompanyId()).orElse(null);
        EventType eventType = eventTypeRepo.findById(eventDTO.getEventTypeId()).orElse(null);
        User  userResponsible = userRepo.findById(eventDTO.getEventResponsible()).orElse(null);

        if (company == null) {
            result.addError(new FieldError("eventDto", "companyId","Company selected not found"));
        }
        if (eventType == null) {
           result.addError(new FieldError("eventDto", "eventTypeId","Event Type selected not found"));
        }
        if (userResponsible == null) {
            result.addError(new FieldError("eventDto", "eventResponsible","Event Responsible not found"));
        }

        // Validate Form fields
        if (!DateUtils.isDateValid(eventDTO.getEventDateBegin())) {
            result.addError(new FieldError("eventDto", "eventDateBegin", "Invalid event date begin"));
        }
        if (!DateUtils.isDateValid(eventDTO.getEventDateEnd())) {
            result.addError(new FieldError("eventDto", "eventDateEnd", "Invalid event date end"));
        }
        if (!DateUtils.isDateBeginBeforeEnd(eventDTO.getEventDateBegin(), eventDTO.getEventDateEnd(), "yyyy-MM-dd") &&
                !DateUtils.isDatesEqual(eventDTO.getEventDateBegin(), eventDTO.getEventDateEnd(), "yyyy-MM-dd")) {
            result.addError(new FieldError("eventDto", "eventDateBegin", "Date begin should be same or before date end"));
        }

        //
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            Message message = FormMessage.withMessage(errors, "error", "Something went wrong",
                    objectName + " not updated");
            return ResponseEntity.badRequest().body(message);
        }

        // Update event record
        Event eventToUpdate = EventService.getEvent(event, eventDTO, company, eventType, userResponsible);
        try {
            SanitizationUtils.sanitizeObjectFields(eventToUpdate, "escape");
            eventService.saveEvent(eventDTO, eventToUpdate);
            Message message = FormMessage.withMessage(null, "success", objectName + " update",
                    objectName + " updated successfully!");
            return ResponseEntity.ok(message);
        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getRootCause();
            String errorMessage = (rootCause != null) ? rootCause.getMessage() : "Unknown SQL Error";
            logger.error("SQL Error: {}", errorMessage);
        } catch (Exception ex) {
            logger.info("Something went wrong when saving Event with id: {}, with error: {}",
                    eventToUpdate.getId(), ex.getMessage());
        }

        Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                objectName + " not updated");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @GetMapping("/display")
    public String displayEvent(Model model, @RequestParam Integer id) {
        Event event = eventRepo.findById(id).orElse(null);
        if (event == null) {
            return "redirect:/events";
        }
        //
        event.getCompanyAttendeeList().sort(Comparator.comparing(User::getUserFirstname)
                .thenComparing(User::getUserLastname));
        //
        event.getLeadList().sort(Comparator.comparing(LeadEvent::getLeadFirstname)
                .thenComparing(LeadEvent::getLeadLastname));

        //
        try {
            SanitizationUtils.sanitizeObjectFields(event, "unescape");
        } catch (Exception e) {
            logger.info("Error while unescape html tags in Event object with id = {}", event.getId());
        }
        // Add data to model
        model.addAttribute("event", event);
        return "events/display";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<Message> deleteEvent(@RequestParam Integer id) {
        try {
            eventRepo.deleteById(id);
            Message message = new Message.Builder()
                    .withMessageType("success")
                    .withMessageHeader("Delete Success")
                    .withMessageBody("Event deleted successfully!")
                    .build();
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            Message message = new Message.Builder()
                    .withMessageType("error")
                    .withMessageHeader("Something went wrong")
                    .withMessageBody("Event not deleted")
                    .build();
            logger.info("Something went wrong when deleting Event  with id = {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }
}