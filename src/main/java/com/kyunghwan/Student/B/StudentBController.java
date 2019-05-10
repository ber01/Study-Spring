package com.kyunghwan.Student.B;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StudentBController {

    @Autowired
    StudentBRepository studentBRepository;
}
