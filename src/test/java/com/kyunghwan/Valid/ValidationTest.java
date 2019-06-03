package com.kyunghwan.Valid;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationTest {

    @Autowired
    private Validator validator;

    @Test
    public void 유효성_테스트(){
        Event event = new Event();
        EventValidator eventValidator = new EventValidator();
        Errors errors = new BeanPropertyBindingResult(event, "event");

        eventValidator.validate(event, errors);

        System.out.println(errors.hasErrors());

        errors.getAllErrors().forEach(e -> {
            System.out.println(Arrays.toString(e.getCodes()));
            System.out.println(e.getDefaultMessage());
        });
    }

    @Test
    public void 유효성_테스트_어노테이션(){

        System.out.println(validator.getClass());

        EventA event = new EventA();
        Errors errors = new BeanPropertyBindingResult(event, "event");

        validator.validate(event, errors);

        System.out.println(errors.hasErrors());

        errors.getAllErrors().forEach(e -> {
            System.out.println(Arrays.toString(e.getCodes()));
            System.out.println(e.getDefaultMessage());
        });
    }
}