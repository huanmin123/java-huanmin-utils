package com.huanmin.utils.netty.base;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;

/**
 * @author huanmin
 * @date 2023/12/4
 */
public class MyDefaultChannelGroup extends DefaultChannelGroup {
    private final ConcurrentMap<String, Channel> channels = PlatformDependent.newConcurrentHashMap();
    public MyDefaultChannelGroup(EventExecutor executor) {
        super(executor);
    }

    public MyDefaultChannelGroup(String name, EventExecutor executor) {
        super(name, executor);
    }

    public MyDefaultChannelGroup(EventExecutor executor, boolean stayClosed) {
        super(executor, stayClosed);
    }

    public MyDefaultChannelGroup(String name, EventExecutor executor, boolean stayClosed) {
        super(name, executor, stayClosed);
    }
    public Channel find(ChannelId id) {
       return channels.get(id.asLongText());
    }

    @Override
    public boolean add(Channel channel) {
        channels.put(channel.id().asLongText(), channel);
        return super.add(channel);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof ChannelId) {
            channels.remove(((ChannelId) o).asLongText());
        } else if (o instanceof Channel) {
            channels.remove(((Channel) o).id().asLongText());
        }
        return super.remove(o);
    }

    @Override
    public void clear() {
        channels.clear();
        super.clear();
    }
}
