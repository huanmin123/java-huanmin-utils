package org.huanmin.arithmetic.blocksearch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 分块查找】
 * 算法要求 : 查找表不需要是有序的,
 * 分块查找，也称索引顺序查找，是一种折半查找和顺序查找的改进方法。
 * 【思想】
 * ① 把线性表分成若干块，每块包含若干个元素
 * ②块内无序，块间有序。
 * ③ 建立一个索引表，把每块中的最大关键字值和每块的第一个元素在表中的位置和最后一个元素在表中的位置存放在索引项中。
 * ④ 先确定待查数据元素所在的块，然后再块内顺序查找
 *
 * @param <T>
 */

//块的索引表:块中的可以无序的,但是块与块之间是有序的
//成本是 组装数据时候开销大,但是查询非常快O(1) 级别查询,不会随着数据量增加而变化
//稳定性: 查询速度不太稳定,因为有可能值都在一个Block中,然后查询变为O(N),因为如果绝大部分值都是一个范围中并且值重复很高的那么就会都堆积到同一个Block中了
//使用场景: 适用于长期查询的数据,持久化到内存中

public class BlockList<T> {

    private final int BLOCKCOUNT = 10000; //初始每块能添加多少值,(最佳,基本查询都能保证在1~0毫秒之间)
    private List<Block<T>> blocks;
    private long maxNum;   //所有 block中最大值
    private int blockSize;  //一共有几个 block
    private long totalLenght; //全部 block的总共长度
    //创建内部类
    private final BlockListInsecurity blockListInsecurity = new BlockListInsecurity();
    private final BlockListSafe blockListSafe = new BlockListSafe();
    private final BlockListCreate blockListCreate = new BlockListCreate();
    private final BlockListMethod blockListMethod = new BlockListMethod();

    private BlockList() {
    }

    public static <T> BlockList<T> builder(Class<T> clazz) {
        return new BlockList<T>();
    }


    //创建
    public BlockListCreate create() {
        return blockListCreate;
    }

    public BlockListMethod method() {
        return blockListMethod;
    }

    public class BlockListMethod {

        //查
        public BlockObject<T> get(int id) {
            return BlockList.this.blockListSafe.getBlockSearchIdBlockObject(id);
        }

        //增删改(id不能有变化)
        public void add(BlockObject<T> blockObject) {
            BlockList.this.blockListSafe.addBlockObjectOne(blockObject);
        }

        // 将List 组装到BlockList里
        public void listTo(List<T> list) {
            BlockList.this.blockListSafe.listToBlockList(list);
        }

        public void remove(BlockObject<T> blockObject) {
            BlockList.this.blockListSafe.removeIdBlockObjectOne(blockObject);
        }

        public void remove(long id) {
            BlockList.this.blockListSafe.removeIdBlockObjectOne(id);
        }

        public void update(BlockObject<T> blockObject) {
            BlockList.this.blockListSafe.updateBlockObjectOne(blockObject);
        }

        //获取blockSize
        public int getBlockSize() {
            return BlockList.this.blockListSafe.getBlockSize();
        }

        //重构blockSize
        public void reconsitution(int blockSize) {
            BlockList.this.blockListSafe.reconsitutionBlockSiez(blockSize);
        }
    }


    public class BlockListCreate {
        public BlockList<T> blockListCreateByblockSize(List<BlockObject<T>> array, int blockSize) {
            BlockList.this.blocks = BlockList.this.blockListInsecurity.creator(array, blockSize);
            return BlockList.this;
        }

        public BlockList<T> blockListCreate(List<BlockObject<T>> array) {
            BlockList.this.blocks = BlockList.this.blockListInsecurity.creator(array, BlockList.this.blockListInsecurity.thisBlockSize(array));
            return BlockList.this;
        }


        public BlockList<T> ListToBlockListCreate(List<T> array) {
            List<BlockObject<T>> listToBlockList = BlockList.this.blockListInsecurity.createListToBlockList(array);
            this.blockListCreate(listToBlockList);
            return BlockList.this;
        }
    }


    //线程安全类
    private class BlockListSafe {
        // 读写锁
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Lock readlock = readWriteLock.readLock();
        private final Lock writeLock = readWriteLock.writeLock();

        private int getBlockSize() {
            this.readlock.lock();
            int blockSize = 0;
            try {
                blockSize = BlockList.this.blockSize;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.readlock.unlock();
            }
            return blockSize;
        }

        //查询id的BlockObject
        private BlockObject<T> getBlockSearchIdBlockObject(int id) {
            BlockObject<T> blockObject = null;
            this.readlock.lock();
            try {
                int[] ints = BlockUtil.blocksSearch(id, BlockList.this.blocks, BlockList.this.blockSize);
                if (ints[1] == -1) {
                    // 没有找到
                    return blockObject;
                }
                blockObject = BlockList.this.blocks.get(ints[0]).getValue(ints[1]);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.readlock.unlock();
            }
            return blockObject;
        }

        //添加新值 (线程安全的)
        private void addBlockObjectOne(BlockObject<T> blockObject) {
            this.readlock.lock();
            try {
                //当前添加的元素id ,不大于当前所有块中最大id那么就添加指定块中
                if (blockObject.getId() <= BlockList.this.maxNum) {
                    // 块的索引
                    int blockIndex = BlockUtil.getBlockIndex(blockObject.getId(), BlockList.this.blockSize, BlockList.this.blocks);
                    BlockList.this.blocks.get(blockIndex).setValue(blockObject);
                }
                //否则就创建新的块
                BlockList.this.blockListInsecurity.newBlock(blockObject);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.readlock.unlock();
            }
        }

