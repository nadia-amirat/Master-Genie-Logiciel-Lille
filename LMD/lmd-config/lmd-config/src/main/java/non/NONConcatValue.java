package non;

import non.visitor.Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NONConcatValue extends NONFieldValue{

    private List<NONFieldValue> fields;

    public NONConcatValue() {
        this.fields=new ArrayList<>();
    }

    public NONConcatValue(List<NONFieldValue> fields) {
        this.fields = fields;
    }

    public List<NONFieldValue> getFields() {
        return fields;
    }

    public void setFields(List<NONFieldValue> fields) {
        this.fields = fields;
    }

    public void addNonFieldValue(NONFieldValue nonFieldValue){
        this.fields.add(nonFieldValue);
    }

    @Override
    public String getValue() {
        String value = "";
        for(NONFieldValue fv : fields){
            value+=fv.getValue();
        }
        return value;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitConcatValue(this);
    }
}
