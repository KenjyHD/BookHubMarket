package com.kenjy.bookapi.service;

import com.kenjy.bookapi.dto.GetPurchaseRequestDTO;
import com.kenjy.bookapi.dto.PurchaseRequestPostDTO;
import com.kenjy.bookapi.dto.PurchaseRequestResponseDTO;
import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.entities.PurchaseRequest;
import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.entities.UsersBooks;
import com.kenjy.bookapi.enums.RequestStatus;
import com.kenjy.bookapi.exception.DuplicatePurchaseRequestForUserException;
import com.kenjy.bookapi.mapper.PurchaseRequestMapper;
import com.kenjy.bookapi.repository.PurchaseRequestRepository;
import com.kenjy.bookapi.repository.UsersBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final UsersBooksRepository usersBooksRepository;
    private final UserService userService;
    private final BookService bookService;
    private final PurchaseRequestMapper purchaseRequestMapper;

    public List<GetPurchaseRequestDTO> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll().stream()
                .map(purchaseRequestMapper::toGetPurchaseRequestDTO)
                .collect(Collectors.toList());
    }

    public void approvePurchaseRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        request.setStatus(RequestStatus.APPROVED);
        request.setDecisionDate(LocalDateTime.now());
        purchaseRequestRepository.save(request);

        UsersBooks usersBooks = new UsersBooks();
        usersBooks.setUser(request.getUser());
        usersBooks.setBook(request.getBook());
        usersBooksRepository.save(usersBooks);
    }

    public void rejectPurchaseRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        request.setStatus(RequestStatus.REJECTED);
        request.setDecisionDate(LocalDateTime.now());
        purchaseRequestRepository.save(request);
    }

    public PurchaseRequestResponseDTO createPurchaseRequest(PurchaseRequestPostDTO dto) {
        if (purchaseRequestRepository.existsByUserIdAndBookId(dto.getUserId(), dto.getBookId())) {
            throw new DuplicatePurchaseRequestForUserException(String.format("User with id {%d} has already requested purchase for book with id {%d}", dto.getUserId(), dto.getBookId()));
        }

        User user = userService.findUserById(dto.getUserId());
        Book book = bookService.findBookById(dto.getBookId());

        PurchaseRequest purchaseRequest = new PurchaseRequest(
                user,
                book,
                RequestStatus.PENDING,
                LocalDateTime.now()
        );
        PurchaseRequest pr = purchaseRequestRepository.save(purchaseRequest);
        return purchaseRequestMapper.toPurchaseRequestResponseDTO(pr);
    }

    public List<GetPurchaseRequestDTO> findPurchaseRequestsByAuthor(User author) {
        List<PurchaseRequest> requests = purchaseRequestRepository.findAllByBookAuthorId(author.getId());
        return requests.stream().map(purchaseRequestMapper::toGetPurchaseRequestDTO).collect(Collectors.toList());
    }
}
