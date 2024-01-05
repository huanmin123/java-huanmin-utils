package com.huanmin.test.server.common.obj.copy;


import com.huanmin.test.entity.UserData;
import com.utils.common.obj.copy.BeanCopierUtil;
import org.junit.Test;

/**
 * @author huanmin
 * @date 2024/1/5
 */
public class BeanCopierUtilTest {
    @Test
    public void copy(){
        UserData user=new UserData();
        user.setName("hu");
        user.setAge(123);

        UserData userDto=new UserData();
        BeanCopierUtil.copyObj(user,UserData.class);
        userDto.setSex("男");

        System.out.println(userDto);

    }
}
