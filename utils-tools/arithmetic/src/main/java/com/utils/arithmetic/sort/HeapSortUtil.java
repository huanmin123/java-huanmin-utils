package com.utils.arithmetic.sort;

/**
 *    堆排序
 * 　 a.将无需序列构建成一个堆，根据升序降序需求选择大顶堆或小顶堆;
 *
 * 　　b.将堆顶元素与末尾元素交换，将最大元素"沉"到数组末端;
 *
 * 　　c.重新调整结构，使其满足堆定义，然后继续交换堆顶元素与当前末尾元素，反复执行调整+交换步骤，直到整个序列有序。
 */
public class HeapSortUtil {

    public static void heapSort(Integer []arr,String sortType){
        if ("asc".equals(sortType)) {
            //1.构建大顶堆
            for(int i=arr.length/2-1;i>=0;i--){
                //从第一个非叶子结点从下至上，从右至左调整结构
                adjustHeapAsc(arr,i,arr.length);
            }
            //2.调整堆结构+交换堆顶元素与末尾元素
            for(int j=arr.length-1;j>0;j--){
                swap(arr,0,j);//将堆顶元素与末尾元素进行交换
                adjustHeapAsc(arr,0,j);//重新对堆进行调整
            }
        } else if ("desc".equals(sortType)) {
            //1.构建大顶堆
            for(int i=arr.length/2-1;i>=0;i--){
                //从第一个非叶子结点从下至上，从右至左调整结构
                adjustHeapDesc(arr,i,arr.length);
            }
            //2.调整堆结构+交换堆顶元素与末尾元素
            for(int j=arr.length-1;j>0;j--){
                swap(arr,0,j);//将堆顶元素与末尾元素进行交换
                adjustHeapDesc(arr,0,j);//重新对堆进行调整
            }
        } else {
            System.out.println("您输入的排序类型错误！");
        }


    }

    /**
     * 调整大顶堆（仅是调整过程，建立在大顶堆已构建的基础上）
     * @param arr
     * @param i
     * @param length
     */
    public static void adjustHeapDesc(Integer []arr,int i,int length){
        int temp = arr[i];//先取出当前元素i
        for(int k=i*2+1;k<length;k=k*2+1){//从i结点的左子结点开始，也就是2i+1处开始
            if(k+1<length && arr[k]>arr[k+1]){//如果左子结点小于右子结点，k指向右子结点
                k++;
            }
            if(arr[k] <temp){//如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
                arr[i] = arr[k];
                i = k;
            }else{
                break;
            }
        }
        arr[i] = temp;//将temp值放到最终的位置
    }

    public static void adjustHeapAsc(Integer []arr,int i,int length){
        int temp = arr[i];//先取出当前元素i
        for(int k=i*2+1;k<length;k=k*2+1){//从i结点的左子结点开始，也就是2i+1处开始
            if(k+1<length && arr[k]<arr[k+1]){//如果左子结点小于右子结点，k指向右子结点
                k++;
            }
            if(arr[k] >temp){//如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
                arr[i] = arr[k];
                i = k;
            }else{
                break;
            }
        }
        arr[i] = temp;//将temp值放到最终的位置
    }



    /**
     * 交换元素
     * @param arr
     * @param a
     * @param b
     */
    public static void swap(Integer []arr,int a ,int b){
        int temp=arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }



}
