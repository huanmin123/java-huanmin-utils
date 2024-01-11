package com.huanmin.test.utils.utils_common.obj.copy;


import com.alibaba.fastjson.JSON;
import com.huanmin.test.entity.RoleEntity;
import com.huanmin.utils.common.obj.copy.BeanCopierUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.time.LocalDate;

/**
 * @author huanmin
 * @date 2024/1/5
 */
public class BeanCopierUtilTest {

    @Getter
    @Setter
    public class UserEntity1  {
        //    private static final long serialVersionUID = 1L;
        private  Integer id;
        private  String name; //名称
        private  String pass; //密码
        private  Integer age; //年龄
        private  String sex;//性别
        private  String site; //地址
        private  Boolean del; //是否删除
        private LocalDate date; //日期
        private RoleEntity roleData;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }
    @Test
    public void copy(){
        UserEntity1 user=new UserEntity1();
        user.setName("hu");
        user.setAge(123);

        UserEntity1 userDto=new UserEntity1();
        userDto.setSex("男");
        BeanCopierUtil.copy(user,userDto);


        System.out.println(userDto);

    }
}
