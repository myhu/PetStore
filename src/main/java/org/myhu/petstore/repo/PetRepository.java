package org.myhu.petstore.repo;

import org.myhu.petstore.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer>{
}
