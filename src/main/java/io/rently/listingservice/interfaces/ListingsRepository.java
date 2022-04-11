package io.rently.listingservice.interfaces;

import io.rently.listingservice.models.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ListingsRepository extends MongoRepository<Listing, String> {

    @Query("{'id' : ?0}")
    Optional<Listing> findById(String id);
}
