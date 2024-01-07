package org.huanmin.file_tool.word;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class TextStyle {
    private String fontFamily=null;
    private Integer fontSize=null;
    private String  color="00002b";//颜色黑色
    private boolean bold=false;//是否加粗
    private boolean italic=false;//是否斜体
    private boolean strike=false;//是否删除线
    private boolean shadow=false;//是否阴影
    private boolean doubleStrike=false;//双删除线
}
