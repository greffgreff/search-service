package io.rently.searchservice.interfaces;

import io.rently.searchservice.dtos.Image;
import io.rently.searchservice.dtos.Listing;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends MongoRepository<Image, String> {

    @Query("{ '_id' : ?0 }")
    Optional<Image> queryById(String id);

}
