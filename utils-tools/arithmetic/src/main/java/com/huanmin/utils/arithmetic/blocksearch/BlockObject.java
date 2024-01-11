package com.huanmin.utils.arithmetic.blocksearch;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BlockObject<T> {

    //索引
    private  Long id;
    //索引对应的数据
    private  T obj ;

    public BlockObject(Long id, T obj) {
        this.id = id;
        this.obj=obj;

    }


    public BlockObject(Integer id, T obj) {
        this.id = Long.valueOf(id);
        this.obj=obj;
    }
}
