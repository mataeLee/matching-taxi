package com.flab.matchingtaxi.service;

import com.flab.matchingtaxi.domain.CallRequest;
import com.flab.matchingtaxi.model.Receipt;
import com.flab.matchingtaxi.repo.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;

    public void saveReceiptByCallRequest(CallRequest request) {
        if(receiptRepository.findByPassengerId(request.getPassenger()) == null) {
            Receipt receipt = Receipt.builder()
                    .passengerId(request.getPassenger())
                    .taxiId("")
                    .startLat(request.getStart().getX())
                    .startLng(request.getStart().getY())
                    .endLat(request.getEnd().getX())
                    .endLng(request.getEnd().getY())
                    .build();
            receiptRepository.save(receipt);
        }
    }

    public Receipt findReceiptByPassengerId(String passengerId) {
        return receiptRepository.findByPassengerId(passengerId);
    }

    public void saveReceipt(Receipt receipt) {
        receiptRepository.save(receipt);
    }
}
