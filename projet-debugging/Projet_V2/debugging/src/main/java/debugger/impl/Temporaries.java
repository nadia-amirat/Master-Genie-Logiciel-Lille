package debugger.impl;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

import java.util.HashMap;
import java.util.List;

public class Temporaries implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event) {
        try {
            HashMap<String, String> variables = new HashMap<>();
            List<LocalVariable> values = event.thread().frame(0).visibleVariables();
            for (LocalVariable v : values){
                variables.put(v.name(), event.thread().frame(0).getValue(v).toString());
            }
            System.out.println("temporary variables : " + variables.toString());
        } catch (IncompatibleThreadStateException e) {
            System.out.println("> error during frame recuperation : "+e.toString());
        } catch (AbsentInformationException e) {
            System.out.println("> error during variables recuperation : "+e.toString());
        } finally {
            debugger.Debugger.userAction(event);
        }
    }
}

