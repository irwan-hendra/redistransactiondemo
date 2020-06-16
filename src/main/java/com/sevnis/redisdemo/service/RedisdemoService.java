package com.sevnis.redisdemo.service;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedisdemoService {


  private static final String KEY = "test:redis:data";

  private final RedisTemplate<String, Integer> redisTemplate;


  public void sendRequest(String id) {
    //Object count = redisTemplate.opsForHash().get(KEY, id);
    redisTemplate.opsForHash().increment(KEY, id, 1);
    //redisTemplate.opsForHash().put(KEY, id, count == null ? new Integer(0) : (((Integer)count) + 1));

    ///TEMP: figure out redis transaction
//    redisTemplate.execute(new SessionCallback<Object>() {
//      public List<Object> execute(RedisOperations operations) throws DataAccessException {
//        operations.multi();
//        Object count = operations.opsForHash().get(KEY, "test2");
//        operations.opsForHash().put(KEY, "test2", count == null ? new Integer(0) : (((Integer)count) + 1));
//        return operations.exec();
//      }
//    });
  }

  public void processRequest() {

    // redis way of transaction
    redisTemplate.multi();
    redisTemplate.opsForHash().entries(KEY);
    redisTemplate.delete(KEY);
    List<Object> resultList = redisTemplate.exec();

    if (resultList.get(0) instanceof Map) {
      Map<String, String> entries = (Map<String, String>) resultList.get(0);
      int total = 0;
      if (entries != null && entries.isEmpty() == false) {
        for (Entry<String, String> entry : entries.entrySet()) {
          System.out.println(entry.getKey() + "<=>" + entry.getValue());
          total += Integer.valueOf(entry.getValue());
        }
      }
      System.out.println(total);
    }
  }
}
