package com.kyunghwan.Student.A;

import org.springframework.stereotype.Controller;

@Controller
public class StudentAController {

    private final StudentARepository studentARepository;

    public StudentAController(StudentARepository studentARepository){
        this.studentARepository = studentARepository;
    }
}
