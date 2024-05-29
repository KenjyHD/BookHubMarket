package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.dto.PurchaseRequestPostDTO;
import com.kenjy.bookapi.entities.Books;
import com.kenjy.bookapi.entities.PurchaseRequest;
import com.kenjy.bookapi.entities.Users;
import com.kenjy.bookapi.enums.PurchaseRequestStatus;
import com.kenjy.bookapi.repository.BookRepository;
import com.kenjy.bookapi.repository.PurchaseRequestRepository;
import com.kenjy.bookapi.repository.UserRepository;
import com.kenjy.bookapi.service.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/purchase")
public class PurchaseRequestController {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestService purchaseRequestService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<?> createPurchaseRequest(@RequestBody PurchaseRequestPostDTO dto) {
        Optional<Users> user = userRepository.findById(dto.getUserId());
        Optional<Books> book = bookRepository.findById(dto.getBookId());

        if (user.isPresent() && book.isPresent()) {
            try {
                PurchaseRequest purchaseRequest = new PurchaseRequest(
                        user.get(),
                        book.get(),
                        PurchaseRequestStatus.PENDING,
                        LocalDateTime.now()
                );
                PurchaseRequest savedPurchaseRequest = purchaseRequestRepository.save(purchaseRequest);
                return ResponseEntity.ok(savedPurchaseRequest);
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("You have already requested this book for purchase.");
            }
        } else {
            return ResponseEntity.badRequest().body("Users or book not found.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPurchaseRequests() {
        return ResponseEntity.ok(purchaseRequestService.getAllPurchaseRequests());
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

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkPurchaseRequest(@RequestParam Long userId, @RequestParam Long bookId) {
        boolean exists = purchaseRequestRepository.existsByUserIdAndBookId(userId, bookId);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }
}
