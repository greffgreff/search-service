package io.rently.searchservice.controllers;

import io.rently.searchservice.dtos.Image;
import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.exceptions.Errors;
import io.rently.searchservice.interfaces.ImageRepository;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController { // FIXME move to new service

    @Autowired
    public ImageRepository repository;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] handleGetImage(@PathVariable String id) {
        Broadcaster.info("Fetching image by id: " + id);
        Optional<Image> data = repository.queryById(id);
        if (data.isPresent()) {
            Image image = data.get();
            return  Base64.getDecoder().decode(image.dataUrl);
        }
        throw Errors.NO_IMAGE;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{id}")
    public ResponseContent handlePostImage(@PathVariable String id, @RequestBody String data) {
        Broadcaster.info("Adding image by id: " + id);
        Image image = new Image(id, data);
        repository.save(image);
        return new ResponseContent.Builder(HttpStatus.CREATED).setMessage("Successfully added image to database").build();
    }

    @PutMapping(value = "/{id}")
    public ResponseContent handlePutImage(@PathVariable String id, @RequestBody String data) {
        Broadcaster.info("Updating image by id: " + id);
        Image image = new Image(id, data);
        repository.deleteById(id);
        repository.save(image);
        return new ResponseContent.Builder().setMessage("Successfully updated image in database").build();
    }

    @DeleteMapping("/{id}")
    public ResponseContent handleDeleteImage(@PathVariable String id) {
        Broadcaster.info("Deleting image by id: " + id);
        repository.deleteById(id);
        return new ResponseContent.Builder().setMessage("Successfully removed image from database").build();
    }
}
