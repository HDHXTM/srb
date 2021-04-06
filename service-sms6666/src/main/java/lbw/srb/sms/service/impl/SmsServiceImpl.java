package lbw.srb.sms.service.impl;

import lbw.srb.common.util.RandomUtils;
import lbw.srb.common.util.RedisUtil;
import lbw.srb.sms.service.SmsService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Value("${txSms.secretId}")
    private String secretId;
    @Value("${txSms.secretKey}")
    private String secretKey;
    @Value("${txSms.templateID}")
    private String templateID;
    @Value("${txSms.sign}")
    private String sign;
    @Value("${txSms.smsSdkAppid}")
    private String smsSdkAppid;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void send(String phone) {
        try{
            String code = RandomUtils.getFourBitRandom();
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {"+86"+phone};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setTemplateID(templateID);
            req.setSign(sign);

            String[] templateParamSet1 = {code, "3"};
            req.setTemplateParamSet(templateParamSet1);

            req.setSmsSdkAppid(smsSdkAppid);

//            client.SendSms(req);
            //将验证码存入redis
            redisUtil.set("srb:sms:code:"+phone,code,180);

            log.info("向"+phone+"发送短信成功");
//            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (Exception e) {
//            System.out.println(e.toString());
            log.error("发送短信失败！！"+e.toString());
        }
    }
}
