package com.huanmin.test.utils_common.base;


import com.huanmin.test.entity.RoleEntity;
import com.huanmin.test.entity.UserEntity;
import com.utils.common.base.Null;
import org.junit.Before;
import org.junit.Test;

public class ObjNullTest {
    UserEntity userEntity = new UserEntity();
    @Before
    public void before() {
        userEntity.setId(1);
        userEntity.setName("huanmin");
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("123 4");
        userEntity.setRoleData(roleEntity);
    }

    @Test
    public void test1() {
        Integer anInt = Null.of(userEntity).of(UserEntity::getRoleData).of(RoleEntity::getRoleName).toInt();
        System.out.println(anInt);

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
