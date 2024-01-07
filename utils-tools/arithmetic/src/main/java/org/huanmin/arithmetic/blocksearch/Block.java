package org.huanmin.arithmetic.blocksearch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author huanmin
 * @param <T>
 */
public class Block<T> {
    private  long maxNum;// 块中的最大值索引
    private List<BlockObject<T>> ints; //块内数据

    public Block( long maxNum) {
        this.maxNum = maxNum;
        this.ints = new LinkedList<>();
    }
    public Block( long maxNum,int size) {
        this.maxNum = maxNum;
        this.ints = new ArrayList<BlockObject<T>>(size);
    }
    public  void setValue(BlockObject<T> v) {
        this.ints.add(v);
    }
    public BlockObject<T> getValue(int index) {
        return this.ints.get(index);
    }

    public void removeValue(long index) {
        int i1 = BlockUtil.binarysearchFirst(this.ints, index);
        this.ints.remove(i1);
    }
    public void updateValue(BlockObject<T> blockObject) {
        int i1 = BlockUtil.binarysearchFirst(this.ints, blockObject.getId());
        this.ints.set(i1,blockObject);
    }


    public List<BlockObject<T>> getInts() {
        return ints;
    }

    public long getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(long maxNum) {
        this.maxNum = maxNum;
    }

    public int siez() {
        return ints.size();
    }

    @Override
    public String toString() {
        return "Block{" +
                "maxNum=" + maxNum +",intsLength:"+ints.size()+
                ", ints=" + ints +
                '}';
    }
}
