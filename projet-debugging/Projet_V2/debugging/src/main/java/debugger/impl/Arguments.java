package debugger.impl;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

import java.util.HashMap;

public class Arguments implements Command {
    @Override
    public void execute(VirtualMachine vm, LocatableEvent event) {
        try {
            var argsVariables = event.location().method().arguments();
            HashMap<String, String> args = new HashMap<>();
            for (LocalVariable v : argsVariables) {
                args.put(v.name(),event.thread().frame(0).getValue(v).toString());
            }
            System.out.println("method : " + args.toString());
        } catch (AbsentInformationException e) {
            System.out.println("> error during arguments recuperation : "+e.toString());

        }
        catch (IncompatibleThreadStateException e) {
            System.out.println("> error during frame recuperation : "+e.toString());
        } finally {
            debugger.Debugger.userAction(event);
        }
    }
}
