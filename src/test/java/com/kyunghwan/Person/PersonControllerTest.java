package com.kyunghwan.Person;

import org.junit.Test;

public class PersonControllerTest {

    @Test
    public void controllerTest(){
        PersonRepository personRepository = new PersonRepository();
        PersonController personController = new PersonController(personRepository);
        personController.save();
    }
}