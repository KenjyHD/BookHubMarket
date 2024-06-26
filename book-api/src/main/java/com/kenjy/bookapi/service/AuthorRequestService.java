package com.kenjy.bookapi.service;

import com.kenjy.bookapi.dto.AuthorRequestDTO;
import com.kenjy.bookapi.dto.GetAuthorRequestDTO;
import com.kenjy.bookapi.entities.AuthorRequest;
import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.enums.RequestStatus;
import com.kenjy.bookapi.mapper.AuthorRequestMapper;
import com.kenjy.bookapi.repository.AuthorRequestRepository;
import com.kenjy.bookapi.repository.BookRepository;
import com.kenjy.bookapi.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorRequestService {
    private final AuthorRequestRepository authorRequestRepository;
    private final AuthorRequestMapper authorRequestMapper;
    private final BookRepository bookRepository;
    private final UserService userService;

    public List<GetAuthorRequestDTO> getAllAuthorRequests() {
        List<AuthorRequest> authorRequest = authorRequestRepository.findAll();

        return authorRequest.stream().map(authorRequestMapper::toGetAuthorRequestDTO).collect(Collectors.toList());
    }

    public AuthorRequestDTO getPendingAuthorRequestByBookId(Long bookId) {
        Optional<AuthorRequest> authorRequest = authorRequestRepository.findByBookIdAndStatus(bookId, RequestStatus.PENDING);
        if (authorRequest.isEmpty()) {
            return null;
        }

        AuthorRequestDTO dto = new AuthorRequestDTO();
        dto.setId(authorRequest.get().getId());
        dto.setUserId(authorRequest.get().getUser().getId());
        dto.setBookId(authorRequest.get().getBook().getId());
        dto.setStatus(authorRequest.get().getStatus());
        return dto;
    }

    public boolean checkPendingAuthorRequestByUserId(Long userId) {
        return authorRequestRepository.existsByUserIdAndStatus(userId, RequestStatus.PENDING);
    }

    public void saveAuthorRequest(AuthorRequest authorRequest) {
        authorRequestRepository.save(authorRequest);
    }

    public void approveAuthorRequest(Long requestId) {
        AuthorRequest authorRequest = authorRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Author request not found"));

        User user = authorRequest.getUser();
        Book book = authorRequest.getBook();

        user.setRole(WebSecurityConfig.AUTHOR);
        User savedUser = userService.saveUser(user);

        book.setAuthor(savedUser);
        bookRepository.save(book);

        authorRequest.setStatus(RequestStatus.APPROVED);
        authorRequest.setDecisionDate(LocalDateTime.now());
        authorRequestRepository.save(authorRequest);
    }

    public void rejectAuthorRequest(Long requestId) {
        AuthorRequest authorRequest = authorRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Author request not found"));

        Book book = authorRequest.getBook();
        authorRequest.setBook(null);
        bookRepository.delete(book);

        authorRequest.setStatus(RequestStatus.REJECTED);
        authorRequest.setDecisionDate(LocalDateTime.now());
        authorRequestRepository.save(authorRequest);
    }

    public boolean existsAuthorRequestByBookIdAndStatus(Long bookId, RequestStatus status) {
        return authorRequestRepository.existsByBookIdAndStatus(bookId, status);
    }
}
