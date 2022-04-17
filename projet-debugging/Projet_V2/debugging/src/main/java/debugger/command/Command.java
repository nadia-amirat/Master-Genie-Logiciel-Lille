package debugger.command;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

public interface Command {
    public void execute(VirtualMachine vm, LocatableEvent event);

}
