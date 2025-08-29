package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.DTO.EventTypeDTO;
import com.event_management.eventmanagement.controllerAdvice.DuplicateResourceException;
import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.model.EventType;
import com.event_management.eventmanagement.repository.EventRepository;
import com.event_management.eventmanagement.repository.EventTypeRepository;
import com.event_management.eventmanagement.utils.ApiMessage;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Service
public class EventTypeService {

    private static final Logger logger = LogManager.getLogger(EventTypeService.class);

    private final EventTypeRepository eventTypeRepo;
    private final EventRepository eventRepo;


    public EventTypeService(EventTypeRepository eventTypeRepo, EventRepository eventRepo) {
        this.eventTypeRepo = eventTypeRepo;
        this.eventRepo = eventRepo;
    }

    public Optional<EventTypeDTO> getEventTypeDTOById(Integer id) {
        return eventTypeRepo.findById(id).map((eventType)-> {
            EventTypeDTO dto = new EventTypeDTO(eventType.getId(),
                    eventType.getEventType(),
                    eventType.getUpdatedAt());

            try {
                SanitizationUtils.sanitizeObjectFields(dto, "unescape");
            } catch (Exception ex) {
                logger.warn("Error while sanitizing EventTypeDTO: {}", ex.getMessage());
            }
            return dto;
        });
    }

    public void updateEventType(Integer id, EventTypeDTO eventTypeDTO) {
        // Fetch EventType
        EventType eventType = eventTypeRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                "EventType with id: {} not find when editing",
                "EventType not found"
        ));

        // Check for company name uniqueness
        if (isEventTypeNameTaken(eventTypeDTO)) {
            throw new DuplicateResourceException(
                    "EvenType name: "+ eventTypeDTO.eventType() + " already exist when updating eventType with ID = " + eventTypeDTO.id(),
                    "EvenType name already exist."
            );
        }

        // Update eventType
        eventType.setEventType(eventTypeDTO.eventType());
        eventType.setUpdatedAt(Instant.now());

        // Sanitize and save
        SanitizationUtils.sanitizeObjectFields(eventType, "escape");
        eventTypeRepo.save(eventType);
    }

    public String displayEventType(Integer id, Model model) {
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

    public ResponseEntity<ApiMessage> deleteEventTypeById(Integer id) {
        boolean isUsed = eventRepo.existsByEventTypeId(id);
        //
        if (isUsed) {
            return ResponseEntity.badRequest().body(new ApiMessage(
                    "error",
                    "Delete error",
                    "Cannot delete. Event Type is in use by event(s)",
                    Collections.emptyMap()
            ));
        }
        //
        eventTypeRepo.deleteById(id);
        ApiMessage message = new ApiMessage(
                "success",
                "Delete success",
                "Event Type deleted successfully!",
                Collections.emptyMap()
        );
        return ResponseEntity.ok(message);
    }

    public int getLastEventTypeId() {
        EventType eventType = eventTypeRepo.findTopByOrderByIdDesc();
        return eventType != null ? 1 + eventType.getId() : 1;
    }

    public EventType addNewEventType(int lastId) {
        EventType eventType = new EventType("--New " + (1 + lastId) + "--",
                Instant.now(),
                Instant.now());
        return eventTypeRepo.save(eventType);
    }

    public void addModelEventTypeList(Model model) {
        Sort sort = Sort.by(Sort.Direction.ASC, "eventType");
        model.addAttribute("eventTypeList", eventTypeRepo.findAll(sort));
    }

    public boolean isEventTypeNameTaken(EventTypeDTO eventTypeDTO) {
        return eventTypeRepo.findEventTypeByEventType(eventTypeDTO.eventType().strip())
                .filter(e -> !e.getId().equals(eventTypeDTO.id()))
                .isPresent();
    }

}