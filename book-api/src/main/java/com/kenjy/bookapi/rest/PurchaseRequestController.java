package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.dto.PurchaseRequestPostDTO;
import com.kenjy.bookapi.entities.Books;
import com.kenjy.bookapi.entities.PurchaseRequest;
import com.kenjy.bookapi.entities.Users;
import com.kenjy.bookapi.entities.UsersBooks;
import com.kenjy.bookapi.enums.PurchaseRequestStatus;
import com.kenjy.bookapi.repository.BookRepository;
import com.kenjy.bookapi.repository.PurchaseRequestRepository;
import com.kenjy.bookapi.repository.UserRepository;
import com.kenjy.bookapi.repository.UsersBooksRepository;
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
    private final UsersBooksRepository usersBooksRepository;
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
    public ResponseEntity<List<PurchaseRequest>> getAllPurchaseRequests() {
        List<PurchaseRequest> purchaseRequests = purchaseRequestRepository.findAll();
        return ResponseEntity.ok(purchaseRequests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseRequest> updatePurchaseRequestStatus(
            @PathVariable Long id,
            @RequestParam PurchaseRequestStatus status
    ) {
        Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(id);
        if (purchaseRequest.isPresent()) {
            PurchaseRequest pr = purchaseRequest.get();
            pr.setStatus(status);
            pr.setDecisionDate(LocalDateTime.now());
            PurchaseRequest updatedPurchaseRequest = purchaseRequestRepository.save(pr);

            if (status == PurchaseRequestStatus.APPROVED) {
                UsersBooks usersBooks = new UsersBooks(pr.getUser(), pr.getBook());
                usersBooksRepository.save(usersBooks);
            }

            return ResponseEntity.ok(updatedPurchaseRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkPurchaseRequest(@RequestParam Long userId, @RequestParam Long bookId) {
        boolean exists = purchaseRequestRepository.existsByUserIdAndBookId(userId, bookId);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }
}
