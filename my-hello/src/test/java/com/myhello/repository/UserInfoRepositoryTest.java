package com.myhello.repository;

import com.myhello.domain.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoRepositoryTest {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Test
    public void save() {
        userInfoRepository.save(new UserInfo("test", "Seoul", "010-1234-5678"));
        UserInfo selectedUserInfo = userInfoRepository.getById("test");
        assertEquals("test", selectedUserInfo.getId());
    }
}