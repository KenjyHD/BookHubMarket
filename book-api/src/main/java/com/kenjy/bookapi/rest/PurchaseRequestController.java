package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.dto.GetPurchaseRequestDTO;
import com.kenjy.bookapi.dto.PurchaseRequestPostDTO;
import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.entities.PurchaseRequest;
import com.kenjy.bookapi.entities.Users;
import com.kenjy.bookapi.enums.RequestStatus;
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
import java.util.*;

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
        Optional<Book> book = bookRepository.findById(dto.getBookId());

        if (user.isPresent() && book.isPresent()) {
            try {
                PurchaseRequest purchaseRequest = new PurchaseRequest(
                        user.get(),
                        book.get(),
                        RequestStatus.PENDING,
                        LocalDateTime.now()
                );
                purchaseRequestRepository.save(purchaseRequest);
                return ResponseEntity.ok().build();
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("You have already requested this book for purchase.");
            }
        } else {
            return ResponseEntity.badRequest().body("Users or book not found.");
        }
    }

    @GetMapping
    public ResponseEntity<List<GetPurchaseRequestDTO>> getAllPurchaseRequests() {
        List<GetPurchaseRequestDTO> purchaseRequests = purchaseRequestService.getAllPurchaseRequests();
        return ResponseEntity.ok(purchaseRequests);
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

    @GetMapping("/checkStatus")
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
