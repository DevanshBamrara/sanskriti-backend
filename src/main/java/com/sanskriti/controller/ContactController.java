package com.sanskriti.controller;

import com.sanskriti.dto.ApiResponse;
import com.sanskriti.dto.ContactRequest;
import com.sanskriti.model.ContactMessage;
import com.sanskriti.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @PostMapping
    public ApiResponse<ContactMessage> submitMessage(@RequestBody ContactRequest request) {
        ContactMessage message = ContactMessage.builder()
                .name(request.getName())
                .email(request.getEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .build();
        
        return new ApiResponse<>(true, "Message sent successfully", contactMessageRepository.save(message));
    }

    @GetMapping
    public ApiResponse<List<ContactMessage>> getAllMessages() {
        return new ApiResponse<>(true, "Messages retrieved", contactMessageRepository.findAllByOrderByCreatedAtDesc());
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<ContactMessage> markAsRead(@PathVariable Long id) {
        return contactMessageRepository.findById(id)
                .map(msg -> {
                    msg.setIsRead(true);
                    return new ApiResponse<>(true, "Marked as read", contactMessageRepository.save(msg));
                })
                .orElse(new ApiResponse<>(false, "Message not found", null));
    }
}
