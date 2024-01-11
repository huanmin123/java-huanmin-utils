package com.huanmin.test.utils.utils_common.sms;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.huanmin.utils.common.base.UniversalException;
import org.junit.Test;

import java.util.Map;

/**
 * @author huanmin
 * @date 2024/1/5
 */
public class AliyunSms {

    public static void sendMsg(String code,String mobile) {
        //regionld 是地域ID 也就是你主要大部分的短信发送到哪里 就就使用 那个个地区的id
        //accessKeyId  是AccessKey 的账号
        //secret  是AccessKey 密码  需要在 AccessKey 中查询Secret

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4Fw1zMqtdNBfFN174aA5", "wZW5dDTUa2f29eUuWsnR4VqtmVbjd1");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");//默认
        request.setVersion("2017-05-25");//默认
        request.setAction("SendSms");  //使用的发送短信的模式  SendSms单发 , SendBatchSms批量
        request.putQueryParameter("PhoneNumbers", mobile); //手机号
        request.putQueryParameter("SignName", "中科软科技有限公司面试通知");//短信标签名
        request.putQueryParameter("TemplateCode", "SMS_218277089"); //模版CODE
        request.putQueryParameter("TemplateParam", "{\"name\":\""+code+"\"}"); //给模板赋值
        try {
            CommonResponse response = client.getCommonResponse(request);
            Map<String,Object> map= JSON.parseObject(response.getData());
            if(map.get("Message").equals("OK")&&map.get("Code").equals("OK")){
                System.out.println("验证码发送成功");
            }else{
                System.out.println("验证码发送失败");
            }
        } catch (ClientException e) {
             UniversalException.logError(e);
        }
    }
    @Test
    public void testSendMsg(){
        AliyunSms.sendMsg("123456","17682345678");
    }
}
