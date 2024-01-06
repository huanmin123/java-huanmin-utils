package com.huanmin.test.utils_common.obj.copy;


import com.huanmin.test.entity.UserEntity;
import com.utils.common.obj.copy.BeanCopierUtil;
import org.junit.Test;

/**
 * @author huanmin
 * @date 2024/1/5
 */
public class BeanCopierUtilTest {
    @Test
    public void copy(){
        UserEntity user=new UserEntity();
        user.setName("hu");
        user.setAge(123);

        UserEntity userDto=new UserEntity();
        BeanCopierUtil.copyObj(user,UserEntity.class);
        userDto.setSex("男");

        System.out.println(userDto);

    }
}
