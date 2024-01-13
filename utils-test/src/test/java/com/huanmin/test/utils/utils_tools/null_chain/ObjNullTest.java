package com.huanmin.test.utils.utils_tools.null_chain;


import com.huanmin.test.entity.RoleEntity;
import com.huanmin.test.entity.UserEntity;
import com.huanmin.utils.common.obj.serializable.SerializeUtil;
import com.huanmin.utils.null_chain.NULL;
import com.huanmin.utils.null_chain.base.NullChain;
import com.huanmin.utils.null_chain.base.NullChainBase;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;


public class ObjNullTest {
    UserEntity userEntity = new UserEntity();
    UserEntity[] userEntitys= new UserEntity[10];
    @Before
    public void before() {
        userEntity.setId(1);
        userEntity.setName("huanmin");
//        userEntity.setDate(LocalDate.now());

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("1234");
        userEntity.setRoleData(roleEntity);
        userEntitys[0] = userEntity;

    }

    @Test
    public void test1() throws ParseException, InterruptedException {
        //Void.TYPE
        NULL.of(userEntity).of(UserEntity::getRoleData).async((data)->{
            Thread.sleep(1000);
            System.out.println("111111111111async111111111111111");
            return data.get().getRoleName();
        }).async((data)->{
            System.out.println("222222222222async222222222222222"+data.get());
            return Void.TYPE;
        });

        System.out.println("main");
        Thread.sleep(4000);

    }

    @Test
    public void test1_ok() {

        boolean b = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).is();
        System.out.println(b);
        String s = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).get();
        System.out.println(s);
        NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).get( System.out::println);

        NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).get(
                System.out::println,
                ()-> System.out.println("isOr")
        );
        String s2 = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).orThrow(() -> new RuntimeException("orThrow"));
        System.out.println(s2);

        String s1 = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).orElse("orElse");
        System.out.println(s1);

        String empty = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).toStr();
        System.out.println(empty);

        Integer convert = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).convert(Integer::parseInt).get();
        System.out.println(convert);

        NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).stream().forEach(System.out::println);

        NULL.of(userEntity).of(UserEntity::getRoleData,System.out::println).of(RoleEntity::getRoleName,System.out::println).stream().forEach(System.out::println);
    }

    //no判断主要用来进行强制中断程序继续执行, 因为在有些场景,下文数据会对上文的数据有着强制的依赖关系,所以需要强制中断,保证数据的完整性
    @Test
    public void test1_error() {
        boolean b = NULL.no(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).is();

        NULL.no(userEntity).no(UserEntity::getRoleData).of(RoleEntity::getRoleName).get( System.out::println);

        NULL.no(userEntity).no(UserEntity::getRoleData).no(RoleEntity::getRoleName).get(
                System.out::println,
                ()-> System.out.println("isOr")
        );
        String s = NULL.no(userEntity).no(UserEntity::getRoleData).no(RoleEntity::getRoleName).get();
        System.out.println(s);
    }


    //可以进行网络之间的传输
    @Test
    public void serialize_() {
        UserEntity build = UserEntity.builder().id(1).age(2).name("啊是大打撒打撒撒旦大苏打撒旦撒大苏打的啊实打实大苏打大苏打").build();
        NullChain<UserEntity> userEntityNullChain = NULL.of(build) ;

        byte[] serialize = SerializeUtil.serialize(userEntityNullChain);
        NullChain<UserEntity> unserialize = SerializeUtil.unserialize(serialize, NullChainBase.class);
        System.out.println(unserialize.is());
    }
}
