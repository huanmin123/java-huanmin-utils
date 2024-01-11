package com.huanmin.test.utils.utils_tools.null_chain;


import com.huanmin.test.entity.RoleEntity;
import com.huanmin.test.entity.UserEntity;
import com.huanmin.utils.common.obj.serializable.SerializeUtil;
import com.huanmin.utils.null_chain.NULL;
import com.huanmin.utils.null_chain.NullChain;
import com.huanmin.utils.null_chain.NullChainDefault;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
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
    public void test1() throws ParseException {
        String json="{\n" +
                "  \"id\": 0,\n" +
                "  \"name\": \"name_d2a111e3ee4b\",\n" +
                "  \"pass\": \"pass_dc051459e118\",\n" +
                "  \"age\": 0,\n" +
                "  \"sex\": \"sex_445e6214722b\",\n" +
                "  \"site\": \"site_d23f574e7661\",\n" +
                "  \"del\": false,\n" +
                "  \"date\": \"2024-01-11\",\n" +
                "  \"roleData\": {\n" +
                "    \"id\": 0,\n" +
                "    \"roleName\": \"roleName_aa5ed84ad551\",\n" +
                "    \"roleCreationTime\": \"2024-01-11 15:33:02\",\n" +
                "    \"roleDescription\": \"roleDescription_0bec7771f0a3\",\n" +
                "    \"roleStatus\": false\n" +
                "  }\n" +
                "}";
        String json1 = NULL.toNULL(json, UserEntity.class).toJson();
        System.out.println(json1);

        NullChain<Serializable> serializableNullChain = NULL.of(null);
        System.out.println(serializableNullChain.is());

    }

    @Test
    public void test1_ok() {

        boolean b = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).is();
        System.out.println(b);
        String s = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).get();
        System.out.println(s);
        NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).is( System.out::println);

        NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).isOr(
                System.out::println,
                ()-> System.out.println("isOr")
        );
        String s2 = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).orThrow(() -> new RuntimeException("orThrow"));
        System.out.println(s2);

        String s1 = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).orElse("orElse");
        System.out.println(s1);

        String empty = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).toStr();
        System.out.println(empty);

        Integer convert = NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).convert(Integer::parseInt);
        System.out.println(convert);

        NULL.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).stream().forEach(System.out::println);

        NULL.of(userEntity).of(UserEntity::getRoleData,System.out::println).of(RoleEntity::getRoleName,System.out::println).stream().forEach(System.out::println);
    }

    //no判断主要用来进行强制中断程序继续执行, 因为在有些场景,下文数据会对上文的数据有着强制的依赖关系,所以需要强制中断,保证数据的完整性
    @Test
    public void test1_error() {
        boolean b = NULL.no(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).is();

        NULL.no(userEntity).no(UserEntity::getRoleData).of(RoleEntity::getRoleName).is( System.out::println);

        NULL.no(userEntity).no(UserEntity::getRoleData).no(RoleEntity::getRoleName).isOr(
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
        NullChain<UserEntity> unserialize = SerializeUtil.unserialize(serialize, NullChainDefault.class);
        System.out.println(unserialize.is());
    }
}
