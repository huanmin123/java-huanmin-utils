package org.huanmin.utils.common.container;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * List分片处理,每次处理size个元素,支持每个分片异步处理
 */
public class ListSlice {
    //将list分片,每片size个元素,然后对每片元素执行consumer
    public static <T> List<Consumer<List<T>>> slice(List<T> list,int size, Consumer<List<T>> consumer) {
        List<Consumer<List<T>>> consumers = new ArrayList<>();
        for (int i = 0; i < list.size(); i+=size) {
            int end = i + size;
            if (end > list.size()) {
                end = list.size();
            }
            List<T> subList = list.subList(i, end);
            consumers.add(t ->consumer.accept(subList));
        }
        return consumers;
    }

    //异步执行
    public static <T>  void sliceAsyncRun(List<T> list,int size, Consumer<List<T>> consumer) {
        List<Consumer<List<T>>> slice = slice(list, size, consumer);
        slice.parallelStream().forEach(t -> new Thread(() -> t.accept(list)).start());

    }


}
