package com.huanmin.utils.arithmetic.lineardata;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 自定义数组集合
 *@author: huanmin
 *@Description:  因为数组是java内置的,所以这里就将数组进行升级进行封装
 */
public class Array<T> implements Iterable<T> {
    Object[] array;
    //最后添加数据的下标
    private int record;
    //容器大小
    private int containerSiez;
    // 读写锁
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readlock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    @SafeVarargs
    public Array(T... array) {
        this.array = array;
        //当前最后一位数的下标
        this.record = array.length;
        this.containerSiez = array.length;
    }

    public Array() {
        //容器大小
        this.containerSiez = 30;
    }

    public Array(int size) {
        this.array = new Object[size];
        this.containerSiez = size;
    }

    public void add(T v) {
        writeLock.lock();
        try {
            //扩容
            if (this.record >= this.containerSiez) {
                this.containerSiez = this.containerSiez + (int) (this.containerSiez * 0.75 + 1);
                Object[] target = new Object[this.containerSiez];
                //java最快数组copy方式
                System.arraycopy(this.array, 0, target, 0, this.array.length);
                this.array = target;//将原数组替换
            }
            this.array[record++] = v;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }

    }

    public boolean update(int index, T v) {
        writeLock.lock();
        try {
            if (!(index >= 0 && index <= record - 1)) {
                return false;
            }
            this.array[index] = v;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    //值是否存在
    public boolean exists( T v) {
        readlock.lock();
        try {
            for (Object o : this.array) {
                if (o.equals(v)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readlock.unlock();
        }
        return false;
    }

    //排序

    /**
     *  //demo 从小到大排序
     *         Array<Integer> integerArray=new Array<>(2,2,100,3,10,10,4,5);
     *             integerArray.sort((data,data1)->{
     *                 if(data>data1){
     *                     return 1;
     *                 }else if(data==data1){
     *                     return 0;
     *                 }else{
     *                     return -1;
     *                 }
     *             });
     *         System.out.println(integerArray);
     * @param c
     */
    public void sort(Comparator<? super T> c) {
        writeLock.lock();
        try {
            Arrays.sort((T[]) this.array, 0, record, c);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    //反转数组
    public  void reverse() {
        writeLock.lock();
        try {
            int length = this.array.length;
            Object temp = 0;// 临时变量
            for (int i = 0; i < length / 2; i++) {
                temp = this.array[i];
                this.array[i]= this.array[length - 1 - i];
                this.array[length - 1 - i]=temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }


    public T get(int index) {
        readlock.lock();
        try {
            if (!(index >= 0 && index <= record - 1)) {
                return null;
            }
           return (T)this.array[index] ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readlock.unlock();
        }
        return null;
    }

    public T[] getAll() {
        readlock.lock();
        try {
            return (T[]) Arrays.copyOfRange(this.array, 0, record);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readlock.unlock();
        }
        return null;
    }

    // 1 ,2 ,3 ,4, 5, 6, 7
    public boolean remove(int index) {
        writeLock.lock();
        try {
            if (index <= -1) {
                return false;
            }
            System.arraycopy(this.array, index, this.array, index - 1, record - index);
            record--;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            int cursor = 0;

            @Override
            public boolean hasNext() {
                readlock.lock();
                try {
                    return cursor++ < record;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    readlock.unlock();
                }
                return false;
            }

            @Override
            public T next() {
                readlock.lock();
                try {
                    return (T) array[cursor-1];
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    readlock.unlock();
                }
                return null;
            }

            @Override
            public void remove() {
                writeLock.unlock();
                try {
                    Array.this.remove(cursor);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    writeLock.unlock();
                }
            }

        };
    }


    @Override
    public String toString() {

        return "Array{" + Arrays.toString(getAll()) +
                '}';
    }

}
