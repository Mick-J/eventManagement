package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.model.*;
import com.event_management.eventmanagement.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final LeadEventRepository leadEventRepo;


    public EventService(EventRepository eventRepo,
                        UserRepository userRepo,
                        LeadEventRepository leadEventRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.leadEventRepo = leadEventRepo;
    }

    @Transactional
    public void saveEvent(EventDTO eventDTO, Event eventToUpdate) {
        //
        List<User> attendeeList = eventDTO.getCompanyAttendeeIds().stream()
                .filter(idAttendee -> idAttendee != -1 && !idAttendee.equals(eventToUpdate.getEventResponsible().getId()))
                .map(idAttendee -> {
                    User attendeeUser = userRepo.findById(idAttendee)
                            .orElseThrow(() -> new RuntimeException("User not found with ID=" + idAttendee));
                        return attendeeUser;
                })
                .collect(Collectors.toList());

        //
        List<LeadEvent> leadEventList = eventDTO.getLeadIds().stream()
                .filter(idLead -> idLead != -1)
                .map(idLead -> {
                    LeadEvent leadEvent = leadEventRepo.findById(idLead)
                            .orElseThrow(() -> new RuntimeException("LeadEvent not found with ID=" + idLead));
                    return leadEvent;
                })
                .collect(Collectors.toList());

        eventToUpdate.setCompanyAttendeeList(attendeeList);
        eventToUpdate.setLeadList(leadEventList);

        eventRepo.save(eventToUpdate);
    }

    public static void addEventCompanyListModel(Model model, CompanyRepository companyRepository) {
        // Company
        Sort sortCompanyName = Sort.by(Sort.Direction.ASC, "companyName"); // Sort by event type label
        model.addAttribute("companyList", companyRepository.findAll(sortCompanyName));
    }

    public static void addEventTypeListModel(Model model, EventTypeRepository eventTypeRepo) {
        // eventType
        Sort sort = Sort.by(Sort.Direction.ASC, "eventType"); // Sort by event type label
        List<EventType> eventTypeList = eventTypeRepo.findAll(sort);
        model.addAttribute("eventTypeList", eventTypeList);
    }

    public static void addEventUserListModel(Model model, UserRepository userRepo) {
        // event user
        Sort sort = Sort.by(Sort.Order.asc("userFirstname"), Sort.Order.asc("userLastname"));
        List<User> userList = userRepo.findAll(sort);
        model.addAttribute("userList", userList);
    }
    public static void addEventLeadListModel(Model model, LeadEventRepository leadEventRepo) {
        // event user
        Sort sort = Sort.by(Sort.Order.asc("leadFirstname"), Sort.Order.asc("leadLastname"));
        List<LeadEvent> leadEventList = leadEventRepo.findAll(sort);
        model.addAttribute("leadEventList", leadEventList);
    }

    public static Event getEvent(Event event, EventDTO eventDTO, Company company, EventType eventType, User user) {
        // Set event entity values
        return setEvent(event, eventDTO, company, eventType, user);
    }

    private static Event setEvent(Event event, EventDTO eventDTO, Company company, EventType eventType, User user) {
        event.setEventTitle(eventDTO.getEventTitle());
        event.setEventLocation(eventDTO.getEventLocation());
        event.setCompany(company);
        event.setEventType(eventType);
        event.setEventResponsible(user);
        event.setEventDateBegin(eventDTO.getEventDateBegin());
        event.setEventTimeBegin(eventDTO.getEventTimeBegin()); // AM or PM
        event.setEventDateEnd(eventDTO.getEventDateEnd());
        event.setEventTimeEnd(eventDTO.getEventTimeEnd() ); // AM or PM
        event.setEventDescription(eventDTO.getEventDescription());
        event.setUpdatedAt(Instant.now());

        return event;
    }

    public static EventDTO getNewEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setEventTitle(event.getEventTitle());
        eventDTO.setEventLocation(event.getEventLocation());
        eventDTO.setCompanyId(event.getCompany().getId());
        eventDTO.setEventTypeId(event.getEventType().getId());
        eventDTO.setEventResponsible(event.getEventResponsible().getId());
        eventDTO.setEventDateBegin(event.getEventDateBegin());
        eventDTO.setEventTimeBegin(event.getEventTimeBegin()); // AM or PM
        eventDTO.setEventDateEnd(event.getEventDateEnd());
        eventDTO.setEventTimeEnd(event.getEventTimeEnd() ); // AM or PM
        eventDTO.setEventDescription(event.getEventDescription());
        eventDTO.setCreatedAt(event.getCreatedAt());
        return eventDTO;
    }
}