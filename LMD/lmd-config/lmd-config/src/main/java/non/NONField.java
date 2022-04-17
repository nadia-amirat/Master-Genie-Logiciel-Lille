package non;

public class NONField {

    private String name;
    private NONFieldValue value;
    private NONObject parent;

    public NONField() {
    }

    public NONField(String name, NONFieldValue value, NONObject parent) {
        this.name = name;
        this.value = value;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(NONFieldValue value) {
        this.value = value;
    }

    public NONObject getParent() {
        return parent;
    }

    public void setParent(NONObject parent) {
        this.parent = parent;
    }

    public String getValue(){
        return value.getValue();
    }

    public NONFieldValue getNonFieldValue(){return this.value;}
}
