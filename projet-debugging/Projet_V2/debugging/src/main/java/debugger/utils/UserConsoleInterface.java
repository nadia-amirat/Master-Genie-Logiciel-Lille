package debugger.utils;


import debugger.command.Command;
import debugger.impl.*;
import org.beryx.textio.*;
import org.beryx.textio.system.SystemTextTerminal;

public class UserConsoleInterface {

   private static final TextIO textIO = new TextIO(new SystemTextTerminal());

    public static void printUsage() {
        System.out.println("*** Welcome in our home-made debugger ! ***");
        System.out.println("There are your option specifications : ");
        System.out.println("\t 0. help : print usage.");
        System.out.println("\t 1. step : executes the next statement. If it is a method call, "
            + "the execution enters to the method.");
        System.out.println("\t 2. step-over : execute the current line.");
        System.out.println("\t 3. continue : continues execution until the next breakpoint.");
        System.out.println("\t 4. frame : returns and prints the current frame.");
        System.out.println("\t 5. temporaries : returns and prints the list of temporary variables "
            +"of the current frame, as a set of pairs name -> value");
        System.out.println("\t 6. stack : returns the method call stack that brought the execution to the current point.");
        System.out.println("\t 8. receiver : returns the receiver of the current method (this).");
        System.out.println("\t 9. sender : returns the object that called the current method.");
        System.out.println("\t 10. receiver-variables : returns and prints the list of instance variables "
                +"of the current receiver, as a set of pairs name →value .");
        System.out.println("\t 11. method : returns and prints the currently running method.");
        System.out.println("\t 12. arguments : returns and prints the list of arguments "
                +"of the currently running method, as a set of  name →value.");
        System.out.println("\t 13. print-var(String varName) : prints the value of the variable passed in"
                +" parameters.");
        System.out.println("\t 14. break(String filename, int lineNumber) : installs a breakpoint at the line "
                +"lineNumber of the file fileName.");
        System.out.println("\t 15. breakpoints : lists the active breakpoints and their locations in the code.");
        System.out.println("\t 16. break-once(String filename, int lineNumber) : installs a breakpoint at the line "
                +"lineNumber of the file fileName. This breakpoint is uninstalled after "
                +"it is reached");
        System.out.println("\t 17. break-on-count(String filename, int lineNumber, int count) : installs a "
                +"breakpoint at the line lineNumber of the file fileName. This breakpoint "
                +"This breakpoint is only activated after it has been reached a certain number of times count.");
          }

    private static void printChoice() {
        System.out.println("\n*** What do you want to do ? ***");
        System.out.println("Reminder : 0-help, "
            +"1-step, "
            +"2-step-over, "
            +"3-continue, "
            +"4-frame, "
            +"5-temporaries, "
            +"6-stack, "
            +"8-receiver, "
            +"9-sender, "
            +"10-receiver-variables, "
            +"11-method, "
            +"12-arguments, "
            +"13-print-var, "
            +"14-break, "
            +"15-breakpoints, "
            +"16-breakOnce, "
            +"17-break-On-count, ");
    }

    //TODO debug : andless loop after the first input
    public static Command askUserChoice() {
        printChoice();
        /* Read user input */
        String line = textIO.newStringInputReader()
            .read("Enter a number: ");
        /* Parsing */
        Command choice = parseChoice(line);
        if (choice != null) return choice;
        else return askUserChoice();
    }

    private static Command parseChoice(String input) {
        switch (input) {
            case "0":
                printUsage();
                return askUserChoice();
            case "1":
                return new Step();
            case "2":
                return new StepOver();
            case "3":
                return new Continue();
            case "4":
                return new Frame();
            case "5" :
                return new Temporaries();
            case "6" :
                return new Stack();
            case "8" :
                return new Receiver();
            case "9" :
                return new Sender();
            case "10" :
                return new ReceiverVariables();
            case "11" :
                return new Method();
            case "12" :
                return new Arguments();
            case "13" :
                return new PrintVar();
            case "14" :
                return new Break();
            case "15" :
                return new BreakPoints();
            case "16" :
                return new BreakOnce();
            case "17":
                return new BreakOnCount();

            default:
                return null;
        }
    }

}
