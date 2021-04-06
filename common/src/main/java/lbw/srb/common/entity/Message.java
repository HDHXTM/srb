package lbw.srb.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//短信
public class Message {
    private String phoneNumber;
    private String code;
}
