package debugger.impl;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

public class Receiver implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event) {
        try {
            //System.out.println("receiver : " + event.thread().frame(0).location().sourceName());
            System.out.println("receiver : " + event.thread().frame(0).thisObject().toString());
        } catch (IncompatibleThreadStateException e) {
            System.out.println("> error during frame recuperation : "+e.toString());
        } finally {
            debugger.Debugger.userAction(event);
        }
    }
}
