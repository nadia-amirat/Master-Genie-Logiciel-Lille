package debugger.impl;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;
import debugger.utils.FrameState;

public class Frame implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event) {
        try {
            System.out.println("frame : ");
            System.out.println(FrameState.getFrameStateFrom(event).toString());
        } catch (IncompatibleThreadStateException e) {
            System.out.println("> error during frame recuperation : "+e.toString());
        } catch (AbsentInformationException e) {
            System.out.println("> error during source's name recuperation : "+e.toString());;
        } finally {
            debugger.Debugger.userAction(event);

        }
    }
}
