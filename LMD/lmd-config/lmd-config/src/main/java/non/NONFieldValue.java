package non;

import non.visitor.Visitor;

import java.util.HashMap;

public abstract class NONFieldValue {
    public abstract String getValue();
    public abstract String accept(Visitor visitor);
}
