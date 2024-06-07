package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.dto.GetPurchaseRequestDTO;
import com.kenjy.bookapi.dto.PurchaseRequestPostDTO;
import com.kenjy.bookapi.dto.PurchaseRequestResponseDTO;
import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.enums.RequestStatus;
import com.kenjy.bookapi.repository.PurchaseRequestRepository;
import com.kenjy.bookapi.service.PurchaseRequestService;
import com.kenjy.bookapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/purchase")
public class PurchaseRequestController {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestService purchaseRequestService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<PurchaseRequestResponseDTO> createPurchaseRequest(@RequestBody PurchaseRequestPostDTO dto) {
        return ResponseEntity.ok(purchaseRequestService.createPurchaseRequest(dto));
    }

    @GetMapping
    public ResponseEntity<List<GetPurchaseRequestDTO>> getAllPurchaseRequests() {
        List<GetPurchaseRequestDTO> purchaseRequests = purchaseRequestService.getAllPurchaseRequests();
        return ResponseEntity.ok(purchaseRequests);
    }

    @GetMapping("/author")
    public ResponseEntity<List<GetPurchaseRequestDTO>> getPurchaseRequestsForAuthor(@AuthenticationPrincipal UserDetails userDetails) {
        User author = userService.findByUsername(userDetails.getUsername());
        if (author == null || !author.getRole().equals("AUTHOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(purchaseRequestService.findPurchaseRequestsByAuthor(author));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approvePurchaseRequest(@PathVariable Long id) {
        purchaseRequestService.approvePurchaseRequest(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectPurchaseRequest(@PathVariable Long id) {
        purchaseRequestService.rejectPurchaseRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-status")
    public ResponseEntity<Map<String, Object>> checkPurchaseRequest(@RequestParam Long userId, @RequestParam Long bookId) {
        Map<String, Object> response = new HashMap<>();
        boolean isOwned = purchaseRequestRepository.existsByUserIdAndBookIdAndStatus(userId, bookId, RequestStatus.APPROVED);
        boolean isRequested = purchaseRequestRepository.existsByUserIdAndBookId(userId, bookId);

        response.put("isOwned", isOwned);
        response.put("isRequested", isRequested);

        if (isRequested) {
            RequestStatus status = purchaseRequestRepository.findStatusByUserIdAndBookId(userId, bookId);
            response.put("requestStatus", status);
        }

        return ResponseEntity.ok(response);
    }
}
