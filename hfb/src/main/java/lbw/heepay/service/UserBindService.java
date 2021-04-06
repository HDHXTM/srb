package lbw.heepay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lbw.heepay.model.UserBind;

import java.util.Map;

public interface UserBindService extends IService<UserBind> {

	UserBind bind(Map<String, Object> paramMap);

	boolean isBind(String idCard);

	UserBind getByBindCode(String bindCode);

	void checkPassword(String bindCode, String password);
}
