package debugger.impl;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import debugger.command.Command;

public class Continue implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event){
       event.request().disable();
    }

}
