package org.huanmin.test.utils.utils_tools.null_chain;


import org.huanmin.test.entity.RoleEntity;
import org.huanmin.test.entity.UserEntity;
import org.huanmin.utils.null_chain.NULL;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;


public class ObjNullErrorTest {
    UserEntity userEntity = new UserEntity();
    UserEntity[] userEntitys= new UserEntity[10];
    @Before
    public void before() {
        userEntity.setId(1);
        userEntity.setName("huanmin");
        userEntity.setAge(33);
        userEntity.setDate(new Date());

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("admin");
        roleEntity.setRoleDescription("xxxx");
        userEntity.setRoleData(roleEntity);
        userEntitys[0] = userEntity;

    }
    @Test
    public void of_error1() {
        userEntity=null;
        String s = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).get();
    }
    @Test
    public void of_error2() {
        userEntity.setRoleData(null);
        String s = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).get();
    }
    @Test
    public void of_error3() {
//        userEntity=null;
//        userEntity.setRoleData(null);
        userEntity.getRoleData().setRoleName(null);
        String s = NULL.of(userEntity).no(UserEntity::getRoleData).no(RoleEntity::getRoleName).get();
    }
    @Test
    public void of_error4() {
        userEntity.getRoleData().setRoleName(null);
        String s2 = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).orThrow(() -> new RuntimeException("orThrow"));
        System.out.println(s2);
    }
    @Test
    public void of_error5() {

    }
}
