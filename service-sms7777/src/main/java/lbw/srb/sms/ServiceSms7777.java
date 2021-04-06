package lbw.srb.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("lbw.srb")
public class ServiceSms7777 {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSms7777.class, args);
    }
}