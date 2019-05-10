package com.kyunghwan.Student.C;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StudentCController {

    private StudentCRepository studentCRepository;

    @Autowired
    public void setStudentCRepository(StudentCRepository studentCRepository) {
        this.studentCRepository = studentCRepository;
    }
}
