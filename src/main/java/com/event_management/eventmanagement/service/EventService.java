package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.DTO.EventDTO;
import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.model.*;
import com.event_management.eventmanagement.repository.*;
import com.event_management.eventmanagement.utils.DateUtils;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final LeadEventRepository leadEventRepo;
    private final EventTypeRepository eventTypeRepo;
    private final CompanyRepository companyRepo;
    private final CompanyService companyService;
    private final EventTypeService eventTypeService;
    private final UserService userService;
    private final LeadService leadService;

    public EventService(EventRepository eventRepo,
                        UserRepository userRepo,
                        LeadEventRepository leadEventRepo,
                        EventTypeRepository eventTypeRepo,
                        CompanyRepository companyRepo,
                        CompanyService companyService,
                        EventTypeService eventTypeService,
                        UserService userService,
                        LeadService leadService) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.leadEventRepo = leadEventRepo;
        this.eventTypeRepo = eventTypeRepo;
        this.companyRepo = companyRepo;
        this.companyService = companyService;
        this.eventTypeService = eventTypeService;
        this.userService = userService;
        this.leadService = leadService;
    }

    public static void addEventTypeListModel(Model model, EventTypeRepository eventTypeRepo) {
        // eventType
        Sort sort = Sort.by(Sort.Direction.ASC, "eventType"); // Sort by event type label
        List<EventType> eventTypeList = eventTypeRepo.findAll(sort);
        model.addAttribute("eventTypeList", eventTypeList);
    }

    public Event getEvent(Event event, EventDTO eventDTO, Company company, EventType eventType, User user) {
        return mapToEvent(event, eventDTO, company, eventType, user);
    }

    private Event mapToEvent(Event event, EventDTO eventDTO, Company company, EventType eventType, User user) {
        event.setEventTitle(eventDTO.eventTitle());
        event.setEventLocation(eventDTO.eventLocation());
        event.setCompany(company);
        event.setEventType(eventType);
        event.setEventResponsible(user);
        event.setEventDateBegin(eventDTO.eventDateBegin());
        event.setEventTimeBegin(eventDTO.eventTimeBegin());
        event.setEventDateEnd(eventDTO.eventDateEnd());
        event.setEventTimeEnd(eventDTO.eventTimeEnd() );
        event.setEventDescription(eventDTO.eventDescription());
        event.setUpdatedAt(Instant.now());
        //
        List<User> attendeeList = getUserList(eventDTO, event);
        List<LeadEvent> leadEventList = getLeadEventList(eventDTO);
        event.setCompanyAttendeeList(attendeeList);
        event.setLeadList(leadEventList);

        return event;
    }

    private EventDTO mapToEventDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getEventType().getId(),
                event.getCompany().getId(),
                event.getEventResponsible().getId(),
                event.getEventTitle(),
                event.getEventLocation(),
                event.getEventDescription(),
                event.getEventDateBegin(), event.getEventDateEnd(),
                event.getEventTimeBegin(), event.getEventTimeEnd(),
                getLeadIdList(event, true),
                getUserIdList(event, true),
                event.getCreatedAt(), event.getUpdatedAt()
                );
    }

    private List<Integer> getLeadIdList(Event event, boolean forView) {
        //
        event.getLeadList().sort(Comparator.comparing(LeadEvent::getLeadFirstname)
                .thenComparing(LeadEvent::getLeadLastname));
        //
        List<Integer> leadIdList = event.getLeadList().stream()
                .map(LeadEvent::getId)
                .distinct()
                .collect(Collectors.toList());

        //
        if (forView) {
            while (leadIdList.size() < 20) {
                leadIdList.add(-1);
            }
        }

        return leadIdList;
    }

    private List<User> getUserList(EventDTO eventDTO, Event eventToUpdate) {
        return eventDTO.companyAttendeeIds().stream()
                .filter(idAttendee -> idAttendee != -1 && !idAttendee.equals(eventToUpdate.getEventResponsible().getId()))
                .map(idAttendee ->
                     userRepo.findById(idAttendee)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "User not found with ID=" + idAttendee,
                                    "Something went wrong, please contact admin"))
                )
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Integer> getUserIdList(Event event, boolean forView) {
        event.getCompanyAttendeeList().sort(Comparator.comparing(User::getUserFirstname)
                .thenComparing(User::getUserLastname));
        List<Integer> userIdList = event.getCompanyAttendeeList().stream()
                .map(User::getId)
                .filter(id -> id != event.getEventResponsible().getId().intValue())
                .distinct()
                .collect(Collectors.toList());
        if (forView) {
            while (userIdList.size() < 20) {
                userIdList.add(-1);
            }
        }
        return userIdList;
    }

    private List<LeadEvent> getLeadEventList(EventDTO eventDTO) {
        return eventDTO.leadIds().stream()
                .filter(idLead -> idLead != -1)
                .map(idLead -> leadEventRepo.findById(idLead)
                            .orElseThrow(() -> new ResourceNotFoundException("LeadEvent not found with ID=" + idLead,
                                    "Something went wrong, please contact the admin"))
                )
                .distinct()
                .collect(Collectors.toList());
    }

    public int getLastEventId() {
        Event event = eventRepo.findTopByOrderByIdDesc();
        return event != null ? event.getId() : 1;
    }

    public Event addNewEvent(int lastEventId) {

        EventType eventType = eventTypeRepo.findById(2).orElseThrow(() -> new ResourceNotFoundException(
                "Default event Type not found while creating new Event",
                "Event could not be created, Please contact admin"
        ));

        Company company = companyRepo.findById(1).orElseThrow(() -> new ResourceNotFoundException(
                "Default company not found while creating new Event",
                "Event could not be created, Please contact admin"
        ));

        User userResponsible = userRepo.findById(1).orElseThrow(() -> new ResourceNotFoundException(
                "Default event responsible not found while creating new Event",
                "Event could not be created, Please contact admin"
        ));

        LocalDate dateBegin = LocalDate.now();
        LocalDate dateEnd = dateBegin.plusDays(1);
        LocalTime timeBegin = LocalTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm");

        Event event = new Event();
        event.setEventTitle("--NEW" + (1 + lastEventId) + "--");
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

        return eventRepo.save(event);
    }

    public EventDTO prepareEventForEdit(Integer id, Model model) {
        Event event = eventRepo.findById(id).orElse(null);

        if (event == null) {
            return null;
        }

        // EventDTO
        EventDTO eventDTO = mapToEventDTO(event);

        // Populate model with dropdown lists
        companyService.addModelCompanyList(model);
        eventTypeService.addModelEventTypeList(model);
        userService.addModelUserList(model);
        leadService.addModelEventLeadList(model);

        SanitizationUtils.sanitizeObjectFields(eventDTO, "unescape");

        return eventDTO;
    }

    public void updateEvent(EventDTO eventDTO, BindingResult result) throws BindException {
        Event event = eventRepo.findById(eventDTO.id())
                .orElseThrow(()-> new ResourceNotFoundException(
                    "Event with id:" + eventDTO.id() + " not found for update" ,
                    "Could not find Event for update"
        ));

        //
        Company company = companyRepo.findById(eventDTO.companyId())
                .orElseThrow(()-> new ResourceNotFoundException(
                    "Company with ID=" + eventDTO.companyId() + " not found while updating Event id=" + eventDTO.id(),
                    "Could not find Event for update"
        ));
        //
        EventType eventType = eventTypeRepo.findById(eventDTO.eventTypeId())
                .orElseThrow(()-> new ResourceNotFoundException(
                    "eventType with ID=" + eventDTO.eventTypeId() + " not found while updating Event id=" + eventDTO.id(),
                    "Could not find Event for update"
        ));
        //
        User  userResponsible = userRepo.findById(eventDTO.eventResponsible()).orElseThrow(()-> new ResourceNotFoundException(
                "User with ID=" + eventDTO.eventResponsible() + " not found while updating Event id=" + eventDTO.id(),
                "Could not find Event for update"
        ));

        // Validate
        if (!DateUtils.isDateBeginBeforeEnd(eventDTO.eventDateBegin(), eventDTO.eventDateEnd(), "yyyy-MM-dd") &&
                !DateUtils.isDatesEqual(eventDTO.eventDateBegin(), eventDTO.eventDateEnd(), "yyyy-MM-dd")) {
            result.addError(new FieldError("eventDto", "eventDateBegin", "Date begin should be same or before date end"));
        }

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        // Update event record
        Event eventToUpdate = getEvent(event, eventDTO, company, eventType, userResponsible);
        SanitizationUtils.sanitizeObjectFields(eventToUpdate, "escape");
        eventRepo.save(eventToUpdate);
    }

    public String displayEvent(Integer id, Model model) {
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

    public void deleteEvent(Integer id) {
        eventRepo.deleteById(id);
    }
}