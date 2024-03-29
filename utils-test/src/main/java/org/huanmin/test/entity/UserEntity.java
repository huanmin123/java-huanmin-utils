package org.huanmin.test.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {
    //    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name; //名称
    private String pass; //密码
    private Integer age; //年龄
    private String sex;//性别
    private String site; //地址
    private Boolean del; //是否删除

    private Date date; //日期
    private RoleEntity roleData;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

