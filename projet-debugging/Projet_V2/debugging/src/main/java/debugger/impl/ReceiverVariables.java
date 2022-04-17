package debugger.impl;

import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

import java.util.List;
import java.util.Map;

public class ReceiverVariables implements Command {
    @Override
    public void execute(VirtualMachine vm, LocatableEvent event) {
        try {
            //System.out.println(event.thread().frame(0).thisObject().toString());
            List<Field> fields = event.thread().frame(0).thisObject().referenceType().allFields();
            Map<Field, Value> m = event.thread().frame(0).thisObject().getValues(fields);
            for(Map.Entry<Field,Value> entry : m.entrySet()) {
                System.out.println(entry.getKey()+" : "+entry.getValue());
            }

    } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
        finally {
            debugger.Debugger.userAction(event);
        }
    }}
