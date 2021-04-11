package lbw.srb.core;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lbw.srb.common.util.RedisUtil;
import lbw.srb.core.enums.BorrowInfoStatusEnum;
import lbw.srb.core.mapper.*;
import lbw.srb.core.pojo.entity.*;
import lbw.srb.core.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Autowired
    private BorrowInfoMapper borrowInfoMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private IntegralGradeMapper integralGradeMapper;

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
    @Test
    void ww(){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.select("name");
        wrapper.eq("value",3);
        wrapper.inSql("parent_id","select id from dict where dict_code='industry'");
        List<Object> objects = dictMapper.selectObjs(wrapper);
        System.out.println(objects.get(0));
    }

    @Test
    void ee(){
        UserInfo userInfo = (UserInfo) redisUtil.get("userInfo::1379409971531345922");
        System.out.println(userInfo);
    }

    @Test
    void d(){
        QueryWrapper<BorrowInfo> wrapper = new QueryWrapper<>();
        wrapper
                .select("status")
                .eq("user_id",1379409971531345924L)
                .last("and user_id !="+ BorrowInfoStatusEnum.FINISH.getStatus());
        List<Object> list = borrowInfoMapper.selectObjs(wrapper);
        for (Object o : list) {
            System.out.println(o);
        }
    }

    @Test
    void df(){
        UpdateWrapper<UserAccount> wrapper = new UpdateWrapper<>();
//        UserAccount userAccount = new UserAccount();
//        userAccount.setId(1379409971615232002L);
//        userAccount.setUserId(1379409971531345925L);
//        System.out.println(userAccount);
        wrapper.setSql("amount=amount+"+999).eq("user_id",1379409971531345925L);
        userAccountMapper.update(null,wrapper);
    }

    @Test
    void ll(){
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id","name").eq("bind_code","65cf69dd91734924b43ee251f6a51a77");
        List<Object> objs = userInfoMapper.selectObjs(wrapper);
        for (Object obj : objs) {
            System.out.println(obj);
        }
    }

@Test
    void ese(){
//    IntegralGrade grade = new IntegralGrade();
//    grade.setBorrowAmount(new BigDecimal(5335555));
//    integralGradeMapper.insert(grade);

    IntegralGrade integralGrade = integralGradeMapper.selectById(1381065416822026241L);
    integralGrade.setUpdateTime(null);
    integralGradeMapper.updateById(integralGrade);
}


}
