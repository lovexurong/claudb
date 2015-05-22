package tonivade.db.command.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tonivade.db.command.IRequest;
import tonivade.db.command.IResponse;
import tonivade.db.data.DataType;
import tonivade.db.data.DatabaseValue;
import tonivade.db.data.IDatabase;

@RunWith(MockitoJUnitRunner.class)
public class HashSetCommandTest {

    @Mock
    private IDatabase db;

    @Mock
    private IRequest request;

    @Mock
    private IResponse response;

    @Test
    public void testExecute() {
        when(request.getParam(0)).thenReturn("a");
        when(request.getParam(1)).thenReturn("key");
        when(request.getParam(2)).thenReturn("value");

        when(db.merge(eq("a"), any(), any())).thenReturn(new DatabaseValue(DataType.HASH, map()));

        HashSetCommand command = new HashSetCommand();

        command.execute(db, request, response);

        verify(response).addInt(false);
    }

    private HashMap<String, String> map() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        return map;
    }

}
