/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.pubsub;

import static com.github.tonivade.resp.protocol.RedisToken.array;
import static com.github.tonivade.resp.protocol.RedisToken.string;
import static com.github.tonivade.tinydb.DatabaseValueMatchers.set;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.tinydb.ITinyDB;
import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;

@CommandUnderTest(PublishCommand.class)
public class PublishCommandTest {

  @Rule
  public final CommandRule rule = new CommandRule(this);

  @Captor
  private ArgumentCaptor<RedisToken> captor;

  @Test
  public void testExecute() throws Exception {
    rule.withAdminData("subscriptions:test", set("localhost:12345"))
    .withParams("test", "Hello World!")
    .execute()
    .then(RedisToken.integer(1))
    .verify(ITinyDB.class).publish("localhost:12345", 
                                   array(string("message"), string("test"), string("Hello World!")));
  }

}
