/*
 * Copyright (c) 2016-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.bitset;

import static com.github.tonivade.resp.protocol.RedisToken.error;
import static com.github.tonivade.resp.protocol.RedisToken.integer;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;
import com.github.tonivade.tinydb.data.DatabaseValue;

@CommandUnderTest(SetBitCommand.class)
public class SetBitCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Test
    public void testExecuteOne() throws Exception {
        rule.withData("test", DatabaseValue.bitset())
            .withParams("test", "10", "1")
            .execute()
            .then(integer(false));
    }

    @Test
    public void testExecuteZero() throws Exception {
        rule.withData("test", DatabaseValue.bitset(10))
            .withParams("test", "10", "0")
            .execute()
            .then(integer(true));
    }

    @Test
    public void testExecuteBitFormat() throws Exception {
        rule.withData("test", DatabaseValue.bitset())
            .withParams("test", "1", "a")
            .execute()
            .then(error("bit or offset is not an integer"));
    }

    @Test
    public void testExecuteOffsetFormat() throws Exception {
        rule.withData("test", DatabaseValue.bitset())
            .withParams("test", "a", "0")
            .execute()
            .then(error("bit or offset is not an integer"));
    }
}
