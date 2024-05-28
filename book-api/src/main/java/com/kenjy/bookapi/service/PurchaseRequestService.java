package com.kenjy.bookapi.service;

import com.kenjy.bookapi.entities.PurchaseRequest;
import com.kenjy.bookapi.enums.PurchaseRequestStatus;
import com.kenjy.bookapi.repository.PurchaseRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepository;

    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll();
    }

    public void approvePurchaseRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        request.setStatus(PurchaseRequestStatus.APPROVED);
        request.setDecisionDate(LocalDateTime.now());
        purchaseRequestRepository.save(request);
    }

    public void rejectPurchaseRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        request.setStatus(PurchaseRequestStatus.REJECTED);
        request.setDecisionDate(LocalDateTime.now());
        purchaseRequestRepository.save(request);
    }
}
