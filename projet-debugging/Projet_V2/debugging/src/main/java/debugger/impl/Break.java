package debugger.impl;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;
import debugger.command.Command;
import org.beryx.textio.TextIO;
import org.beryx.textio.system.SystemTextTerminal;

import java.util.stream.Collectors;

public class Break implements Command {
    private static final TextIO textIO = new TextIO(new SystemTextTerminal());
    @Override
    public void execute(VirtualMachine vm, LocatableEvent event) {

        try {
            System.out.println("Break : \n");
            String fileName = textIO.newStringInputReader()
                .read("Enter filename : ");

            int lineNumber =  textIO.newIntInputReader()
                .read("Enter number line : ");
            System.err.println(fileName.equals(debugger.ToDebug.class.getName()));
            setBreakPoint(fileName,lineNumber,vm);


            System.out.println("A Break point has been added at line "+lineNumber + "in the file "+fileName);
        } catch (AbsentInformationException e) {
            System.out.println("> error during source's name recuperation : "+e.toString());;
        } finally {
            debugger.Debugger.userAction(event);

        }

    }

    public static void setBreakPoint(String className, int lineNumber, VirtualMachine vm) throws AbsentInformationException {
        for (ReferenceType targetClass : vm.allClasses())
            if(targetClass.name().equals(className)) {
                Location location = targetClass.locationsOfLine(lineNumber).get(0);
                BreakpointRequest brkReq = vm.eventRequestManager().createBreakpointRequest(location);
                brkReq.enable();
                //tester que les break non actifs ne se rajoute pas dans breakpoints
                // BreakpointRequest brkReq2 = vm.eventRequestManager().createBreakpointRequest(location);
                System.err.println(vm.eventRequestManager().breakpointRequests());
            }
    }
    public static void setInitBreakPoint(String className, VirtualMachine vm) throws AbsentInformationException {
        for (ReferenceType targetClass : vm.allClasses())
            if(targetClass.name().equals(className)) {
                Location location = targetClass.allLineLocations().get(0);
                BreakpointRequest brkReq = vm.eventRequestManager().createBreakpointRequest(location);

                brkReq.enable();

            }
    }
}
