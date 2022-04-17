package non.visitor;

import non.*;

public class NONResolutionSerializerVisitor implements Visitor{

    @Override
    public String visitStringValue(NONStringValue stringValue) {
        return stringValue.getValue();
    }

    @Override
    public String visitObject(NONObject object) {
        return object.getValue();
    }

    @Override
    public String visitLocalFieldValue(NONLocalFieldValue localFieldValue) {
        return localFieldValue.getValue();
    }

    @Override
    public String visitGlobalFieldValue(NONGlobalFieldValue globalFieldValue) {
        return globalFieldValue.getValue();
    }

    @Override
    public String visitConcatValue(NONConcatValue concatValue) {
        return concatValue.getValue();
    }


    public String serializeObject(NONObject nonObject){
        String res = nonObject.id()+":\n";
        for (NONField field: nonObject.getFields().values()) {
            res+=field.getName()+" ";
            res+="'"+field.getValue()+"'\n";
        }
        return res;
    }

    public String serializeNON(NONDefs non){
        String res = "";
        for (NONObject object: NONDefs.getObjects().values()) {
            res+= serializeObject(object);
        }
        return res;
    }

}
