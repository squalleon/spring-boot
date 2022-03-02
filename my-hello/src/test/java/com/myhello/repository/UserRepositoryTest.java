package com.myhello.repository;

import com.myhello.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.tools.JavaCompiler;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class UserRepositoryTest {
//    @Autowired
//    private UserRepository userRepository;

    @Test
    public void save() {

        Optional<String> opt = Optional.ofNullable(null);//자바 Optional 객체");
        if(opt.isPresent()) {
            System.out.println("=======> opt="+opt.get());
        }else{
            System.out.println("=======> opt is null");
        }

//        userRepository.save(new User("test1", "1234", "kim"));
//        Optional<User> selectedUser = userRepository.findById("test1");
//        assertEquals("test1", selectedUser.getId());

        /*User user = new User();
        user.setName("test");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name")
                .withIncludeNullValues();
//                .withStringMatcher();

        Example<User> example = Example.of(user, matcher);*/
    }
}