        //删除一个值  (线程安全的)
        private void removeIdBlockObjectOne(BlockObject<T> blockObject) {
            this.removeIdBlockObjectOne(blockObject.getId());
        }

        //(线程安全的)
        private void removeIdBlockObjectOne(long id) {
            this.readlock.lock();
            try {
                // 块的索引
                int blockIndex = BlockUtil.getBlockIndex(id, BlockList.this.blockSize, BlockList.this.blocks);
                //删除指定块的值
                BlockList.this.blocks.get(blockIndex).removeValue(id);
                BlockList.this.totalLenght = BlockList.this.totalLenght - 1;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.readlock.unlock();
            }
        }

        //通过对象索引id,找到对应的对象,进行修改  (线程安全的) (注意: 对象id不允许修改,不然就会找不到,因为系统是用对象id做的索引)
        private void updateBlockObjectOne(BlockObject<T> blockObject) {
            this.readlock.lock();
            // 块的索引arrarr
            try {
                int blockIndex = BlockUtil.getBlockIndex(blockObject.getId(), BlockList.this.blockSize, BlockList.this.blocks);
                BlockList.this.blocks.get(blockIndex).updateValue(blockObject);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.readlock.unlock();
            }
        }

        //调整BlockList的大小
        private void reconsitutionBlockSiez(int blockSize) {
            this.writeLock.lock();
            try {
                //所有块的最大值
                BlockList.this.maxNum = BlockUtil.getMaxListBlock(BlockList.this.blocks);
                //将数据从容器里那出阿里重新组装
                List<BlockObject<T>> array = new ArrayList<BlockObject<T>>((int) BlockList.this.totalLenght);
                for (Block<T> block : BlockList.this.blocks) {
                    array.addAll(block.getInts());
                }
                //初始化现有值重组装
                BlockList.this.blockListInsecurity.initialize(array, blockSize);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.writeLock.unlock();
            }
        }


        private void listToBlockList(List<T> list) {
            this.writeLock.lock();
            try {
                for (T t : list) {
                    long classId = BlockUtil.getClassId(t);
                    BlockList.this.method().add(new BlockObject<T>(classId, t));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.writeLock.unlock();
            }
        }


    }

    //线程不安全类
    private class BlockListInsecurity {

        //创建一个新块然后添加到BlockList里
        private void newBlock(BlockObject<T> array) {
            Block<T> tBlock = new Block<T>(array.getId() + BlockList.this.maxNum, (int) (BlockList.this.totalLenght / BlockList.this.blockSize));
            tBlock.setValue(array);
            try {
                BlockList.this.blocks.add(tBlock);
                BlockList.this.maxNum = tBlock.getMaxNum();
                BlockList.this.blockSize = BlockList.this.blocks.size();
                BlockList.this.totalLenght = BlockList.this.totalLenght + tBlock.siez();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private int thisBlockSize(List<BlockObject<T>> array) {
            //全部块的最大值
            BlockList.this.maxNum = (int) BlockUtil.getMaxListBlockObject(array, 0, array.size() - 1);
            return BLOCKCOUNT > array.size() ? 1 : (int) Math.ceil((double) array.size() / BLOCKCOUNT);
        }

        //10   3      30
        private List<Block<T>> creator(List<BlockObject<T>> array, int blockSize) {
            //全部块的最大值
            BlockList.this.maxNum = BlockUtil.getMaxListBlockObject(array, 0, array.size() - 1);
            return initialize(array, blockSize);
        }


        private List<Block<T>> initialize(List<BlockObject<T>> array, int blockSize) {
            // 配置块的范围
            List<Block<T>> list = new ArrayList<>();
            //每个块的边界大小(用于多线程快速分配值)
            List<int[]> list1 = new ArrayList<>();
            //每块大小
            int margin = (int) Math.ceil((double) array.size() / blockSize);
            //每个块的基值
            long basicValue = (long) Math.ceil((double) BlockList.this.maxNum / blockSize);
            for (int i1 = 0; i1 < blockSize; i1++) {
                int i3 = i1 + 1;
                //设置每个块内元素最大值
                long maxNum1 = basicValue * i3;
                //计算每一个块的边界
                int begin = margin * i1;
                int end = margin * i3;
                list1.add(new int[]{begin, end});
                list.add(new Block<T>(maxNum1, end));
            }
            int[] ints1 = list1.get(list1.size() - 1);
            ints1[1] = array.size();
            list1.set(list1.size() - 1, ints1);

            //组装块的数据
            for (BlockObject<T> blockObject : array) {
                for (Block<T> block : list) {
                    if (blockObject.getId() <= block.getMaxNum()) {
                        block.setValue(blockObject);
                        break;
                    }
                }
            }


            //初始化值
            BlockList.this.blocks = list;
            BlockList.this.blockSize = list.size();
            BlockList.this.totalLenght = array.size();
            return list;
        }


        private List<BlockObject<T>> createListToBlockList(List<T> list) {
            List<BlockObject<T>> blockObjects = new ArrayList<BlockObject<T>>(list.size());
            //通过反射拿到对象内的id值
            try {
                for (T t : list) {
                    long classId = BlockUtil.getClassId(t);
                    blockObjects.add(new BlockObject<T>(classId, t));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return blockObjects;
        }


    }


    @Override
    public String toString() {
        return "BlockList{" +
                ", maxNum=" + maxNum +
                ", blockSize=" + blockSize +
                ", blocks=" + blocks +
                '}';
    }
}
