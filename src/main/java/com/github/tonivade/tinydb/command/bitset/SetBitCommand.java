/*
 * Copyright (c) 2016-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.bitset;

import static com.github.tonivade.resp.protocol.RedisToken.error;
import static com.github.tonivade.resp.protocol.RedisToken.integer;
import static com.github.tonivade.tinydb.data.DatabaseKey.safeKey;
import static com.github.tonivade.tinydb.data.DatabaseValue.bitset;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.annotation.ParamLength;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.tinydb.command.TinyDBCommand;
import com.github.tonivade.tinydb.command.annotation.ParamType;
import com.github.tonivade.tinydb.data.DataType;
import com.github.tonivade.tinydb.data.Database;

@Command("setbit")
@ParamLength(3)
@ParamType(DataType.STRING)
public class SetBitCommand implements TinyDBCommand {

  @Override
  public RedisToken execute(Database db, Request request) {
    try {
      int offset = Integer.parseInt(request.getParam(1).toString());
      int bit = Integer.parseInt(request.getParam(2).toString());
      Queue<Boolean> queue = new LinkedList<>();
      db.merge(safeKey(request.getParam(0)), bitset(), (oldValue, newValue) -> {
        BitSet bitSet = BitSet.valueOf(oldValue.getString().getBuffer());
        queue.add(bitSet.get(offset));
        bitSet.set(offset, bit != 0);
        return oldValue;
      });
      return integer(queue.poll());
    } catch (NumberFormatException e) {
      return error("bit or offset is not an integer");
    }
  }
}
