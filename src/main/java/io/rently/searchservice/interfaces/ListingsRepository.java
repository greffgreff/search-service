package io.rently.searchservice.interfaces;

import io.rently.searchservice.dtos.Listing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ListingsRepository extends MongoRepository<Listing, String> {

    @Query("{'address.location': {$near: {$maxDistance: ?2, $geometry: {type: 'Point', coordinates: [?0, ?1]}}}}")
    List<Listing> findNearByGeoCode(Double lon, Double lat, Integer range, Pageable pageable);

    @Query("{ or: [ {'address.country': ?0}, {'address.city': ?1}, {'address.zip': ?2} ] }")
    List<Listing> findByAddress(String country, String city, String zip, Pageable pageable);
}
