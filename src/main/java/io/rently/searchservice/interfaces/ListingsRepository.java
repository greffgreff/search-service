package io.rently.searchservice.interfaces;

import io.rently.searchservice.dtos.Listing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ListingsRepository extends MongoRepository<Listing, String> {

    @Query("{ $text: { $search: ?0 } }")
    List<Listing> query(String query, Pageable pageable);

    @Query("{ 'address.location': { $near: {$maxDistance: ?2, $geometry: { type: 'Point', coordinates: [?0, ?1]} } } }")
    List<Listing> queryAnyNearbyGeoCode(Double lon, Double lat, Integer range, Pageable pageable);

    @Query("{ $and: [" +
            "{ $text: { $search: ?0 } }, " +
            "{ 'address.location': { $near: {$maxDistance: ?3, $geometry: { type: 'Point', coordinates: [?1, ?2]} } } }" +
            "] }")
    List<Listing> queryNearbyGeoCode(String query, Double lon, Double lat, Integer range, Pageable pageable);

    @Query("{ $or: [ {'address.country': ?1}, {'address.city': ?2}, {'address.zip': ?3} ] }")
    List<Listing> queryAnyAtAddress(String country, String city, String zip, Pageable pageable);

    @Query("{ $and: [" +
            "{ $text: { $search: ?0 } }, " +
            "{ $or: [ {'address.country': ?1}, {'address.city': ?2}, {'address.zip': ?3} ] }" +
            "] }")
    List<Listing> queryAtAddress(String query, String country, String city, String zip, Pageable pageable);
}
