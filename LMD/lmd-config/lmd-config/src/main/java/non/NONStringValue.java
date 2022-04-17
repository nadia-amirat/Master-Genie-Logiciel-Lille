package non;

import non.visitor.Visitor;

public class NONStringValue extends NONFieldValue {

    private String value;

    public NONStringValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitStringValue(this);
    }
}
