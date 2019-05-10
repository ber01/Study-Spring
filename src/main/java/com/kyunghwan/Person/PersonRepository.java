package com.kyunghwan.Person;

import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {

    public void save() {
        System.out.println("PersonRepository save method 실행");
    }
}
