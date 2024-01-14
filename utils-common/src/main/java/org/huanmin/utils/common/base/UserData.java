package org.huanmin.utils.common.base;

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
    private static final long serialVersionUID = 1123L;
    private  Integer id;
    private  String name; //名称
    private  String pass; //密码
    private  Integer age; //年龄
    private  String sex;//性别
    private  String site; //地址

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

