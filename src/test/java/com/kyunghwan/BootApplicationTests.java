package com.kyunghwan;

import com.kyunghwan.Board.BoardController;
import com.kyunghwan.Board.BoardRepository;
import com.kyunghwan.Person.PersonController;
import com.kyunghwan.Person.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootApplicationTests {

    @Autowired
    ApplicationContext context;

    @Test
    public void confirmBeans(){
        String[] beans = context.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beans));
    }

    @Test
    public void getBean(){
        BoardController boardController = (BoardController) context.getBean("boardController");
        BoardRepository boardRepository = context.getBean(BoardRepository.class);

        PersonController personController = context.getBean(PersonController.class);
        PersonRepository personRepository = (PersonRepository) context.getBean("personRepository");

        assertThat(boardController).isNotNull();
        assertThat(boardRepository).isNotNull();
        assertThat(personController).isNotNull();
        assertThat(personRepository).isNotNull();
    }
}
