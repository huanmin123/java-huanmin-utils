package org.huanmin.utils.common.container;

import java.util.StringJoiner;

public class ArrayByteUtil {

    /**
     * 获取byte[]的实际的长度
     * @param bytes
     * @return
     */
    public static int getActualBytesLength(byte[] bytes){
        int end = bytes.length;
        Integer em=null; //第一个0的位置
        Integer em1=null; //0之后的位置
        //找到第一个0的位置
        for (int i = 0; i < end; i++) {
            if (bytes[i] == '\0') {
                em=i;
                break;
            }
        }
        //没有找到第一个0, 那么就代表这个数组是实际的数组
        if (em==null){
            return end;
        }

        //找最后一个非0的下标 ,那么代表这个数组后面可能还有值 ,下标继续向后走
        for (int i = em+1; i < end; i++) {
            if (bytes[i] != '\0') {
                em1=i;
            }
        }
        //0后面没有值, 那么就代表第一个0的位置就是这个数组是实际的数组
        if (em1==null){
            return em;
        }
        //找到了最后非0的数了,那么这个位置就是这个数组是实际的数组
        return em1+1 ; //因为是从0开始的所以需要+1
    }
    //字节填充,如果字节数组长度小于指定长度,则填充0
    public static byte[] fillBytes(byte[] bytes, int length) {
        if (bytes.length >= length) {
            return bytes;
        }
        byte[] bb = new byte[length];
        System.arraycopy(bytes, 0, bb, 0, bytes.length);
        return bb;
    }
    
    
    /**
     * 获取byte[]实际长度的数组
     * @param bytes
     * @return 实际长度的byte[]
     */
    public  static byte[] getActualBytes(byte[] bytes){
        if (null == bytes || 0 == bytes.length) {
            return new byte[1];
        }
        int length = getActualBytesLength(bytes);
        byte[] bb = new byte[length];
        System.arraycopy(bytes, 0, bb, 0, length);
        return bb;
    }
    
    //字节转16进制
    public static String byte2Hex(byte[] bytes,String split) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                sb.append("0");
            }
            sb.append(temp).append(split);
        }
        //判断是否多了一个分隔符,如果多了就去掉
        if (sb.length()>0 && split.equals(sb.substring(sb.length()-1))){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();

    }
    
    // 获取双字节十六进制数 ,并且按照一般主流包头的规则进行交换拼接 (后一个字节在前面) ,一般展示的长度是4位个字节的长度,当然也可以是2位个字节的长度
    //注意: 读取到0000000000000000的时候,就代表这个数组的长度就是这个了,后面的就不用读取了,就变为正常的16进制了
    public static String byte2HexHead(byte[] bytes,int len,String split) throws Exception {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = 0; i < bytes.length; i+=2) {
            temp = Integer.toHexString(bytes[i+1] & 0xFF);
            if (temp.length() == 1) {
                sb.append("0");
            }
            sb.append(temp);
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                sb.append("0");
            }
            sb.append(temp).append(split);
        }
        //判断是否多了一个分隔符,如果多了就去掉
        if (sb.length()>0 && split.equals(sb.substring(sb.length()-1))){
            sb.deleteCharAt(sb.length()-1);
        }
        //进行数据长度展示的截取
        String s1 = sb.toString().replaceAll(",", "");
        //截取0000000000000000之前的的内容其余的丢弃
        int index = s1.indexOf("0000000000000000");
        if (index!=-1){
            s1 = s1.substring(0,index);
        }else{
            //如果没有找到,就表示包头没有读取够,那么就直接返回错误
            throw new Exception("包头读取的长度不够,请检查包头的长度是否正确");
        }
        int length = s1.length();
        //判断是否是偶数,如果不是就在后面补0
        while (length%len!=0){
            s1+="0";
            length++;
        }
        StringJoiner sj = new StringJoiner(split);
        //根据len进行截取
        for (int i = 0; i < length; i++) {
            if (i%len==0){
                sj.add(s1.substring(i,i+len));
            }
        }
        return sj.toString();
    }
    


    

    
    //字节转8进制
    public static String byte2Octal(byte[] bytes,String split) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toOctalString(aByte & 0xFF);
            if (temp.length() == 1) {
                sb.append("0");
            }
            sb.append(temp).append(split);
        }
        //判断是否多了一个分隔符,如果多了就去掉
        if (sb.length()>0 && split.equals(sb.substring(sb.length()-1))){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();

    }

}
