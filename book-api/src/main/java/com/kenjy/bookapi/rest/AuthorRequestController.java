package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.config.SwaggerConfig;
import com.kenjy.bookapi.dto.AuthorRequestDTO;
import com.kenjy.bookapi.dto.GetAuthorRequestDTO;
import com.kenjy.bookapi.service.AuthorRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/author")
public class AuthorRequestController {
    private final AuthorRequestService authorRequestService;

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/requests")
    public ResponseEntity<List<GetAuthorRequestDTO>> getAllAuthorRequests() {
        return ResponseEntity.ok(authorRequestService.getAllAuthorRequests());
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/request")
    public ResponseEntity<AuthorRequestDTO> getPendingAuthorRequest(@RequestParam Long bookId) {
        return ResponseEntity.ok(authorRequestService.getPendingAuthorRequestByBookId(bookId));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkPendingAuthorRequest(@RequestParam Long userId) {
        return ResponseEntity.ok(authorRequestService.checkPendingAuthorRequestByUserId(userId));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<Void> approveAuthorRequest(@PathVariable Long requestId) {
        authorRequestService.approveAuthorRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectAuthorRequest(@PathVariable Long requestId) {
        authorRequestService.rejectAuthorRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
