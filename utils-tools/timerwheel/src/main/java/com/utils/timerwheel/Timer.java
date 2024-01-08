package com.utils.timerwheel;


import com.utils.common.base.DateUtil;
import com.utils.common.enums.DateEnum;
import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/30 17:05
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class Timer {
    private TimerWheel wheel_millisecond; //1级时间轮 (毫秒)   1s = 1000ms=1000个槽,按照10毫秒一个槽,那么1s就有100个槽
    private TimerWheel wheel_second; //2级时间轮 (秒)     1m = 60s=60个槽,按照1秒一个槽,那么1m就有60个槽
    private TimerWheel wheel_minute; //3级时间轮 (分钟)   1h = 60m=60个槽,按照1分钟一个槽,那么1h就有60个槽
    private TimerWheel wheel_hour; //4级时间轮 (小时)   1d = 24h=24个槽,按照1小时一个槽,那么1d就有24个槽
    private TimerWheel wheel_day; //5级时间轮 (天)     1天=30个槽,按照1天一个槽,那么1月就有30个槽
    private TimerWheel wheel_month; //6级时间轮 (月)    1月=12个槽,按照1月一个槽,那么1年就有12个槽
    private TimerWheel wheel_year; //7级时间轮 (年)     年=10000个槽,按照1年一个槽,那么10000年就有10000个槽
    private int busyNum;            // 忙的任务数量
    private Lock thread_busyNum; //忙的任务数量线程ID
    private int total;               // 总的任务数量
    private Lock thread_total;  //总的任务数量线程ID
    private List<TimerNodeData> tasks;      //全部的任务
    private TimerResolverCore timerResolver; //定时器解析器

    private static int TIMER_STATUS_STOP = 0; //停止
    private static int TIMER_STATUS_START = 1; //已启动
    private static int TIMER_STATUS_DEL = 2; //待删除 (不会删除任务,只是将任务状态改为待删除)
    private static int TIMER_STATUS_NOTARIZE_DEL = 3; //确认删除(会删除任务,通过外部因素来改变任务状态进行删除)
    private static int TIMER_STATUS_RESTART = 4; //重启任务(重置执行计划,然后将任务状态改为已启动,那么任务就会重新执行)
    private static int TIMER_DYNAMIC_AWAIT = 0;  //等待执行
    private static int TIMER_DYNAMIC_RUN = 1;   //执行中
    //毫秒级时间轮
    private static int WHEEL_MILLISECOND_SLOT_NUM = 100; //1级时间轮 (毫秒)   1s = 1000ms=1000个槽,按照10毫秒一个槽,那么1s就有100个槽
    //秒级时间轮
    private static int WHEEL_SECOND_SLOT_NUM = 60; //2级时间轮 (秒)     1m = 60s=60个槽,按照1秒一个槽,那么1m就有60个槽
    //分钟级时间轮
    private static int WHEEL_MINUTE_SLOT_NUM = 60; //3级时间轮 (分钟)   1h = 60m=60个槽,按照1分钟一个槽,那么1h就有60个槽
    //小时级时间轮
    private static int WHEEL_HOUR_SLOT_NUM = 24; //4级时间轮 (小时)   1d = 24h=24个槽,按照1小时一个槽,那么1d就有24个槽
    //天级时间轮
    private static int WHEEL_DAY_SLOT_NUM = 30; //5级时间轮 (天)     1天=30个槽,按照1天一个槽,那么1月就有30个槽
    //月级时间轮
    private static int WHEEL_MONTH_SLOT_NUM = 12; //6级时间轮 (月)     1月=12个槽,按照1月一个槽,那么1年就有12个槽
    //年级时间轮
    private static int WHEEL_YEAR_SLOT_NUM = 10000; //7级时间轮 (年)     年=10000个槽,按照1年一个槽,那么10000年就有10000个槽


    public Timer() {
        this.wheel_millisecond = create_timer_wheel(WHEEL_MILLISECOND_SLOT_NUM, 0);
        this.wheel_second = create_timer_wheel(WHEEL_SECOND_SLOT_NUM, 1);
        this.wheel_minute = create_timer_wheel(WHEEL_MINUTE_SLOT_NUM, 2);
        this.wheel_hour = create_timer_wheel(WHEEL_HOUR_SLOT_NUM, 3);
        this.wheel_day = create_timer_wheel(WHEEL_DAY_SLOT_NUM, 4);
        this.wheel_month = create_timer_wheel(WHEEL_MONTH_SLOT_NUM, 5);
        this.wheel_year = create_timer_wheel(WHEEL_YEAR_SLOT_NUM, 6);
        this.tasks = new ArrayList<>(1000);
        this.busyNum = 0;
        this.thread_busyNum = new ReentrantLock();
        this.total = 0;
        this.thread_total = new ReentrantLock();
        this.timerResolver = new TimerResolverCore();
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.Timer, () -> {
            try {
                manager_millisecond();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.Timer, () -> {
            try {
                manager_second_minute_hour_day_month_year();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.Timer, () -> {
            try {
                sask_manager();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }


    private void sask_transfer(TimerWheel pWheel, TimerWheel nextWheel) throws Exception {
        //判断当前插槽是否有任务,如果有任务那么就下发任务
        TimerNodeData timerNodeData = pWheel.slot_array.get(pWheel.slot_index);
        if (timerNodeData.data != null) {
            //将任务下发
            add_timer_wheel_node(nextWheel, timerNodeData.data);
            //清除当前插槽的数据
            timerNodeData.data = null;
        }
    }


    //创建时间轮
    private TimerWheel create_timer_wheel(int slot_num, int type) {
        TimerWheel timerWheel = new TimerWheel();
        timerWheel.slot_num = slot_num;
        timerWheel.slot_index = 0;
        timerWheel.type = type;
        //创建时间轮槽锁
        List<Lock> locks = new ArrayList<>(slot_num);
        for (int i = 0; i < slot_num; i++) {
            Lock lock = new ReentrantLock();
            locks.add(lock);
        }
        List<TimerNodeData> timerNodeDatas = new ArrayList<>(slot_num);
        for (int i = 0; i < slot_num; i++) {
            timerNodeDatas.add(new TimerNodeData(null, null));
        }
        timerWheel.locks = locks;
        timerWheel.slot_array = timerNodeDatas;
        return timerWheel;
    }


    private void thread_sask_run(TimerNode pNode) {
        addBusyNum(); //添加忙碌线程数
        pNode.timer_dynamic = TIMER_DYNAMIC_RUN;
        Object pVoid = pNode.func.apply(pNode.args); //执行任务
        pNode.returnValue = pVoid;
        pNode.timer_dynamic = TIMER_DYNAMIC_AWAIT;
        subBusyNum();//减少忙碌线程数
    }

    private void addBusyNum() {
        //加锁
        try {
            thread_busyNum.lock();
            busyNum++;
        } finally {
            //解锁
            thread_busyNum.unlock();
        }
    }

    private void addTotal() {
        //加锁
        Lock threadTotal = thread_total;
        try {
            threadTotal.lock();
            total++;
        } finally {
            //解锁
            threadTotal.unlock();
        }
    }

    private void subBusyNum() {
        try {
            //加锁
            thread_busyNum.lock();
            busyNum--;
        } finally {
            //解锁
            thread_busyNum.unlock();
        }
    }

    //获取工作中的任务数量
    public int getBusyNum() {
        return busyNum;
    }

    //总任务数量
    public int getTotal() {
        return total;
    }

    //修改任务状态,为确认删除
    public void updateTaskStatusDEL(char timer_id) {
        TimerNodeData timerNodeData = tasks.get(timer_id);
        if (timerNodeData.data != null) {
            TimerNode data = timerNodeData.data;
            data.timer_status = TIMER_STATUS_NOTARIZE_DEL;//修改为确认删除
        }
    }

    //重启任务(前提任务没有被删除)
    public void updateTaskStatusRESTART(char timer_id) {
        TimerNodeData timerNodeData = tasks.get(timer_id);
        if (timerNodeData.data != null) {
            TimerNode data = timerNodeData.data;
            if (data.timer_status != TIMER_STATUS_NOTARIZE_DEL) {
                data.timer_status = TIMER_STATUS_RESTART;//修改为重启任务
            }
        }
    }

    //修改任务状态为启动(前提任务必须是暂停状态)
    public void updateTaskStatusSTART(char timer_id) {
        TimerNodeData timerNodeData = tasks.get(timer_id);
        if (timerNodeData.data != null) {
            TimerNode data = timerNodeData.data;
            if (data.timer_status == TIMER_STATUS_STOP) {
                data.timer_status = TIMER_STATUS_START;//修改为启动
            }
        }
    }

    //修改任务状态为停止(前提任务不能是确认删除)
    public void updateTaskStatusSTOP(char timer_id) {
        TimerNodeData timerNodeData = tasks.get(timer_id);
        if (timerNodeData.data != null) {
            TimerNode data = timerNodeData.data;
            if (data.timer_status != TIMER_STATUS_NOTARIZE_DEL) {
                data.timer_status = TIMER_STATUS_STOP;//修改为停止
            }
        }
    }


    //修改指定任务的执行计划并重启任务
    public void updateTaskTimerResolver(char timer_id, String strResolver) {
        TimerNodeData timerNodeData = tasks.get(timer_id);
        if (timerNodeData.data != null) {
            TimerNode data = timerNodeData.data;
            data.strResolver = strResolver;
            data.timer_status = TIMER_STATUS_RESTART;//修改为重启任务
        }
    }

    //获取指定任务的返回值,如果任务没有执行完毕,则返回null,不会清除任务的返回值
    public Object getTaskResult(char timer_id) {
        TimerNodeData timerNodeData = tasks.get(timer_id);
        if (timerNodeData.data != null) {
            TimerNode data = timerNodeData.data;
            return data.returnValue;
        }
        return null;
    }

    //获取指定任务的返回值并且将任务返回值设置为NULL
    public Object getTaskResultAndNUll(char timer_id) {
        TimerNodeData timerNodeData = tasks.get(timer_id);
        if (timerNodeData.data != null) {
            TimerNode data = timerNodeData.data;
            Object pVoid = data.returnValue;
            data.returnValue = null;
            return pVoid;
        }
        return null;
    }


    //根据当前的任务获取下次执行的任务
   private void next_timer_sask(TimerNode timerNode) throws Exception {
        //获取定时计划执行时间(10位时间戳(秒级))
        long expires = timerResolver.resolver(timerNode.timerResolver);
        if (expires == 0) {
            timerNode.timer_status = TIMER_STATUS_DEL;//任务已经执行完毕
        }
        timerNode.expires = expires;
        timer_node_sask_print(timerNode);

    }

    private void timer_node_sask_print(TimerNode timerNode) {
        String string = DateUtil.dateTurnString(DateUtil.secondTurnDate(timerNode.expires), DateEnum.DATETIME_PATTERN);
        String format = String.format("%s任务,预计执行时间: %d(时间戳)---%s(格式化时间)\n", timerNode.timer_id, timerNode.expires, string);
        System.out.println(format);
    }

    //任务添加到时间轮
    private void add_timer_wheel(TimerNode timerNode) throws Exception {
        //获取执行时间的年月日时分秒
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.secondTurnDate(timerNode.expires));
        int sask_second = calendar.get(Calendar.SECOND);
        int sask_minute = calendar.get(Calendar.MINUTE);
        int sask_hour = calendar.get(Calendar.HOUR);
        int sask_day = calendar.get(Calendar.DATE);
        int sask_month = calendar.get(Calendar.MONTH);
        int sask_year = calendar.get(Calendar.YEAR);
        //获取当前时间的年月日时分秒
        Calendar currentCalendar = Calendar.getInstance();
        int current_second = currentCalendar.get(Calendar.SECOND);
        int current_minute = currentCalendar.get(Calendar.MINUTE);
        int current_hour = currentCalendar.get(Calendar.HOUR);
        int current_day = currentCalendar.get(Calendar.DATE);
        int current_month = currentCalendar.get(Calendar.MONTH);
        int current_year = currentCalendar.get(Calendar.YEAR);
        //将任务添加到对应的时间轮中
        // 1.如果当前年大于等于任务的年
        if (current_year >= sask_year) {
            // 2.如果当前月大于等于任务的月
            if (current_month >= sask_month) {
                // 3.如果当前日大于等于任务的日
                if (current_day >= sask_day) {
                    // 4.如果当前时大于等于任务的时
                    if (current_hour >= sask_hour) {
                        // 5.如果当前分大于等于任务的分
                        if (current_minute >= sask_minute) {
                            // 6.如果当前秒大于等于任务的秒
                            if (current_second >= sask_second) {
                                add_timer_wheel_node(wheel_millisecond, timerNode);
                            } else {
                                //添加到秒时间轮
                                add_timer_wheel_node(wheel_second, timerNode);
                            }
                        } else {
                            //添加到分时间轮
                            add_timer_wheel_node(wheel_minute, timerNode);
                        }
                    } else {
                        //添加到时时间轮
                        add_timer_wheel_node(wheel_hour, timerNode);
                    }
                } else {
                    //添加到天时间轮
                    add_timer_wheel_node(wheel_hour, timerNode);
                }
            } else {
                //添加到月时间轮
                add_timer_wheel_node(wheel_month, timerNode);
            }
        } else {
            //添加到年时间轮
            add_timer_wheel_node(wheel_year, timerNode);
        }
    }

    public void add_timer_sask(String timer_resolver, String timer_name, Function func, Object args) throws Exception {
        //创建任务节点
        TimerNode timerNode = create_timer_node(timer_resolver, timer_name, func, args);
        add_timer_wheel(timerNode);//任务添加到时间轮
        addTotal();//添加任务总数
        tasks.add(new TimerNodeData(timerNode.timer_id, timerNode));//添加任务到任务列表
    }
    //分布式任务添加


    //创建节点
    private TimerNode create_timer_node(String timerResolver, String timer_id, Function func, Object args) throws Exception {
        TimerNode timerNode = new TimerNode();
        //解析定时计划
        TimerResolverCore timerResolver1 = this.timerResolver.create_timer_resolver(timerResolver);
        //获取定时计划执行时间(10位时间戳(秒级))
        long expires = this.timerResolver.resolver(timerResolver1);
        //获取计划类型
        int type = this.timerResolver.get_plan_type(timerResolver1);
        int timer_status = type == 0 ? TIMER_STATUS_STOP : TIMER_STATUS_START;//如果是0那么是停止状态
        timerNode.func = func;
        timerNode.args = args;
        timerNode.timerResolver = timerResolver1;
        timerNode.expires = expires;
        timerNode.timer_id = timer_id;
        timerNode.timer_status = timer_status;
        timerNode.timer_dynamic = TIMER_DYNAMIC_AWAIT; //默认等待执行
        timerNode.next = null;
        timerNode.strResolver = timerResolver;//保存原始任务解析字符串
        timerNode.returnValue = null;
        timer_node_sask_print(timerNode);
        return timerNode;
    }


    //给指定时间轮添加任务
    private void add_timer_wheel_node(TimerWheel wheel, TimerNode timerNode) throws Exception {
        if (timerNode == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.secondTurnDate(timerNode.expires));
        //获取时间轮槽索引
        int slot_index = 0;
        switch (wheel.type) {
            case 0:
                slot_index = (int) timerNode.expires % WHEEL_MILLISECOND_SLOT_NUM;
                break;
            case 1:
                slot_index = calendar.get(Calendar.SECOND);
                break;
            case 2:
                slot_index = calendar.get(Calendar.MINUTE);
                break;
            case 3:
                slot_index = calendar.get(Calendar.HOUR);
                break;
            case 4:
                slot_index = calendar.get(Calendar.DATE);
                break;
            case 5:
                slot_index = calendar.get(Calendar.MONTH);
                break;
            case 6:
                slot_index = calendar.get(Calendar.YEAR);
                break;
            default:
                throw new Exception("不存在的时间轮类型");
        }
        //给目标时间轮槽位加锁
        Lock lock = wheel.locks.get(slot_index);
        try {
            lock.lock();
            //添加任务到目标时间轮槽位
            TimerNodeData pData = wheel.slot_array.get(slot_index);
            if (pData.data == null) {//如果槽位是空的那么创建一个链表
                pData.data = timerNode;
            } else {//这个槽位已经有任务了,我们需要把任务添加到链表中最后
                TimerNode head = pData.data;
                TimerNode pNode = pData.data;
                while (pNode != null) {
                    head = pNode;
                    if (pNode.timer_id.equals(timerNode.timer_id)) {
                        throw new Exception(pNode.timer_id + "任务名称重复了,添加任务失败");
                    }
                    pNode = pNode.next;
                }
                head.next = timerNode;
            }
        } finally {
            //解锁
            lock.unlock();
        }
    }

    //任务管理
    private void sask_manager() throws Exception {

        while (true) {
            //迭代所有的任务
            List<TimerNodeData> pIterator = tasks;
            for (int i = 0; i < pIterator.size(); i++) {
                TimerNode pNode = pIterator.get(i).data;
                if (pNode.timer_status == TIMER_STATUS_NOTARIZE_DEL) {//如果任务状态为删除状态那么就删除任务
                    //删除节点
                    pIterator.remove(i);
                    subTotal();//减少总线任务数
                    System.out.println("=================删除任务:" + pNode.timer_id);
                } else if (pNode.timer_status == TIMER_STATUS_RESTART) {//重启任务
                    System.out.println("=================" + pNode.timer_id + ":,任务重新加载\n");
                    //重新解析定时计划
                    TimerResolverCore timerResolver1 = timerResolver.create_timer_resolver(pNode.strResolver);
                    long expires = timerResolver.resolver(timerResolver1);
                    pNode.timerResolver = timerResolver1;
                    pNode.expires = expires;
                    pNode.timer_status = TIMER_STATUS_START;//设置任务状态为启动
                    timer_node_sask_print(pNode);//打印任务信息
                    //将任务重新加入到对应的时间轮中
                    add_timer_wheel(pNode);

                }
            }
            Thread.sleep(1);//休眠1秒检测一次
        }
    }

    private void subTotal() {
        //加锁
        Lock threadTotal = thread_total;
        try {
            threadTotal.lock();
            total--;
        } finally {
            //解锁
            threadTotal.unlock();
        }
    }


    private void manager_second_minute_hour_day_month_year() throws Exception {
        Calendar calendar = Calendar.getInstance();
        TimerWheel pWheel_millisecond = wheel_millisecond;
        TimerWheel pWheel_second = wheel_second;
        TimerWheel pWheel_minute = wheel_minute;
        TimerWheel pWheel_hour = wheel_hour;
        TimerWheel pWheel_day = wheel_day;
        TimerWheel pWheel_month = wheel_month;
        TimerWheel pWheel_year = wheel_year;
        //初始化插槽获取当前时间的秒数
        pWheel_second.slot_index = calendar.get(Calendar.SECOND);//设置当前插槽的索引
        pWheel_minute.slot_index = calendar.get(Calendar.MINUTE);//设置当前插槽的索引
        pWheel_hour.slot_index = calendar.get(Calendar.HOUR);//设置当前插槽的索引
        pWheel_day.slot_index = calendar.get(Calendar.DATE);//设置当前插槽的索引
        pWheel_month.slot_index = calendar.get(Calendar.MONTH);//设置当前插槽的索引
        pWheel_year.slot_index = calendar.get(Calendar.YEAR);//设置当前插槽的索引
        while (true) {
            Calendar calendar1 = Calendar.getInstance();
            int second = calendar1.get(Calendar.SECOND);
            int minute = calendar1.get(Calendar.MINUTE);
            int hour = calendar1.get(Calendar.HOUR);
            int day = calendar1.get(Calendar.DATE);
            int month = calendar1.get(Calendar.MONTH);
            int year = calendar1.get(Calendar.YEAR);
            if (pWheel_year.slot_index == year
                    && pWheel_month.slot_index == month
                    && pWheel_day.slot_index == day
                    && pWheel_hour.slot_index == hour
                    && pWheel_minute.slot_index == minute
                    && pWheel_second.slot_index == second) {
                Thread.sleep(500);// 休眠500毫秒
                continue;
            }

            int cu_pWheel_second = pWheel_second.slot_index;
            int cu_pWheel_minute = pWheel_minute.slot_index;
            int cu_pWheel_hour = pWheel_hour.slot_index;
            int cu_pWheel_day = pWheel_day.slot_index;
            int cu_pWheel_month = pWheel_month.slot_index;
            int cu_pWheel_year = pWheel_year.slot_index;

            pWheel_second.slot_index = second;
            pWheel_minute.slot_index = minute;
            pWheel_hour.slot_index = hour;
            pWheel_day.slot_index = day;
            pWheel_month.slot_index = month;
            pWheel_year.slot_index = year;

            if (pWheel_second.slot_index != cu_pWheel_second) {
                sask_transfer(pWheel_second, pWheel_millisecond);
//            printf("second===second:%d,minute:%d,hour:%d,day:%d,month:%d,year:%d\n", second, minute, hour, day, month, year);
            }
            if (pWheel_minute.slot_index != cu_pWheel_minute) {
                sask_transfer(pWheel_minute, pWheel_second);
            }
            if (pWheel_hour.slot_index != cu_pWheel_hour) {
                sask_transfer(pWheel_hour, pWheel_minute);
            }
            if (pWheel_day.slot_index != cu_pWheel_day) {
                sask_transfer(pWheel_day, pWheel_hour);
            }
            if (pWheel_month.slot_index != cu_pWheel_month) {
                sask_transfer(pWheel_month, pWheel_day);
            }
            if (pWheel_year.slot_index != cu_pWheel_year) {
                sask_transfer(pWheel_year, pWheel_month);
            }


        }
    }

    //定时器管理-(毫秒)(如果插槽里有任务那么就会执行插槽内的所有任务,而任务最多延迟1秒执行)
    private void manager_millisecond() throws Exception {

        TimerWheel pWheel = wheel_millisecond;
        //初始化插槽获取当前时间的毫秒数
        pWheel.slot_index = DateUtil.getDateTimeMillis(new Date()) / 10;//设置当前插槽的索引
        //反复循环
        while (true) {
            //执行时间轮的任务
            manager_millisecond_sask(pWheel);
            //休眠10毫秒
            Thread.sleep(10);
            //插槽索引+1
            pWheel.slot_index = pWheel.slot_index + 1;
            //如果插槽索引大于插槽数量那么就重置插槽索引
            if (pWheel.slot_index >= pWheel.slot_num) {
                pWheel.slot_index = 0;
            }
        }
    }

    private void manager_millisecond_sask(TimerWheel wheel) throws Exception {
        TimerWheel pWheel = wheel;
        //获取当前时间
        long now = DateUtil.getNowTimeSecond();
        //获取当前插槽
        int slot_index = pWheel.slot_index;
        //获取当前插槽的锁
        Lock lock = pWheel.locks.get(slot_index);
        try {
            //加锁
            lock.lock();
            //获取当前插槽的链表
            List<TimerNodeData> list = pWheel.slot_array;
            //获取当前插槽的节点
            TimerNodeData charKvListData = list.get(slot_index);
            if (charKvListData.data != null) {
                //获取当前插槽的链表,如果当前插槽的节点不为空,那么就执行当前插槽的所有任务
                TimerNode parent = charKvListData.data;
                TimerNode node = charKvListData.data;
                while (node != null) {
                    parent = node;
                    //如果当前节点的过期时间小于当前时间,并且状态是可执行那么就执行回调函数
                    if (node.expires <= now && node.timer_status == TIMER_STATUS_START) {
                        //添加任务到线程池
                        TimerNode finalNode = node;
                        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.Timer, () -> {
                            thread_sask_run(finalNode); //执行回调函数
                        });

                        //从新计算过期时间,并且判断任务是否已经执行完毕,如果任务已经执行完毕,那么就不需要放入时间轮了
                        next_timer_sask(node);
                        if (node.timer_status != TIMER_STATUS_DEL) {
                            //添加任务到对应的时间轮里
                            add_timer_wheel(node);
                        } else {
                            node.timer_status = TIMER_STATUS_DEL; //设置任务状态为带删除
                            //移除链表中的任务
                            if (node.next == null) {
                                parent.next = null;
                            } else {
                                parent.next = node.next;
                                node = parent.next;
                                continue;
                            }
                        }
                    }
                    node = node.next;
                }
                //将当前插槽的节点设置为NULL
                charKvListData.data = null;
                charKvListData.key = null;
            }

        } finally {
            //解锁
            lock.unlock();
        }

    }

}
