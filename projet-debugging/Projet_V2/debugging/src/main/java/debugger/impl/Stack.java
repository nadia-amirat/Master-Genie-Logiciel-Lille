package debugger.impl;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

import java.util.List;

public class Stack implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event) {
        try {
            String result = "";
            List<StackFrame> frames = event.thread().frames();
            for (StackFrame f : frames){
                result = f.location().method().name() + "." + result;
            }
            System.out.println("stack : " + result);
        } catch (IncompatibleThreadStateException e) {
            System.out.println("> error during frame recuperation : "+e.toString());
        } finally {
            debugger.Debugger.userAction(event);
        }
    }
}

