package org.huanmin.test.entity;

import cn.easyes.annotation.HighLight;
import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.common.constants.Analyzer;
import cn.easyes.common.enums.FieldStrategy;
import cn.easyes.common.enums.FieldType;
import cn.easyes.common.enums.IdType;
import lombok.Data;

import java.util.Date;

@Data
@IndexName(shardsNum = 3,replicasNum = 2) // 可指定分片数,副本数,若缺省则默认均为1
public class DocumentEntity {
    /**
     * es中的唯一id,如果你想自定义es中的id为你提供的id,比如MySQL中的id,请将注解中的type指定为customize,如此id便支持任意数据类型)
     */
    @IndexId(type = IdType.CUSTOMIZE)
    private Long id;
    /**
     * 文档标题,不指定类型默认被创建为keyword类型,可进行精确查询
     */
    private String title;
    /**
     * 文档内容,指定了类型及存储/查询分词器
     */
    @HighLight(mappingField="highlightContent")
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String content;
    /**
     * 作者 加@TableField注解,并指明strategy = FieldStrategy.NOT_EMPTY 表示更新的时候的策略为 创建者不为空字符串时才更新
     */
    @IndexField(strategy = FieldStrategy.NOT_EMPTY)
    private String creator;
    /**
     * 创建时间
     */
    @IndexField(fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    private Date gmtCreate;
    /**
     * es中实际不存在的字段,但模型中加了,为了不和es映射,可以在此类型字段上加上 注解@TableField,并指明exist=false
     */
    @IndexField(exist = false)
    private String notExistsField;
    /**
     * 地理位置经纬度坐标 例如: "40.13933715136454,116.63441990026217"
     */
    @IndexField(fieldType = FieldType.GEO_POINT)
    private String location;
    /**
     * 图形(例如圆心,矩形)
     */
    @IndexField(fieldType = FieldType.GEO_SHAPE)
    private String geoLocation;
    /**
     * 自定义字段名称
     */
    @IndexField(value = "wu-la")
    private String customField;

    /**
     * 高亮返回值被映射的字段
     */
    private String highlightContent;
}


