package com.kyunghwan.Student;

import com.kyunghwan.Student.A.StudentAController;
import com.kyunghwan.Student.B.StudentBController;
import com.kyunghwan.Student.C.StudentCController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentControllerTest {

    @Autowired
    StudentAController studentAController;

    @Autowired
    StudentBController studentBController;

    @Autowired
    StudentCController studentCController;

    @Test
    public void dIConstructor(){
        assertThat(studentAController).isNotNull();
    }

    @Test
    public void dIField(){
        assertThat(studentBController).isNotNull();
    }

    @Test
    public void dISetter(){
        assertThat(studentCController).isNotNull();
    }
}