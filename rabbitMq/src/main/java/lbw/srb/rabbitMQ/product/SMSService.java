package lbw.srb.rabbitMQ.product;

import lbw.srb.common.constant.Constant;
import lbw.srb.common.entity.Message;
import lbw.srb.rabbitMQ.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SMSService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendRegisterMessage(String phone){

        log.info("发送注册短信{}",phone);
        rabbitTemplate.convertAndSend(Constant.SMS_EXCHANGE,"sms",phone);
    }

}
