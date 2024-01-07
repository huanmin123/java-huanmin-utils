package org.huanmin.arithmetic.blocksearch;


import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.huanmin.arithmetic.query.MaxMinBinarySearchListUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class BlockUtil {
   private static   ThreadPoolExecutor executor = ThreadFactoryUtil.getExecutor(ThreadFactoryUtil.ThreadConfig.BlockUtil);
    //二分查询(首次出现停止查找)
    public  static <T> int binarysearchFirst(List<BlockObject<T>> ints, long value) {
        int low = 0;
        int high = ints.size() - 1;
        if(value < ints.get(low).getId() || value > ints.get(high).getId()){
            return -1;
        }
        while(low <= high) {
            int mid = (low + high) >> 1; //每次折半比较
            if (value == ints.get(mid).getId()) {
                return mid;
            }else  if (value > ints.get(mid).getId()) {
                low=mid + 1;
            } else if (value < ints.get(mid).getId()) {
                high=mid - 1;
            }
        }
        return -1;
    }

    //L=0, R=array.size() - 1
    public static <T> long getMaxListBlockObject(List<BlockObject<T>> ints, int L, int R) {
        if (L == R) {
            return ints.get(L).getId();
        }
        int mid = (L + R) / 2;
        long maxLeft = getMaxListBlockObject(ints, L, mid);
        long maxRight = getMaxListBlockObject(ints, mid + 1, R);
        return Math.max(maxLeft, maxRight);// 返回两个数较大的一个

    }
    public static <T> long getMaxListBlock(List<Block<T>> ints) {
        List<Long> list=new CopyOnWriteArrayList<>();
        List<Future<?>> futures=new ArrayList<>();
        for (int i = 0; i < ints.size(); i++) {
            int finalI = i;
            Future<?> submit = executor.submit(() -> {
                //拿到每一个Block的容器进行计算最大值
                List<BlockObject<T>> ints1 = ints.get(finalI).getInts();
                long max = getMaxListBlockObject(ints1, 0, ints1.size() - 1);
                list.add(max);
            });
            futures.add(submit);
        }
        ExecutorUtil.waitComplete(futures);
        return  MaxMinBinarySearchListUtil.getMax(list, 0, list.size() - 1);
    }



    /**
     * 查找num所在的块,比较的是Block中的maxNum
     *
     * @param num
     * @return -1表示未找到所在块
     */
   public static  <T> int getBlockIndex(long num, int blocksLenght, List<Block<T>> blocks) {
        int resultIndex = 0;
        // 1.使用二分查找 找到num所在块的位置
        int low = 0;
        int high =blocksLenght - 1;
        int mid = 0;
        while (low <= high) {
            mid = (low + high) / 2;
            // 2.表示找到的值等于当前块的最大值,那么直接返回当前索引即可
            if (num == blocks.get(mid).getMaxNum()) {
                return mid;
            } else if (num > blocks.get(mid).getMaxNum()) {
                low = mid + 1;
            } else if (num < blocks.get(mid).getMaxNum()) {
                high = mid - 1;
            }
        }
        // 3.走到这里表示num与块中最大值一样的值,或者num就在索引= (low+high)/2 块的中
        resultIndex = (low + high + 1) / 2;
        if (resultIndex >= blocks.size()) {
            resultIndex = -1;// 表示未找到
        }

        return resultIndex;
    }


    /**
     *  例如:blocks[0]中的最大值小于blocks[1]中的最小值,依次类推
     * @param num    待查找的值
     * @return num 在array数组中的索引位置:-1表示未找到
     */
    public static  <T>  int[] blocksSearch( int num,List<Block<T>> blocks, int blockSize) {
        // 1.先从块的索引表中找出当前值所在的块
        int blockIndex = BlockUtil.getBlockIndex( num,blockSize,blocks);
        // -1表示块未找到,那么对应num的在array肯定找不到
        if (blockIndex==-1) {
            return  new int[]{-1,-1};
        }
        // 2.在块中使用两头加折半查找.
        Block<T> block = blocks.get(blockIndex);

        return orderBlocksSearch(num, blocks, block, blockIndex);
    }

    public static  <T>  int[] orderBlocksSearch( int num,List<Block<T>> blocks,  Block<T> block, int blockIndex) {
            int[] index=new int[2];
            index[0]=blockIndex;
            if (blockIndex == -1) {
                index[1]=-1;
                return index;
            }
            int siez = blocks.get(blockIndex).siez()- 1;
            for (int start = 0, end = siez ; start <= end; start++, end--) {
                if (num == block.getValue(start).getId()) {
                    index[1]=start;
                    return index;
                }
                int reduce = start + end >> 1;
                if (num == block.getValue(reduce).getId()) {
                    index[1]=reduce;
                    return  index;
                }
                if (num == block.getValue(end).getId()) {
                    index[1]=end;
                    return index;
                }
            }
            //如果都没有找到那么就返回-1
            index[1]=-1;
            return index;

    }


    public static <T> long getClassId(T object) throws NoSuchFieldException, IllegalAccessException {
        //通过反射拿到对象内的id值
        Class<?> aClass = object.getClass();
        Field field = aClass.getDeclaredField("id");
        field.setAccessible(true);
        return  (long)field.get(object);
    }




}
