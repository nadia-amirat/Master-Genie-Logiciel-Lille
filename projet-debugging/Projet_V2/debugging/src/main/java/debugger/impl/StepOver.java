package debugger.impl;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;
import debugger.command.Command;

public class StepOver implements Command {

    public void execute(VirtualMachine vm, LocatableEvent event){
        StepRequest stepRequest = vm.eventRequestManager().createStepRequest(event.thread(), StepRequest.STEP_LINE, StepRequest.STEP_OVER);
        stepRequest.enable();
    }
}
