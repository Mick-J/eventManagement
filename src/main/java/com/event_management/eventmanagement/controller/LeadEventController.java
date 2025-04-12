package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.LeadEvent;
import com.event_management.eventmanagement.model.LeadEventDTO;
import com.event_management.eventmanagement.repository.LeadEventRepository;
import com.event_management.eventmanagement.service.LeadService;
import com.event_management.eventmanagement.utils.FileUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/leadEvent")
public class LeadEventController {

    private final LeadEventRepository leadEventRepo;
    private final LeadService leadService;

    private static final Logger logger = LogManager.getLogger(LeadEventController.class);

    public LeadEventController(LeadEventRepository leadEventRepo, LeadService leadService) {
        this.leadEventRepo = leadEventRepo;
        this.leadService = leadService;
    }

    @GetMapping({"", "/"})
    public String getLeadEventList(Model model) {
        List<LeadEvent> leadEventList = leadService.getLeadList();
        model.addAttribute("leadEventList", leadEventList);
        return "leadEvent/index";
    }

    @GetMapping("/edit")
    public String editLeadEvent(Model model, @RequestParam Integer id) {
        LeadEvent leadEvent = leadEventRepo.findById(id).orElse(null);

        if (leadEvent == null) {
            logger.info("Edit Lead with id={} not found", id);
            return "leadEvent/index";
        }

        try {
            SanitizationUtils.sanitizeObjectFields(leadEvent, "unescape");
            LeadEventDTO leadEventDTO = new LeadEventDTO();
            leadEventDTO.setLeadFirstName(leadEvent.getLeadFirstname());
            leadEventDTO.setLeadLastName(leadEvent.getLeadLastname());
            leadEventDTO.setLeadEmail(leadEvent.getLeadEmail());
            leadEventDTO.setLeadPhone(leadEvent.getLeadPhone());
            leadEventDTO.setLeadCompanyName(leadEvent.getLeadCompanyName());
            leadEventDTO.setLeadInterestLevel(leadEvent.getLeadInterestLevel());
            leadEventDTO.setComment(leadEvent.getComment());
            leadEventDTO.setLeadState(leadEvent.getLeadState());
            leadEventDTO.setLeadBCardImg(leadEvent.getLeadBCardImg());

            model.addAttribute("leadEventDTO", leadEventDTO);
            model.addAttribute("id", id);

            return "leadEvent/edit";
        } catch (Exception e) {
            logger.info("Error while unescape lead data html tags {} ", e.getMessage());
        }
        return "/leadEvent/index";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editLeadEvent(@RequestParam Integer id,
                                      @RequestParam MultipartFile leadBCardImgFile,
                                      @Valid @ModelAttribute LeadEventDTO leadEventDTO,
                                      BindingResult result)
    {
        String objectName = "Lead";
        LeadEvent leadEvent = leadEventRepo.findById(id).orElse(null);
        if (leadEvent == null) {
            Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                    objectName + " not found");
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

        try {
            //
            leadEvent.setLeadFirstname(leadEventDTO.getLeadFirstName());
            leadEvent.setLeadLastname(leadEventDTO.getLeadLastName());
            leadEvent.setLeadEmail(leadEventDTO.getLeadEmail());
            leadEvent.setLeadPhone(leadEventDTO.getLeadPhone());
            leadEvent.setLeadCompanyName(leadEventDTO.getLeadCompanyName());
            leadEvent.setLeadInterestLevel(leadEventDTO.getLeadInterestLevel());
            leadEvent.setComment(leadEventDTO.getComment());
            leadEvent.setLeadState(leadEventDTO.getLeadState());
            leadEvent.setUpdatedAt(Instant.now());
            if (FileUtils.isFileSent(leadBCardImgFile)) {
                // file rename
                String filename = leadBCardImgFile.getOriginalFilename();
                if (filename != null) {
                    String fileExtension = filename.substring(filename.lastIndexOf('.'));
                    String storeFileNewName = leadEvent.getLeadFirstname() + "_" + leadEvent.getLeadCompanyName() + "_card" + fileExtension;

                    String uploadDir = "uploads/leadEvent/";
                    FileUtils.uploadFile(uploadDir, storeFileNewName, leadBCardImgFile);
                    leadEvent.setLeadBCardImg(storeFileNewName);
                }
            }
            SanitizationUtils.sanitizeObjectFields(leadEvent, "escape");
            leadEventRepo.save(leadEvent);
            Message message = FormMessage.withMessage(null, "success", objectName + " update",
                    objectName + " updated successfully!");
            return ResponseEntity.ok(message);
        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getRootCause();
            String errorMessage = (rootCause != null) ? rootCause.getMessage() : "Unknown SQL Error";
            logger.error("SQL Error: {}", errorMessage);
        } catch (Exception ex) {
            logger.info("Error while saving lead {} ", ex.getMessage());
        }
        Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                objectName + " not updated");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @GetMapping("/display")
    public String displayLeadEvent(Model model, @RequestParam Integer id) {
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

    @GetMapping("/add")
    public RedirectView addLeadEvent(Model model, RedirectAttributes attributes) {

        String leadDummyEmail = "example" + (1 + getLastLeadEventId()) + "@example.com";
        LeadEvent leadEvent = new LeadEvent(
                "--UNKNOWN--", "--UNKNOWN--", leadDummyEmail, "0000000000",
                "--Company name--", "Medium", "", "Pending", "",
                Instant.now(), Instant.now());

        try {
            LeadEvent newLeadEvent = leadEventRepo.save(leadEvent);

            LeadEventDTO leadEventDTO = new LeadEventDTO();
            leadEventDTO.setLeadFirstName(leadEvent.getLeadFirstname());
            leadEventDTO.setLeadLastName(leadEvent.getLeadLastname());
            leadEventDTO.setLeadEmail(leadEvent.getLeadEmail());
            leadEventDTO.setLeadPhone(leadEvent.getLeadPhone());
            leadEventDTO.setLeadCompanyName(leadEvent.getLeadCompanyName());
            leadEventDTO.setLeadInterestLevel(leadEvent.getLeadInterestLevel());
            leadEventDTO.setComment(leadEvent.getComment());
            leadEventDTO.setLeadState(leadEvent.getLeadState());

            attributes.addAttribute("id", newLeadEvent.getId());
            model.addAttribute("leadEventDTO", leadEventDTO);

            return new RedirectView("/leadEvent/edit");
        } catch (Exception ex) {
            logger.info("Something went wrong when adding new lead");
        }
        return new RedirectView("/leadEvent/index");
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<Message> deleteLeadEvent(@RequestParam Integer id) {
        try {
            leadEventRepo.deleteById(id);
            Message message = new Message.Builder()
                    .withMessageType("success")
                    .withMessageHeader("Delete Success")
                    .withMessageBody("Lead deleted successfully!")
                    .build();
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            Message message = new Message.Builder()
                    .withMessageType("error")
                    .withMessageHeader("Something went wrong")
                    .withMessageBody("Lead not deleted")
                    .build();
            logger.info("Something went wrong when deleting lead event with id = {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    private int getLastLeadEventId() {
        LeadEvent lastLeadEvent = leadEventRepo.findTopByOrderByIdDesc();
        return (lastLeadEvent != null) ? lastLeadEvent.getId() : 1;
    }

}