package com.huanmin.test.utils_tools.null_chain;


import com.alibaba.fastjson.JSON;
import com.huanmin.test.entity.RoleEntity;
import com.huanmin.test.entity.UserEntity;
import com.utils.common.base.UserData;
import com.utils.null_chain.Null;
import com.utils.null_chain.NullChain;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;


public class ObjNullTest {
    UserEntity userEntity = new UserEntity();
    UserEntity[] userEntitys= new UserEntity[10];
    @Before
    public void before() {
        userEntity.setId(1);
        userEntity.setName("huanmin");
        userEntity.setDate(LocalDate.now());

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("1234");
        userEntity.setRoleData(roleEntity);
        userEntitys[0] = userEntity;

    }

    @Test
    public void test1() throws ParseException {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1);
        userEntity1.setName("huanmin1");
        UserData userEntity2 = new UserData();
        userEntity2.setSex("男");

        NullChain<UserEntity> userEntityNullChain1 = Null.of(userEntity1);
        NullChain<UserData> userEntityNullChain2 = Null.of(userEntity2);

        UserData userEntity3 = Null.merge(UserData.class, Arrays.asList(userEntityNullChain1, userEntityNullChain2)).get();
        System.out.println(userEntity3);

    }

    @Test
    public void test1_ok() {

        boolean b = Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).is();
        System.out.println(b);
        String s = Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).get();
        System.out.println(s);
        Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).is( System.out::println);

        Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).isOr(
                System.out::println,
                ()-> System.out.println("isOr")
        );
        String s2 = Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).orThrow(() -> new RuntimeException("orThrow"));
        System.out.println(s2);

        String s1 = Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).orElse("orElse");
        System.out.println(s1);

        String empty = Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).toStr();
        System.out.println(empty);

        Integer convert = Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).convert(Integer::parseInt);
        System.out.println(convert);

        Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).stream().forEach(System.out::println);

        Null.of(userEntity).of(UserEntity::getRoleData,System.out::println).of(RoleEntity::getRoleName,System.out::println).stream().forEach(System.out::println);
    }

    //no判断主要用来进行强制中断程序继续执行, 因为在有些场景,下文数据会对上文的数据有着强制的依赖关系,所以需要强制中断,保证数据的完整性
    @Test
    public void test1_error() {
        boolean b = Null.no(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).is();

        Null.no(userEntity).no(UserEntity::getRoleData).of(RoleEntity::getRoleName).is( System.out::println);

        Null.no(userEntity).no(UserEntity::getRoleData).no(RoleEntity::getRoleName).isOr(
                System.out::println,
                ()-> System.out.println("isOr")
        );
        String s = Null.no(userEntity).no(UserEntity::getRoleData).no(RoleEntity::getRoleName).get();
        System.out.println(s);
    }
}
