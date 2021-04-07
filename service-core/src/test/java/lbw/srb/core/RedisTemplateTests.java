package lbw.srb.core;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lbw.srb.common.util.RedisUtil;
import lbw.srb.core.mapper.DictMapper;
import lbw.srb.core.mapper.UserInfoMapper;
import lbw.srb.core.pojo.entity.Dict;
import lbw.srb.core.pojo.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTemplateTests {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DictMapper dictMapper;

    @Test
    void aa(){
        redisUtil.del("userInfo::"+333333333);

    }
    @Test
    void vv(){
        UserInfo userInfo = userInfoMapper.selectById(1379409971531345922L);
        System.out.println(userInfo);
        redisUtil.set("userInfo::1379409971531345922",userInfo);

    }

    @Test
    void rr(){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.select("name");
        wrapper.inSql("parent_id","select id from dict where dict_code='industry'");
        List<Dict> dicts = dictMapper.selectList(wrapper);
        for (Dict dict : dicts) {
            System.out.println(dict);
        }
    }



}
