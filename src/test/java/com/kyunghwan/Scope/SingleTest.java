package com.kyunghwan.Scope;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SingleTest {

    @Autowired
    Single single;

    @Autowired
    Single single2;

    @Autowired
    Proto proto;

    @Autowired
    Proto proto2;

    @Test
    public void 싱글톤_테스트(){

        System.out.println(single);
        System.out.println(single2);

        assertThat(single).isEqualTo(single2);
    }

    @Test
    public void 프로토_테스트(){
        System.out.println(proto); // Proto@2dbd803f
        System.out.println(proto2); // Proto@3e48e859

        assertThat(proto).isNotEqualTo(proto2);
    }

    @Test
    public void 싱글톤이_프로토_참조(){
        System.out.println(single.getProto()); // Proto@78de58ea
        System.out.println(single.getProto()); // Proto@3a022576
    }
}