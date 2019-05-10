package com.kyunghwan.Person;

import org.springframework.stereotype.Controller;

@Controller
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void save(){
        personRepository.save();
    }
}
