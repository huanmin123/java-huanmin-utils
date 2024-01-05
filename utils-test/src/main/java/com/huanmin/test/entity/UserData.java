package com.huanmin.test.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData implements Serializable {
//    private static final long serialVersionUID = 1L;
    private  long id;
    private  String name; //名称
    private  String pass; //密码
    private  int age; //年龄
    private  String sex;//性别
    private  String site; //地址
    private  Boolean isDel; //是否删除
    private  RoleData roleData;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
