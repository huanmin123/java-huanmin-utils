package com.huanmin.utils.redis.jedis.utils;


import com.huanmin.utils.common.multithreading.utils.SleepTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/8/4 12:01
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
@Component
public class JedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    @Autowired
    private JedisPool jedisPool;
    //当前线程的数据库
    public static ThreadLocal<Integer> dbIndex = ThreadLocal.withInitial(() -> 0);

    //清除线程变量
    public static void cleanThreadLocal() {
        if (dbIndex.get() != null) {
            dbIndex.remove();
        }
    }

    /**
     * 获取Jedis资源
     */
    public Jedis getJedis() {
        Jedis resource = jedisPool.getResource();
        config(resource); //配置
        return resource;
    }

    public <T> T handle(Function<Jedis, T> function) {
        T apply = null;
        try (Jedis jedis = getJedis()) {
            apply = function.apply(jedis);
        } catch (Exception e) {
            logger.error("JedisUtils handle error", e);
        }
        return apply;
    }


    private void config(Jedis jedis) {
        Integer integer = dbIndex.get();
        jedis.select(integer);
        cleanThreadLocal();//清除线程变量
    }

    //手动切换当前redis放入的数据库 ,切换后的操作将在该数据库中进行,直到线程结束或者手动再次切换
    public static void dbIndex(int dbIndex) {
        JedisUtil.dbIndex.set(dbIndex);
    }


    // 设定一个key的活动时间（s）
    public Long expire(String key, int seconds) {
        return handle((jedis) -> jedis.expire(key, seconds));
    }

    //设置 key 的过期时间以毫秒计。
    public Long pexpire(final String key, final long milliseconds) {
        return handle((jedis) -> jedis.pexpire(key, milliseconds));
    }

    //设定一个在某个时间点失效(时间戳)
    public Long expireAt(String key, long unixTime) {
        return handle((jedis) -> jedis.expireAt(key, unixTime));
    }

    //获得一个key的活动时间  , 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
    public Long ttl(String key) {
        return handle((jedis) -> jedis.ttl(key));
    }


    //返回当前选定数据库中的键数。
    public Long dbSize() {
        return handle(BinaryJedis::dbSize);
    }

    // 清空所有key
    public void flushAll() {
        handle(BinaryJedis::flushAll);
    }

    //内存使用情况
    public enum RedisInfoEnum {
        Memory, //内存使用情况
        clients,//已连接客户端信息
        server//Redis 服务器信息
    }

    // 检测 Redis 使用情况
    public String getRedisInfo(RedisInfoEnum redisInfoEnum) {
        return handle((jedis) -> {
            return jedis.info(redisInfoEnum.name());
        });
    }

    //判断key是否存在
    public Boolean exists(String key) {
        return handle((jedis) -> jedis.exists(key));
    }

    //用于删除已存在的键。不存在的 key 会被忽略。
    public Long del(String key) {
        return handle((jedis) -> jedis.del(key));
    }

    //查找所有符合给定模式 pattern 的 key
    public Set<String> keys(final String pattern) {
        return handle((jedis) -> jedis.keys(pattern));
    }

    //将当前数据库的 key 移动到给定的数据库 db 当中。
    public Long move(final String key, final int dbIndex) {
        return handle((jedis) -> jedis.move(key, dbIndex));
    }

    //移除 key 的过期时间，key 将持久保持。
    public Long persist(final String key) {
        return handle((jedis) -> jedis.persist(key));
    }

    //以毫秒为单位返回 key 的剩余的过期时间。   如果key是永久的，返回-1      如果是-2 就是此key  以过期
    public Long pttl(final String key) {
        return handle((jedis) -> jedis.pttl(key));
    }

    //从当前数据库中随机返回一个 key 。
    public String randomKey() {
        return handle((jedis) -> jedis.randomKey());
    }

    //修改 key 的名称
    public String rename(final String oldkey, final String newkey) {
        return handle((jedis) -> jedis.rename(oldkey, newkey));
    }

    //仅当 newkey 不存在时，将 key 改名为 newkey 。
    public Long renamenx(final String oldkey, final String newkey) {
        return handle((jedis) -> jedis.renamenx(oldkey, newkey));
    }

    //迭代数据库中的数据库键。
    public ScanResult<String> scan(final String key, String value, int count, final String cursor) {
        ScanParams scanParams = new ScanParams();
        scanParams.match("*" + value + "*").count(count);
        return handle((jedis) -> jedis.scan(cursor, scanParams));
    }

    //返回 key 所储存的值的类型
    /*
    返回 key 的数据类型，数据类型有：
        none (key不存在)
        string (字符串)
        list (列表)
        set (集合)
        zset (有序集)
        hash (哈希表)
     */
    public String type(final String key) {
        return handle((jedis) -> jedis.type(key));
    }


    public RedisStr redisStr = new RedisStr();

    public class RedisStr {
        //获取指定key的值(字符串)
        public String get(String key) {
            return handle((jedis) -> jedis.get(key));
        }

        //获取所有(一个或多个)给定 key 的值。
        public List<String> mget(final String... keys) {
            return handle((jedis) -> jedis.mget(keys));
        }

        //将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
        public String getSet(final String key, final String value) {
            return handle((jedis) -> jedis.getSet(key, value));
        }

        //返回 key 所储存的字符串值的长度。
        public Long strlen(final String key) {
            return handle((jedis) -> jedis.strlen(key));
        }

        //设置指定 key 的值。
        public String set(final String key, final String value) {
            return handle((jedis) -> jedis.set(key, value));
        }

        //同时设置一个或多个 key-value 对。  列: "key1 Hello key2 World"
        public String mset(final String... keysvalues) {
            return handle((jedis) -> jedis.mset(keysvalues));
        }

        //用于所有给定 key 都不存在时，同时设置一个或多个 key-value 对。  列: "key1 Hello key2 World"
        public Long msetnx(final String... keysvalues) {
            return handle((jedis) -> jedis.msetnx(keysvalues));
        }

        //如果key存在那么返回0,不设置key, 如果key不存在那么设置key,返回 1
        public Long setnx(String key, String value) {
            return handle((jedis) -> jedis.setnx(key, value));
        }

        //为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
        public String setex(String key, int seconds, String value) {
            return handle((jedis) -> jedis.setex(key, seconds, value));
        }

        //这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。
        public String psetex(final String key, final long milliseconds, final String value) {
            return handle((jedis) -> jedis.psetex(key, milliseconds, value));
        }

        //将 key 所储存的值减去指定的减量值 ,如果不存在那么默认初始化0然后在减去指定的减量值。 返回新值
        public Long decrBy(String key, long integer) {
            return handle((jedis) -> jedis.decrBy(key, integer));
        }

        //将 key 中储存的数字值减一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。  返回新值
        public Long decr(String key) {
            return handle((jedis) -> jedis.decr(key));
        }

        //将 key 中储存的数字加上指定的增量值。如果 key 不存在，那么 key 的值会先被初始化为 0   返回新值
        public Long incrBy(String key, long integer) {
            return handle((jedis) -> jedis.incrBy(key, integer));
        }

        //将 key 所储存的值加上给定的浮点增量值（increment） 。
        public Double incrByFloat(final String key, final double increment) {
            return handle((jedis) -> jedis.incrByFloat(key, increment));
        }

        // key 中储存的数字值增一 如果 key 不存在，那么 key 的值会先被初始化为 0    返回新值
        public Long incr(String key) {
            return handle((jedis) -> jedis.incr(key));
        }

        //用于为指定的 key 追加值。如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
        public Long append(String key, String value) {
            return handle((jedis) -> jedis.append(key, value));
        }

        //截取字符串,从0开始
        public String substr(String key, int start, int end) {
            return handle((jedis) -> jedis.substr(key, start, end));
        }

        //获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)
        public String getrange(final String key, final long startOffset, final long endOffset) {
            return handle((jedis) -> jedis.getrange(key, startOffset, endOffset));
        }
    }

    public PublishSubscription publishSubscription = new PublishSubscription();

    public class PublishSubscription {
        //订阅一个或多个符合给定模式的频道。
        public void subscribe(Consumer<String> consumer, final String... channels) {
            Jedis jedis = getJedis();
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    System.out.println("channel:" + channel + "=====message:" + message);
                    consumer.accept(message);

                }
            }, channels);
        }

        //订阅一个或多个符合给定模式的频道。 支持正则表达式匹配
        public void psubscribe(Consumer<String> consumer, String... patterns) {
            try (Jedis jedis = getJedis()) {
                jedis.psubscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("channel:" + channel + "=====message:" + message);
                        consumer.accept(message);
                    }
                }, patterns);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //将信息发送到指定的频道
        public Long publish(final String channel, final String message) {
            return handle((jedis) -> jedis.publish(channel, message));
        }


    }


    //哈希
    /*
    hash 是一个 string 类型的 field（字段） 和 value（值） 的映射表，hash 特别适合用于存储对象。Redis 中每个 hash 可以存储 232 - 1 键值对（40多亿）。
    */
    public RedisMap redisMap = new RedisMap();

    public class RedisMap {
        // 用于为哈希表中的字段赋值 。如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。如果字段已经存在于哈希表中，旧值将被覆盖。 更新返回0,创建返回1
        public Long hset(String key, String field, String value) {
            return handle((jedis) -> jedis.hset(key, field, value));
        }

        //用于返回哈希表中指定字段的值。
        public String hget(String key, String field) {
            return handle((jedis) -> jedis.hget(key, field));
        }

        //用于为哈希表中不存在的的字段赋值 。如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。如果字段已经存在于哈希表中，操作无效。
        public Long hsetnx(String key, String field, String value) {
            return handle((jedis) -> jedis.hsetnx(key, field, value));
        }

        //于同时将多个 field-value (字段-值)对设置到哈希表中。此命令会覆盖哈希表中已存在的字段。如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作。
        public String hmset(String key, Map<String, String> hash) {
            return handle((jedis) -> jedis.hmset(key, hash));
        }

        //用于返回哈希表中，一个或多个给定字段的值。如果指定的字段不存在于哈希表，那么返回一个 null 值。
        public List<String> hmget(String key, String... fields) {
            return handle((jedis) -> jedis.hmget(key, fields));
        }

        /*用于为哈希表中的字段值加上指定增量值。
        增量也可以为负数，相当于对指定字段进行减法操作。
        如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
        如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
        对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。*/
        public Long hincrBy(String key, String field, long value) {
            return handle((jedis) -> jedis.hincrBy(key, field, value));
        }

        //用于查看哈希表的指定字段是否存在。
        public Boolean hexists(String key, String field) {
            return handle((jedis) -> jedis.hexists(key, field));
        }

        //用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。
        public Long hdel(String key, String field) {
            return handle((jedis) -> jedis.hdel(key, field));
        }

        //用于获取哈希表中字段的数量。
        public Long hlen(String key) {
            return handle((jedis) -> jedis.hlen(key));
        }


        //用于获取哈希表中的所有域（field）
        public Set<String> hkeys(String key) {
            return handle((jedis) -> jedis.hkeys(key));
        }

        //返回哈希表所有的值。
        public List<String> hvals(String key) {
            return handle((jedis) -> jedis.hvals(key));
        }

        //返回哈希表中，所有的字段和值。
        public Map<String, String> hgetAll(String key) {
            return handle((jedis) -> jedis.hgetAll(key));
        }
    }

    //列表
    /*
     Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）
     一个列表最多可以包含 232 - 1 个元素 (4294967295, 每个列表超过40亿个元素)。
     */
    public RedisList redisList = new RedisList();

    public class RedisList {
        //用于将一个或多个值插入到列表的尾部(最右边)。如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当key存在但不是列表类型时，返回一个错误。
        public Long rpush(String key, String... string) {
            return handle((jedis) -> jedis.rpush(key, string));
        }

        //将一个或多个值插入到已存在的列表尾部(最右边)。如果列表不存在，操作无效。
        public Long rpushx(String key, String... string) {
            return handle((jedis) -> jedis.rpushx(key, string));
        }

        //将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
        public Long lpush(String key, String... string) {
            return handle((jedis) -> jedis.lpush(key, string));
        }

        //将一个或多个值插入到已存在的列表头部，列表不存在时操作无效。
        public Long lpushx(String key, String... string) {
            return handle((jedis) -> jedis.lpushx(key, string));
        }

        //用于在列表的元素前或者后插入元素。当指定元素不存在于列表中时，不执行任何操作。当列表不存在时，被视为空列表，不执行任何操作。如果 key 不是列表类型，返回一个错误。
        public Long linsert(String key, ListPosition where, String pivot, String value) {
            return handle((jedis) -> jedis.linsert(key, where, pivot, value));
        }

        //通过索引来设置元素的值。当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。
        public String lset(String key, long index, String value) {
            return handle((jedis) -> jedis.lset(key, index, value));
        }

        //用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
        public Long llen(String key) {
            return handle((jedis) -> jedis.llen(key));
        }

        //返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
        // 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
        public List<String> lrange(String key, long start, long end) {
            return handle((jedis) -> jedis.lrange(key, start, end));
        }

        //对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
        // 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
        public String ltrim(String key, long start, long end) {
            return handle((jedis) -> jedis.ltrim(key, start, end));
        }

        //用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
        public String lindex(String key, long index) {
            return handle((jedis) -> jedis.lindex(key, index));
        }

        //移出并获取列表的开头一个元素或者多个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
        public List<String> blpop(int timeout, String... keys) {
            return handle((jedis) -> jedis.blpop(timeout, keys));
        }

        //移出并获取列表的最后一个元素或者多个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
        public List<String> brpop(int timeout, String... keys) {
            return handle((jedis) -> jedis.brpop(timeout, keys));
        }

        //从列表中取出最后一个元素，并插入到另外一个列表的头部； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
        public String brpoplpush(String source, String destination, int timeout) {
            return handle((jedis) -> jedis.brpoplpush(source, destination, timeout));
        }


        /*
        Redis Lrem 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。
            COUNT 的值可以是以下几种：
            count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
            count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
            count = 0 : 移除表中所有与 VALUE 相等的值。
         */
        public Long lrem(String key, long count, String value) {
            return handle((jedis) -> jedis.lrem(key, count, value));
        }

        //移除列表的第一个元素,并返回。
        public String lpop(String key) {
            return handle((jedis) -> jedis.lpop(key));
        }

        //移除列表的最后一个元素,并返回。
        public String rpop(String key) {
            return handle((jedis) -> jedis.rpop(key));
        }

        //用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
        public String rpoplpush(String srckey, String dstkey) {
            return handle((jedis) -> jedis.rpoplpush(srckey, dstkey));
        }

    }


    //集合
    /*
    Set 是 String 类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。
    集合对象的编码可以是 intset 或者 hashtable。
    Redis 中集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。
    集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。
     */
    public RedisSet redisSet = new RedisSet();

    public class RedisSet {
        /*将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。
                假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。
                当集合 key 不是集合类型时，返回一个错误。*/
        public Long sadd(String key, String... member) {
            return handle((jedis) -> jedis.sadd(key, member));
        }

        //返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
        public Set<String> smembers(String key) {
            return handle((jedis) -> jedis.smembers(key));
        }

        //移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。
        public Long srem(String key, String... member) {
            return handle((jedis) -> jedis.srem(key, member));
        }

        //用于移除集合中的指定 key 的一个随机元素，移除后会返回移除的元素。
        public String spop(String key) {
            return handle((jedis) -> jedis.spop(key));
        }

        //返回集合中元素的数量。
        public Long scard(String key) {
            return handle((jedis) -> jedis.scard(key));
        }

        //判断成员元素是否是集合的成员。
        public Boolean sismember(String key, String member) {
            return handle((jedis) -> jedis.sismember(key, member));
        }

        //回第一个集合与其他集合之间的差异，也可以认为说第一个集合中独有的元素。不存在的集合 key 将视为空集。
        public Set<String> sdiff(String... keys) {
            return handle((jedis) -> jedis.sdiff(keys));
        }

        //将多个给定集合(keys)之间的差集, 存储在指定的集合中(dstkey)。如果指定的集合 key 已存在，则会被覆盖。
        public Long sdiffstore(String dstkey, String... keys) {
            return handle((jedis) -> jedis.sdiffstore(dstkey, keys));
        }

        //返回给定所有给定集合的交集。
        public Set<String> sinter(String... keys) {
            return handle((jedis) -> jedis.sinter(keys));
        }

        //将多个给定集合(keys)之间的交集, 存储在指定的集合中(dstkey)。如果指定的集合 key 已存在，则会被覆盖。
        public Long sinterstore(String dstkey, String... keys) {
            return handle((jedis) -> jedis.sinterstore(dstkey, keys));
        }

        //返回给定集合的并集。不存在的集合 key 被视为空集。
        public Set<String> sunion(String... keys) {
            return handle((jedis) -> jedis.sunion(keys));
        }

        //将给定集合的并集存储在指定的集合 destination 中。如果 destination 已经存在，则将其覆盖。
        public Long sunionstore(String dstkey, String... keys) {
            return handle((jedis) -> jedis.sunionstore(dstkey, keys));
        }


        /*
        将指定成员 member 元素从 source 集合移动到 destination 集合。。SMOVE 是原子性操作。
        如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。
        否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。
        当 destination 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。
        当 source 或 destination 不是集合类型时，返回一个错误。
         */
        public Long smove(String srckey, String dstkey, String member) {
            return handle((jedis) -> jedis.smove(srckey, dstkey, member));
        }

        //用于返回集合中的一个随机元素。
        public String srandmember(String key) {
            return handle((jedis) -> jedis.srandmember(key));
        }

        //命令用于返回集合中的一个随机元素。
        // 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。
        // 如果 count 大于等于集合基数，那么返回整个集合。
        // 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
        public List<String> srandmember(final String key, final int count) {
            return handle((jedis) -> jedis.srandmember(key, count));
        }

        //用于迭代有序集合中的元素（包括元素成员和元素分值）
        // key 集合
        // value 匹配的元素 可以使用*进行前后匹配  ,支持正则匹配
        // count 每次迭代显示的元素数量
        // cursor 游标
        // 每次返回的ScanResult内有下次游标的值,通过这个游标就能继续取剩下的元素,以此类推
        public ScanResult<String> sscan(final String key, String value, int count, final String cursor) {
            ScanParams scanParams = new ScanParams();
            scanParams.match("*" + value + "*").count(count);
            return handle((jedis) -> jedis.sscan(key, cursor, scanParams));
        }


    }

    //有序集合
    /*
    有序集合和集合一样也是 string 类型元素的集合,且不允许重复的成员。
    不同的是每个元素都会关联一个 double 类型的分数。redis 正是通过分数来为集合中的成员进行从小到大的排序。
    有序集合的成员是唯一的,但分数(score)却可以重复。
    集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。 集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。
     */
    public RedisSortedSet sortedSet = new RedisSortedSet();

    public class RedisSortedSet {
        /*
        用于将一个或多个成员元素及其分数值加入到有序集当中。如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
        分数值可以是整数值或双精度浮点数。如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。当 key 存在但不是有序集类型时，返回一个错误。
         */
        public Long zadd(String key, double score, String member) {
            return handle((jedis) -> jedis.zadd(key, score, member));
        }

        //用于计算集合中元素的数量。
        public Long zcard(String key) {
            return handle((jedis) -> jedis.zcard(key));
        }

        //用于计算有序集合中指定分数区间的成员数量。
        public Long zcount(String key, double min, double max) {
            return handle((jedis) -> jedis.zcount(key, min, max));
        }

        /*
        对有序集合中指定成员的分数加上增量 increment可以通过传递一个负数值 increment ，让分数减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
        当 key 不存在，或分数不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。当 key 不是有序集类型时，返回一个错误。分数值可以是整数值或双精度浮点数。
         */
        public Double zincrby(byte[] key, double score, byte[] member) {
            return handle((jedis) -> jedis.zincrby(key, score, member));
        }

        /*
          计算给定的一个或多个有序集的交集(sets)，并将该交集(结果集)储存到 destination 。
          结果集中某个成员的分数值计算规则 取最大值 (max) 或取最小值 (min) 取和 (sum)
          一般建议取最大的分值,ZParams.Aggregate.MAX
         */
        public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
            return handle((jedis) -> jedis.zinterstore(dstkey, params, sets));
        }

        //给定的一个或多个有序集的并集(sets)，并将该并集(结果集)储存到 destination 。
        //结果集中某个成员的分数值计算规则 取最大值 (max) 或取最小值 (min) 取和 (sum)
        //一般建议取最大的分值,ZParams.Aggregate.MAX
        public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
            return handle((jedis) -> jedis.zunionstore(dstkey, params, sets));
        }


        //在计算有序集合中指定字典区间内成员数量。
        public void zlexcount(final String key, final String min, final String max) {
            handle((jedis) -> jedis.zlexcount(key, min, max));
        }

        //返回有序集中，指定区间内的成员。其中成员的位置按分数值递增(从小到大)来排序。具有相同分数值的成员按字典序来排列。
        public Set<String> zrange(String key, int start, int end) {
            return handle((jedis) -> jedis.zrange(key, start, end));
        }

        //通过字典区间返回有序集合的成员。
        public Set<String> zrangeByLex(final String key, final String min, final String max) {
            return handle((jedis) -> jedis.zrangeByLex(key, min, max));
        }

        /*
        返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。
         */
        public Set<String> zrangeByScore(final String key, final double min, final double max) {
            return handle((jedis) -> jedis.zrangeByScore(key, min, max));
        }

        //返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
        public Long zrank(final String key, final String member) {
            return handle((jedis) -> jedis.zrank(key, member));
        }

        //用于移除有序集中的一个或多个成员，不存在的成员将被忽略。当 key 存在但不是有序集类型时，返回一个错误。
        public Long zrem(final String key, final String... members) {
            return handle((jedis) -> jedis.zrem(key, members));
        }

        //用于移除有序集合中给定的字典区间的所有成员。
        public Long zremrangeByLex(final String key, final String min, final String max) {
            return handle((jedis) -> jedis.zremrangeByLex(key, min, max));
        }

        //用于移除有序集中，指定排名(rank)区间内的所有成员。
        public Long zremrangeByRank(final String key, final long start, final long stop) {
            return handle((jedis) -> jedis.zremrangeByRank(key, start, stop));
        }

        //用于移除有序集中，指定分数（score）区间内的所有成员。
        public Long zremrangeByScore(final String key, final double min, final double max) {
            return handle((jedis) -> jedis.zremrangeByScore(key, min, max));
        }

        //返回有序集中，指定区间内的成员。其中成员的位置按分数值递减(从大到小)来排列。具有相同分数值的成员按字典序的逆序排列。
        public Set<String> zrevrange(final String key, final long start, final long stop) {
            return handle((jedis) -> jedis.zrevrange(key, start, stop));
        }

        //返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。具有相同分数值的成员按字典序的逆序排列。
        public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
            return handle((jedis) -> jedis.zrevrangeByScore(key, max, min));
        }

        /*
           返回有序集中成员(member)的排名。。排名以 0 为底，也就是说分数值最大的成员排名为 0
        */
        public Long zrevrank(final String key, final String member) {
            return handle((jedis) -> jedis.zrevrank(key, member));
        }

        //返回有序集中，成员(member)的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
        public Double zscore(final String key, final String member) {
            return handle((jedis) -> jedis.zscore(key, member));
        }

        //用于迭代有序集合中的元素（包括元素成员和元素分值）
        // key 集合
        // value 匹配的元素 可以使用*进行前后匹配  ,支持正则匹配
        // count 每次迭代显示的元素数量
        // cursor 游标
        // 每次返回的ScanResult内有下次游标的值,通过这个游标就能继续取剩下的元素,以此类推
        public ScanResult<Tuple> zscan(final String key, String value, int count, final String cursor) {
            ScanParams scanParams = new ScanParams();
            scanParams.match("*" + value + "*").count(count);
            return handle((jedis) -> jedis.zscan(key, cursor, scanParams));
        }
    }

    //是用来做基数统计的算法(存在一定误差，占用内存少，稳定占用 12k 左右内存，可以统计 2^64 个元素(18446744073709551616))
    public RedisHyperLogLog redisHyperLogLog = new RedisHyperLogLog();

    public class RedisHyperLogLog {
        //将所有元素参数添加到 HyperLogLog 数据结构中
        public Long pfadd(final String key, final String... elements) {
            return handle((jedis) -> jedis.pfadd(key, elements));
        }

        //返回给定 HyperLogLog 的基数估算值。
        public long pfcount(final String key) {
            return handle((jedis) -> jedis.pfcount(key));
        }

        //多个(sourcekeys) HyperLogLog 合并为一个 HyperLogLog(destkey) ，合并后的 HyperLogLog 的基数估算值是通过对所有 给定 HyperLogLog 进行并集计算得出的。
        public String pfmerge(final String destkey, final String... sourcekeys) {
            return handle((jedis) -> jedis.pfmerge(destkey, sourcekeys));
        }
    }


    public RedisManage redisManage = new RedisManage();

    public class RedisManage {


        public List<Object> manage(Consumer<Transaction> consumer) {
            List<Object> exec = new ArrayList<>();
            try (Jedis jedis = getJedis()) {
                Transaction multi = jedis.multi();
                consumer.accept(multi);
                exec = multi.exec();
            } catch (Exception e) {
                logger.error("JedisUtils handle error", e);
            }
            return exec;
        }

        //如果key在exec之前被修改了,那么重新执行事物
        public List<Object> manageCAS(Consumer<Transaction> consumer, String... key) {
            List<Object> exec = null;
            do {
                if (key.length != 0) {
                    try (Jedis jedis = getJedis()) {
                        jedis.watch(key);
                        Transaction multi = jedis.multi();
                        consumer.accept(multi);
                        exec = multi.exec();
                    } catch (Exception e) {
                        logger.error("JedisUtils handle error", e);
                    }
                }
                if (exec == null) {
                    System.out.println("有人修改了-重新执行新的事物");
                    SleepTools.ms(100);
                }
            } while (exec == null);

            return exec;
        }

    }

    public RedisLua redisLua = new RedisLua();

    public class RedisLua {
        // 解释器执行Lua脚本, 不带参数
        public Object eval(final String script) {
            return handle((jedis) -> jedis.eval(script));
        }

        //script： 参数是一段 Lua 5.1 脚本程序。脚本不必(也不应该)定义为一个 Lua 函数。
        //keyCount： 用于指定键名参数的个数。
        //key [key ...]： 从 EVAL 的第三个参数开始算起，表示在脚本中所用到的那些 Redis 键(key)，这些键名参数可以在 Lua 中通过全局变量 KEYS 数组，用 1 为基址的形式访问( KEYS[1] ， KEYS[2] ，以此类推)。
        //arg [arg ...]： 附加参数，在 Lua 中通过全局变量 ARGV 数组访问，访问的形式和 KEYS 变量类似( ARGV[1] 、 ARGV[2] ，诸如此类)。
        public Object eval(final String script, final List<String> keys, final List<String> args) {
            return handle((jedis) -> jedis.eval(script, keys, args));
        }
    }


    public class Combination {

        //自定义原生组合使用方式
        /*
        使用管道技术，客户端可以在未收到响应的时候继续发送请求，可以将多个请求一起发送，服务端将最终的结果返回。
        使用管道需要注意以下几点：
            1. 每条命令都不能依赖于之前命令的执行结果
            2. 如果中间某条命令执行失败，不影响之后命令的执行
            3. 管道中的每条命令执行期间，可以有其他线程的命令插队执行，这点与redis的事物不一样
            4. 由于redis需要处理完所有命令之后再返回客户端结果，因此在这之前，需要在内存中缓存已处理完命令的结果，所以并不是打包的命令越多越好,会占用内存的
         */
        public List<Object> run(Consumer<Pipeline> handle) {

            return handle((jedis) -> {
                Pipeline pipelined = jedis.pipelined();//开启管道
                handle.accept(pipelined);
                return pipelined.syncAndReturnAll();
            });
        }

        //如果redis中没有key，则查询数据库(或者其他)，并将数据存入redis中
        public String getStringOrHandle(String key, Function<String, String> handle) {
            return handle((jedis) -> {
                String val = null;
                if (!jedis.exists(key)) {
                    String apply = handle.apply(key);
                    jedis.set(key, apply);
                    val = apply;
                } else {
                    val = jedis.get(key);
                }
                return val;
            });
        }

        //如果redis中没有key，则查询数据库(或者其他)，并将数据存入redis中
        public Map<String, String> getMapOrHandle(String key, Function<String, Map<String, String>> handle) {
            return handle((jedis) -> {
                Map<String, String> val = null;
                if (!jedis.exists(key)) {
                    Map<String, String> apply = handle.apply(key);
                    jedis.hmset(key, apply);
                    val = apply;
                } else {
                    val = jedis.hgetAll(key);
                }
                return val;
            });
        }

    }


}
