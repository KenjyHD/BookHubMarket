package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.dto.AuthorRequestDTO;
import com.kenjy.bookapi.service.AuthorRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/author")
public class AuthorRequestController {
    private final AuthorRequestService authorRequestService;
    @GetMapping("/request")
    public ResponseEntity<AuthorRequestDTO> getPendingAuthorRequest(@RequestParam Long bookId) {
        return ResponseEntity.ok(authorRequestService.getPendingAuthorRequestByBookId(bookId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkPendingAuthorRequest(@RequestParam Long userId) {
        return ResponseEntity.ok(authorRequestService.checkPendingAuthorRequestByUserId(userId));
    }

    @PutMapping("/{requestId}/approve")
    public ResponseEntity<Void> approveAuthorRequest(@PathVariable Long requestId) {
        authorRequestService.approveAuthorRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectAuthorRequest(@PathVariable Long requestId) {
        authorRequestService.rejectAuthorRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
