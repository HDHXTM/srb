package lbw.srb.sms.receiver;


import lbw.srb.common.exception.Assert;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.common.util.RandomUtils;
import lbw.srb.common.util.RedisUtil;
import lbw.srb.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SmsReceiver {

    @Resource
    private SmsService smsService;


    @RabbitListener(queues = "smsQueue")
    public void send(String phone){
        log.info("收到发送信息请求");
        smsService.send(phone);
    }
}
