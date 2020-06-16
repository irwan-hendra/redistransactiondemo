package com.sevnis.redisdemo.service;


import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisdemoServiceTest {

  @Autowired
  private RedisdemoService redisdemoService;

  @Test
  public void testMultiInvocations() throws Exception {

    Random random = new Random();
    for(int i = 0; i < 10000; i++) {

      String id = "fakeId" + random.nextInt(10);
      redisdemoService.sendRequest(id);
    }

    Thread.sleep(30000);
    redisdemoService.processRequest();
  }
}
