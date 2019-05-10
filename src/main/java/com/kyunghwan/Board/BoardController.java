package com.kyunghwan.Board;

import org.springframework.stereotype.Controller;

@Controller
public class BoardController {

    private final BoardRepository boardRepository = new BoardRepository();

    public void save(){
        boardRepository.save();
    }
}
