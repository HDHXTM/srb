package lbw.srb.rabbitMQ.config;

import lbw.srb.common.constant.Constant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {



    @Bean
    public Queue smsQueue(){
        return QueueBuilder.durable(Constant.SMS_QUEUE).build();
    }

    @Bean
    public Exchange smsExchange(){
        return ExchangeBuilder.topicExchange(Constant.SMS_EXCHANGE).durable(true).build();
    }
    @Bean
    public Binding bindEQ(@Qualifier(Constant.SMS_QUEUE) Queue queue,
                           @Qualifier(Constant.SMS_EXCHANGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(Constant.SMS_ROUTINGKEY).noargs();
    }
}
