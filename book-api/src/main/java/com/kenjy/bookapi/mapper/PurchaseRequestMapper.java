package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.dto.GetPurchaseRequestDTO;
import com.kenjy.bookapi.dto.PurchaseRequestResponseDTO;
import com.kenjy.bookapi.entities.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseRequestMapper {

    public PurchaseRequestResponseDTO toPurchaseRequestResponseDTO(PurchaseRequest pr) {
        return new PurchaseRequestResponseDTO(
                pr.getId(),
                pr.getUser().getId(),
                pr.getBook().getId(),
                pr.getStatus(),
                pr.getRequestDate()
        );
    }

    public GetPurchaseRequestDTO toGetPurchaseRequestDTO(PurchaseRequest pr) {
        return new GetPurchaseRequestDTO(pr.getId(), pr.getUser().getUsername(), pr.getBook().getTitle(), pr.getStatus());
    }
}
