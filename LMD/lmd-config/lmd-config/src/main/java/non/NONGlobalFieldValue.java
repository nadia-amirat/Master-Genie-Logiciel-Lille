package non;

import non.visitor.Visitor;

public class NONGlobalFieldValue extends NONFieldValue {

    private String name;

    public String getName() {
        return name;
    }

    public String getParentId() {
        return parentId;
    }

    private String parentId;

    public NONGlobalFieldValue(){}

    public NONGlobalFieldValue(String name, String parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    @Override
    public String getValue() {
        NONObject object = NONDefs.at(parentId);
        return object.get(name);
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitGlobalFieldValue(this);
    }
}
