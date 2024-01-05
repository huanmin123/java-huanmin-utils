package com.huanmin.test.server.common.obj.copy;


import com.huanmin.test.entity.UserData;
import com.utils.common.obj.copy.BeanCopyUtil;
import org.junit.Test;

/**
 * @author huanmin
 * @date 2024/1/5
 */
public class BeanCopyUtilTest {
    @Test
    public void copy1() throws Exception {
        UserData user=new UserData();
        user.setName("hu");
        user.setAge(12);

        UserData user1=new UserData();
        user1.setName("hu");
        user1.setAge(1);

        BeanCopyUtil.copy(user, user1);
        System.out.println(user1);

        UserData copy = BeanCopyUtil.copy(user);
        System.out.println(copy);


        UserData user2=new UserData();
    }
}
