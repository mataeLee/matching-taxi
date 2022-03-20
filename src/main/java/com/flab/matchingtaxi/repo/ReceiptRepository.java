package com.flab.matchingtaxi.repo;

import com.flab.matchingtaxi.model.Receipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, String> {
    Receipt findByPassengerId(String passengerId);
}
