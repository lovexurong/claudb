/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db.command.key;

import static tonivade.db.data.DatabaseKey.safeKey;

import java.time.Instant;

import tonivade.db.command.ITinyDBCommand;
import tonivade.db.data.DatabaseKey;
import tonivade.db.data.IDatabase;
import tonivade.redis.command.IRequest;
import tonivade.redis.command.IResponse;

public abstract class TimeToLiveCommand implements ITinyDBCommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        DatabaseKey key = db.getKey(safeKey(request.getParam(0)));
        if (key != null) {
            keyExists(response, key);
        } else {
            notExists(response);
        }
    }

    protected abstract int timeToLive(DatabaseKey key, Instant now);

    private void keyExists(IResponse response, DatabaseKey key) {
        if (key.expiredAt() != null) {
            hasExpiredAt(response, key);
        } else {
            response.addInt(-1);
        }
    }

    private void hasExpiredAt(IResponse response, DatabaseKey key) {
        Instant now = Instant.now();
        if (!key.isExpired(now)) {
            response.addInt(timeToLive(key, now));
        } else {
            notExists(response);
        }
    }

    private void notExists(IResponse response) {
        response.addInt(-2);
    }
}
