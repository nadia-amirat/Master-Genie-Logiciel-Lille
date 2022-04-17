package debugger.impl;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

public class Sender implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event) {
        try {
            System.out.println("sender : " + event.thread().frame(1).location().sourceName());
        } catch (IncompatibleThreadStateException e) {
            System.out.println("> error during frame recuperation : "+e.toString());
        } catch (AbsentInformationException e) {
            System.out.println("> error during source's name recuperation : "+e.toString());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("sender : no sender by now");
        } finally {
            debugger.Debugger.userAction(event);
        }
    }
}
