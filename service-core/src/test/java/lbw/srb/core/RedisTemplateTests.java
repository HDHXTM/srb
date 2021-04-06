package lbw.srb.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTemplateTests {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void aa(){
        Set<String> keys = redisTemplate.keys("dict::*");
        redisTemplate.delete(keys);
    }



}
