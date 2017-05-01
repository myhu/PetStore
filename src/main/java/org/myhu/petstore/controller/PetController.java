package org.myhu.petstore.controller;

import org.myhu.petstore.model.Pet;
import org.myhu.petstore.repo.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {

    @Autowired
    PetRepository petRepository;


    @RequestMapping(value="/item", method=RequestMethod.GET)
    public List<Pet> getPet() {

        return petRepository.findAll();
    }

    @RequestMapping(value="/item/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id) {
        this.petRepository.delete(id);
        return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
    }

    @RequestMapping(value="/item", method=RequestMethod.POST)
    public ResponseEntity<Boolean> addPet(@RequestBody Pet pet) {
        this.petRepository.save(pet);
        return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.CREATED);
    }
}
