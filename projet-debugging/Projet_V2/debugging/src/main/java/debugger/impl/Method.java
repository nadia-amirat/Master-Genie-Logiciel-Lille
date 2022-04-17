package debugger.impl;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

public class Method implements Command {
    @Override
    public void execute(VirtualMachine vm, LocatableEvent event) {
        System.out.println("method : "+event.location().method().name());
        debugger.Debugger.userAction(event);
    }
}
