/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db.command;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tonivade.db.data.IDatabase;

public class Session implements ISession {

    private final String id;

    private final ChannelHandlerContext ctx;

    private int db;

    private final Set<String> subscriptions = new HashSet<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Session(String id, ChannelHandlerContext ctx) {
        super();
        this.id = id;
        this.ctx = ctx;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ChannelHandlerContext getContext() {
        return ctx;
    }

    @Override
    public int getCurrentDB() {
        return db;
    }

    @Override
    public void setCurrentDB(int db) {
        this.db = db;
    }

    @Override
    public Set<String> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void addSubscription(String channel) {
        subscriptions.add(channel);
    }

    @Override
    public void removeSubscription(String channel) {
        subscriptions.remove(channel);
    }

    @Override
    public void enqueue(ICommand command, IDatabase db, IRequest request, IResponse response) {
        executor.submit(() -> {
            command.execute(db, request, response);

            ctx.writeAndFlush(response.toString());

            if (response.isExit()) {
                ctx.close();
            }
        });
    }

    @Override
    public void destroy() {
        executor.shutdown();
    }

}
