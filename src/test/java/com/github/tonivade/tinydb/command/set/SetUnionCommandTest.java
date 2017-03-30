/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.set;

import static com.github.tonivade.resp.protocol.SafeString.safeString;
import static com.github.tonivade.tinydb.DatabaseValueMatchers.set;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.github.tonivade.resp.protocol.SafeString;
import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;
import com.github.tonivade.tinydb.command.set.SetUnionCommand;

@CommandUnderTest(SetUnionCommand.class)
public class SetUnionCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Captor
    private ArgumentCaptor<Collection<SafeString>> captor;

    @Test
    public void testExecute() throws Exception {
        rule.withData("a", set("1", "2", "3"))
            .withData("b", set("3", "4"))
            .withParams("a", "b")
            .execute()
            .verify().addSafeArray(captor.capture());

        Collection<SafeString> result = captor.getValue();

        assertThat(result.size(), is(4));

        assertThat(result.contains(safeString("1")), is(true));
        assertThat(result.contains(safeString("2")), is(true));
        assertThat(result.contains(safeString("3")), is(true));
        assertThat(result.contains(safeString("4")), is(true));
    }

    @Test
    public void testExecuteNoExists() throws Exception {
        rule.withData("a", set("1", "2", "3"))
            .withParams("a", "b")
            .execute()
            .verify().addSafeArray(captor.capture());

        Collection<SafeString> result = captor.getValue();

        assertThat(result.size(), is(3));

        assertThat(result.contains(safeString("1")), is(true));
        assertThat(result.contains(safeString("2")), is(true));
        assertThat(result.contains(safeString("3")), is(true));
    }

}
