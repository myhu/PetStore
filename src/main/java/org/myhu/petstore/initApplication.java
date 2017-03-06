package org.myhu.petstore;

import org.myhu.petstore.model.Pet;
import org.myhu.petstore.repo.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class initApplication {

	public static void main(String[] args) {
		SpringApplication.run(initApplication.class, args);
	}

	@Autowired
	PetRepository petRepository;

	@Bean
	public String initTemplate()
	{
		petRepository.save(createPet());

		return "";
	}

	private Pet createPet() {
		Pet pet = new Pet();
		pet.setName("myhu");

		return pet;
	}

}
