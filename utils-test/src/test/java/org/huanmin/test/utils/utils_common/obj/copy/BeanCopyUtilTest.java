package org.huanmin.test.utils.utils_common.obj.copy;


import org.huanmin.test.entity.UserEntity;
import org.huanmin.utils.common.obj.copy.BeanCopyUtil;
import org.junit.Test;

/**
 * @author huanmin
 * @date 2024/1/5
 */
public class BeanCopyUtilTest {
    @Test
    public void copy1() throws Exception {
        UserEntity user=new UserEntity();
        user.setName("hu");
        user.setAge(12);

        UserEntity user1=new UserEntity();
        user1.setName("hu");
        user1.setAge(1);

        BeanCopyUtil.copy(user, user1);
        System.out.println(user1);

        UserEntity copy = BeanCopyUtil.copy(user);
        System.out.println(copy);


        UserEntity user2=new UserEntity();
    }
}
