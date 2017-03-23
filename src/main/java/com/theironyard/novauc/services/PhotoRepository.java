package com.theironyard.novauc.services;

import com.theironyard.novauc.entities.Photo;
import com.theironyard.novauc.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by psubedi2020 on 3/21/17.
 */
public interface PhotoRepository extends CrudRepository<Photo, Integer> {
    List<Photo> findByRecipient(User name);
    Photo findFirstByFilename(String filename);
    Photo findFirstById(int id);
}

