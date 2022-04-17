package non;

import non.visitor.Visitor;

public class NONLocalFieldValue extends NONFieldValue {

    public String getName() {
        return name;
    }

    private String name;
    private String parentId;


    public NONLocalFieldValue(String name, String parentId) {
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
        return visitor.visitLocalFieldValue(this);
    }


}
