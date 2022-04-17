package debugger.impl;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;
import org.beryx.textio.TextIO;
import org.beryx.textio.system.SystemTextTerminal;

public class PrintVar implements Command {
    private static final TextIO textIO = new TextIO(new SystemTextTerminal());
    @Override
    public void execute(VirtualMachine vm, LocatableEvent event)  {
        try {
            String varName = textIO.newStringInputReader()
                .read("Enter variable name : ");
            LocalVariable localV = event.thread().frame(0).visibleVariableByName(varName );
            Value v = event.thread().frame(0).getValue(localV);
            System.out.println(v.toString());
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
     finally {
        debugger.Debugger.userAction(event);
    }
    }
}
