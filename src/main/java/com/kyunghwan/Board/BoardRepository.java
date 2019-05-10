package com.kyunghwan.Board;

import org.springframework.stereotype.Repository;

@Repository
public class BoardRepository {

    public void save() {
        System.out.println("BoardRepository save method 실행");
    }
}
