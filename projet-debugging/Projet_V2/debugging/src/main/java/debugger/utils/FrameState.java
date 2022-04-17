package debugger.utils;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.event.LocatableEvent;

import java.util.HashMap;
import java.util.List;

public class FrameState {

    private String fileName;
    private String className;
    private String methodName;
    private int line;
    private HashMap<String, String> variables;

    private FrameState(String fileName, String className, String methodName, int line, HashMap<String, String> variables) {
        this.fileName = fileName;
        this.className = className;
        this.methodName = methodName;
        this.line = line;
        this.variables = variables;
    }

    public String toString(){
        return String.format("[ \tfileName: '%s', \n\tclassName : '%s', \n\tmethodName : '%s', \n\tline : %d, \n\tvariables : %s]",
            fileName, className, methodName, line, variables.toString());
    }

    public static FrameState getFrameStateFrom(LocatableEvent breakEvent) throws AbsentInformationException, IncompatibleThreadStateException {
        int line = breakEvent.location().lineNumber();
        String fileName = null;
        fileName = breakEvent.location().sourceName();
        String className = breakEvent.location().declaringType().name();
        String methodName = breakEvent.location().method().name();
        HashMap<String, String> variables = new HashMap<>();
        List<LocalVariable> values = breakEvent.thread().frame(0).visibleVariables();
        for (LocalVariable v : values) {
            variables.put(v.name(), breakEvent.thread().frame(0).getValue(v).toString());
        }
        return new FrameState(fileName, className, methodName, line, variables);
    }
}
