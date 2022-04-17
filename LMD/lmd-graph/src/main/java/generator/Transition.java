package generator;

public class Transition {
    private String startState;
    private String targetState;
    private String action;

    public Transition(String startState, String targetState, String action) {
        this.startState = startState;
        this.targetState = targetState;
        this.action = action;
    }

    public String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public String getTargetState() {
        return targetState;
    }

    public void setTargetState(String targetState) {
        this.targetState = targetState;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
