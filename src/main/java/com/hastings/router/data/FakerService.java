package com.hastings.router.data;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class FakerService {


    /**
     * Generates a basic username.
     *
     * @return the generated username
     */
    public String generate() {
        Faker faker = new Faker();
        return faker.superhero().prefix() + faker.name().firstName() + faker.address().buildingNumber();
    }

}
