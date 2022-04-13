package io.rently.searchservice.interfaces;

import io.rently.searchservice.dtos.Listing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ListingsRepository extends MongoRepository<Listing, String> {
    String ADDRESS_QUERY = "{ $or: [ {'address.country': ?1}, {'address.city': ?2}, {'address.zip': ?3} ] }";
    String SEARCH_QUERY = "{ $text: { $search: ?0 } }";

    @Query("{ 'address.location': { $near: {$maxDistance: ?2, $geometry: { type: 'Point', coordinates: [?0, ?1]} } } }")
    List<Listing> findNearByGeoCode(Double lon, Double lat, Integer range, Pageable pageable);

    @Query(
            "{ $and: [" +
            "{ $text: { $search: ?0 } }, " +
            "{ $or: [{'address.country': ?1}, {'address.city': ?2}, {'address.zip': ?3}] }" +
            "] }"
    )
    List<Listing> findByAddress(String query, String country, String city, String zip, Pageable pageable);
}
