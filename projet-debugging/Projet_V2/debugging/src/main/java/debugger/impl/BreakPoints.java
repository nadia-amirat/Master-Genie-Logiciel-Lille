package debugger.impl;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

import java.util.stream.Collectors;

public class BreakPoints implements Command {


    @Override
    public void execute(VirtualMachine vm, LocatableEvent event) {
            try {
                System.out.println(vm.eventRequestManager().breakpointRequests().stream().filter(tmp->tmp.isEnabled()).collect(Collectors.toList()));
            }
            finally {
                debugger.Debugger.userAction(event);

            }
    }
}
