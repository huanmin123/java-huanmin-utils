package org.huanmin.utils.common.multithreading.queue.priority;

public enum PriorityEnum {
    USER(1,"普通用户"),
    VIP_USER(2,"VIP普通用户"),
    MAX_VIP_USER(3,"最顶级VIP普通用户"),
    PRIVILEGE_USER(4,"特权用户"); //最牛逼
    private int type;
    private String dec;

    PriorityEnum(int type, String dec) {
        this.type = type;
        this.dec = dec;
    }

    public int getType() {
        return type;
    }

    public String getDec() {
        return dec;
    }
}
