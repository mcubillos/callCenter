package com.example.callcenter.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class DispatcherTest {

    @Autowired
    Dispatcher dispatcher;

    @Value("${queue.name}")
    private String queueName;

    @Test
    public void dispatchCallTest() {

        try {
            for(int i=0; i < 10; i++){
                String id = UUID.randomUUID().toString();
                dispatcher.dispatchCall(queueName, id);
            }

            Thread.sleep(15000);

            Assert.assertEquals(dispatcher.pendingJobs(queueName), 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}