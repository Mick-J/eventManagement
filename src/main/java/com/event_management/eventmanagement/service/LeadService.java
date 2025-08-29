package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.DTO.LeadEventDTO;
import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.model.LeadEvent;
import com.event_management.eventmanagement.repository.LeadEventRepository;
import com.event_management.eventmanagement.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LeadService {

    private static final Logger logger = LogManager.getLogger(LeadService.class);

    private final LeadEventRepository leadEventRepo;

    public LeadService(LeadEventRepository leadEventRepo) {
        this.leadEventRepo = leadEventRepo;
    }

    public List<LeadEvent> getLeadList() {
        Sort sort = Sort.by(Sort.Order.asc("leadFirstname"), Sort.Order.asc("leadLastname"));
        return leadEventRepo.findAll(sort);
    }

    public void addModelEventLeadList(Model model) {
        Sort sort = Sort.by(Sort.Order.asc("leadFirstname"), Sort.Order.asc("leadLastname"));
        model.addAttribute("leadEventList", leadEventRepo.findAll(sort));
    }

    public Optional<LeadEventDTO> getLeadEventDTOById(Integer id) {
        return leadEventRepo.findById(id).map((leadEvent) -> {
            LeadEventDTO leadEventDTO = new LeadEventDTO(
                    leadEvent.getId(),
                    leadEvent.getLeadFirstname(), leadEvent.getLeadLastname(),
                    leadEvent.getLeadEmail(), leadEvent.getLeadPhone(),
                    leadEvent.getLeadCompanyName(),
                    leadEvent.getLeadInterestLevel(), leadEvent.getComment(),
                    leadEvent.getLeadState(), leadEvent.getLeadBCardImg(),
                    leadEvent.getCreatedAt(), leadEvent.getUpdatedAt()
            );
            //
            try {
                SanitizationUtils.sanitizeObjectFields(leadEventDTO, "unescape");
            } catch (Exception ex) {
                logger.warn("Error while sanitizing DTO: {}", ex.getMessage());
            }
            return leadEventDTO;
        });
    }

    public void updateLeadEvent(Integer id, MultipartFile leadBCardImgFile, LeadEventDTO leadEventDTO) {
        // check if leadEvent in DB
        LeadEvent leadEvent = leadEventRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "LeadEvent with ID" + id + " not found when updating",
                "LeadEvent not found, please contact the admin."
        ));

        // map leadEventDTO to leadEvent
         mapToLeadEvent(leadEvent, leadEventDTO);

        // file upload
        if (FileUtils.isFileSent(leadBCardImgFile)) {
            uploadLeadEventCard(leadEvent, leadBCardImgFile);
        }
        // sanitize and Save
        SanitizationUtils.sanitizeObjectFields(leadEvent, "escape");
        leadEventRepo.save(leadEvent);
    }

    public String displayLeadEvent(Integer id, Model model) {
        LeadEvent leadEvent = leadEventRepo.findById(id).orElse(null);

        if (leadEvent == null) {
            return "redirect:leadEvent/index";
        }

        //
        try {
            SanitizationUtils.sanitizeObjectFields(leadEvent, "unescape");
        } catch (Exception e) {
            logger.info("Error while unescape html tags in lead Event object with id = {}", leadEvent.getId());
        }
        model.addAttribute("hasFancyImg", true);
        model.addAttribute("leadEvent", leadEvent);
        return "leadEvent/display";
    }

    public int getLastLeadEventId() {
        LeadEvent lastLeadEvent = leadEventRepo.findTopByOrderByIdDesc();
        return (lastLeadEvent != null) ? lastLeadEvent.getId() : 1;
    }

    private void mapToLeadEvent(LeadEvent leadEvent, LeadEventDTO leadEventDTO) {
        leadEvent.setLeadFirstname(leadEventDTO.leadFirstName());
        leadEvent.setLeadLastname(leadEventDTO.leadLastName());
        leadEvent.setLeadEmail(leadEventDTO.leadEmail());
        leadEvent.setLeadPhone(leadEventDTO.leadPhone());
        leadEvent.setLeadCompanyName(leadEventDTO.leadCompanyName());
        leadEvent.setLeadInterestLevel(leadEventDTO.leadInterestLevel());
        leadEvent.setComment(leadEventDTO.comment());
        leadEvent.setLeadState(leadEventDTO.leadState());
        leadEvent.setUpdatedAt(Instant.now());
    }

    private void uploadLeadEventCard(LeadEvent leadEvent, MultipartFile leadBCardImgFile) {
            String filename = leadBCardImgFile.getOriginalFilename();
            if (filename != null) {
                // file rename
                String fileExtension = filename.substring(filename.lastIndexOf('.'));
                String storeFileNewName = leadEvent.getLeadFirstname() + "_" + leadEvent.getLeadCompanyName() + "_card" + fileExtension;

                //
                String uploadDir = "uploads/leadEvent/";
                try {
                    FileUtils.uploadFile(uploadDir, storeFileNewName, leadBCardImgFile);
                } catch (IOException ex) {
                    logger.warn("LeadEvent Card file upload error {} ", ex.getMessage(), ex);
                    throw new RuntimeException("LeadEvent Card file upload error.");
                }
                leadEvent.setLeadBCardImg(storeFileNewName);
            }
    }

    public LeadEvent addNewLeadEvent(int lastLeadEventId) {
        String leadDummyEmail = "example" + (1 + lastLeadEventId) + "@example.com";
        LeadEvent leadEvent = new LeadEvent(
                "--UNKNOWN--",
                "--UNKNOWN--",
                leadDummyEmail,
                "0000000000",
                "--Company name--",
                LeadInterestLevel.MEDIUM.toString(),
                "",
                LeadState.PENDING.toString(),
                "",
                Instant.now(),
                Instant.now());
        return leadEventRepo.save(leadEvent);
    }

    public ResponseEntity<ApiMessage> deleteLeadEventById(Integer id) {
        leadEventRepo.findById(id).orElseThrow( () -> new ResourceNotFoundException(
                        "LeadEvent with ID " + id + " not found when deleting ",
                        "Could not delete this LeadEvent."
                ));
        leadEventRepo.deleteById(id);
        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Delete Success",
                "LeadEvent deleted successfully",
                Collections.emptyMap()));
    }
}