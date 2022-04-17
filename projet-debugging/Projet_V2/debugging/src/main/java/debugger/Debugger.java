package debugger;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.ClassPrepareRequest;
import debugger.command.Command;
import debugger.impl.Break;
import debugger.utils.UserConsoleInterface;

import java.io.IOException;
import java.util.Map;

public class Debugger {

    private static VirtualMachine vm;
    private static final Class<debugger.ToDebug> debugClass = debugger.ToDebug.class;

    private static void run() throws IOException, IllegalConnectorArgumentsException, VMStartException, AbsentInformationException, InterruptedException, IncompatibleThreadStateException {
        // Connect to VM
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("main").setValue(debugClass.getName());

        vm = launchingConnector.launch(arguments);

        // Intercept class preparation
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest.addClassFilter(debugClass.getName());
        classPrepareRequest.enable();

        // Handle Break Point
        EventSet eventSet = null;
        while ((eventSet = vm.eventQueue().remove()) != null) {
            for (Event event : eventSet) {
                if (event instanceof ClassPrepareEvent) {
                    Break.setInitBreakPoint(debugClass.getName(),vm);
                    UserConsoleInterface.printUsage();
                }
                if (event instanceof BreakpointEvent) {
                    userAction((BreakpointEvent) event);
                }
                if (event instanceof StepEvent) {
                    event.request().disable();
                    userAction((StepEvent) event);
                }
                if (event instanceof VMDeathEvent) return;
                vm.resume();
            }
        }
    }

    public static void userAction(LocatableEvent event){
        Command userCom = UserConsoleInterface.askUserChoice();
        userCom.execute(vm, event);
    }
    public static void main(String[] args) {
        try {
            run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalConnectorArgumentsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (VMStartException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AbsentInformationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
    }

}
