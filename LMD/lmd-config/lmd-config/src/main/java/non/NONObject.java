package non;

import non.visitor.Visitor;

import java.util.ArrayList;
import java.util.HashMap;

public class NONObject extends NONFieldValue {
    private String id;
    private HashMap<String,NONField> fields;

    public NONObject() {
        this.fields=new HashMap<>();
    }

    public NONObject(NONObject object){
        this.id=object.id;
        this.fields=new HashMap<>();
        for(String key : object.fields.keySet()){
            this.fields.put(key,object.fields.get(key));
        }
    }

    public NONObject(String id, HashMap<String,NONField>  fields) {
        this.id = id;
        this.fields = fields;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  HashMap<String,NONField>  getFields() {
        return fields;
    }

    public void setFields( HashMap<String,NONField>  fields) {
        this.fields = fields;
    }

    public void addField(String key,NONField value){
        this.fields.put(key,value);
    }

    @Override
    public String getValue() {
        return this.id;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitObject(this);
    }

    public String get(String fieldName) {
        NONField field = fields.get(fieldName);
        return field.getValue();
    }

}
