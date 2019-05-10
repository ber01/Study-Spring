package com.kyunghwan.Board;

import org.junit.Test;

public class BoardControllerTest {

    @Test
    public void controllerTest(){
        BoardController boardController = new BoardController();
        boardController.save();
    }
}