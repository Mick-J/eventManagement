package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.EventType;
import com.event_management.eventmanagement.model.EventTypeDTO;
import com.event_management.eventmanagement.repository.EventRepository;
import com.event_management.eventmanagement.repository.EventTypeRepository;
import com.event_management.eventmanagement.service.EventService;
import com.event_management.eventmanagement.utils.FormMessage;
import com.event_management.eventmanagement.utils.Message;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/eventType")
public class EventTypeController {

    private final EventTypeRepository eventTypeRepo;
    private final EventRepository eventRepo;

    private static final Logger logger = LogManager.getLogger(EventTypeController.class);

    public EventTypeController(EventTypeRepository eventTypeRepo, EventRepository eventRepo) {
        this.eventTypeRepo = eventTypeRepo;
        this.eventRepo = eventRepo;
    }

    @GetMapping({"", "/"})
    public String getEventTypeList(Model model) {
        EventService.addEventTypeListModel(model, eventTypeRepo);
        return "/eventType/index";
    }

    @GetMapping("/edit")
    public String editEventType(Model model, @RequestParam Integer id) {
        EventType eventType = eventTypeRepo.findById(id).orElse(null);

        if (eventType == null) {
            logger.info("Edit Event Type with id={} not found", id);
            return "redirect:/eventType";
        }

        EventTypeDTO eventTypeDTO = new EventTypeDTO();
        eventTypeDTO.setEventType(eventType.getEventType());
        eventTypeDTO.setUpdatedAt(Instant.now());
        //
        try {
            SanitizationUtils.sanitizeObjectFields(eventTypeDTO, "unescape");
        } catch (Exception ex) {
            logger.info("Error while sanitizing {} ", ex.getMessage());
        }
        model.addAttribute("eventType", eventType);
        model.addAttribute("eventTypeDTO", eventTypeDTO);

        return "/eventType/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editEventType(
            @RequestParam Integer id,
            @Valid @ModelAttribute EventTypeDTO eventTypeDTO,
            BindingResult result) {
        String objectName = "Event type";

        EventType eventType = eventTypeRepo.findById(id).orElse(null);

        if (eventType == null) {
            Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                    objectName + " not updated");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            Message message = FormMessage.withMessage(errors, "error", "Something went wrong",
                    objectName + " not updated");
            return ResponseEntity.badRequest().body(message);
        }

        eventType.setEventType(eventTypeDTO.getEventType());
        eventType.setUpdatedAt(Instant.now());

        try {
            SanitizationUtils.sanitizeObjectFields(eventType, "escape");
            eventTypeRepo.save(eventType);
            Message message = FormMessage.withMessage(null, "success", objectName + " update",
                    objectName + " updated successfully!");
            return ResponseEntity.ok(message);
        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getRootCause();
            String errorMessage = (rootCause != null) ? rootCause.getMessage() : "Unknown SQL Error";
            logger.error("SQL Error: {}", errorMessage);
        } catch (Exception ex) {
            logger.error("Error while saving event type: {}", ex.getMessage());
        }
        Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                objectName + " not updated");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @GetMapping("/display")
    public String displayEventType(Model model, @RequestParam Integer id) {
        EventType eventType = eventTypeRepo.findById(id).orElse(null);

        if (eventType == null) {
            return "redirect:/eventType";
        }
        //
        try {
            SanitizationUtils.sanitizeObjectFields(eventType, "unescape");
        } catch (Exception e) {
            logger.info("Error while unescape html tags in Event Type object with id = {}", eventType.getId());
        }
        model.addAttribute("eventType", eventType);

        return "/eventType/display";
    }

    @GetMapping("/add")
    public RedirectView addEventType(RedirectAttributes attributes) {
        EventType eventType = new EventType("--New--", Instant.now(), Instant.now());

        try {
            eventType = eventTypeRepo.save(eventType);
        } catch (Exception ex) {
            logger.error("Error while adding new eventType {} ", ex.getMessage());
            return new RedirectView("/eventType");
        }

        attributes.addAttribute("id", eventType.getId());
        return new RedirectView("/eventType/edit");
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<Message> deleteEventType(@RequestParam Integer id) {
        boolean isUsed = eventRepo.existsByEventTypeId(id);
        if (!isUsed) {
            try {
                eventTypeRepo.deleteById(id);
                Message message = new Message.Builder()
                        .withMessageType("success")
                        .withMessageHeader("Delete success")
                        .withMessageBody("Event Type deleted successfully!")
                        .build();
                return ResponseEntity.ok(message);
            } catch (Exception e) {
                Message message = new Message.Builder()
                        .withMessageType("error")
                        .withMessageHeader("Something went wrong")
                        .withMessageBody("Event type not delete")
                        .build();
                logger.info("Something went wrong when deleting Event type with id = {}", id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
            }
        }

        //
        Message message = new Message.Builder()
                .withMessageType("error")
                .withMessageHeader("Delete error")
                .withMessageBody("Cannot delete. Event Type is in use by event(s)")
                .build();
        return ResponseEntity.badRequest().body(message);
    }
}