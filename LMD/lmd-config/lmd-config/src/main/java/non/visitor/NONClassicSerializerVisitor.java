package non.visitor;

import non.*;

import java.util.Map;

public class NONClassicSerializerVisitor implements Visitor{
    @Override
    public String visitStringValue(NONStringValue stringValue) {
        return "'"+stringValue.getValue()+"'";
    }

    @Override
    public String visitObject(NONObject object) {
        return "@";
    }

    @Override
    public String visitLocalFieldValue(NONLocalFieldValue localFieldValue) {
        return "."+localFieldValue.getName();
    }

    @Override
    public String visitGlobalFieldValue(NONGlobalFieldValue globalFieldValue) {
        return globalFieldValue.getParentId()+"."+globalFieldValue.getName();
    }

    @Override
    public String visitConcatValue(NONConcatValue concatValue) {
        String res= "";
        for (int i = 0; i< concatValue.getFields().size(); i++) {

            res = res + " "+ concatValue.getFields().get(i).accept(this) + " ";
        }
        return res;

    }
    public String serializeObject(NONObject nonObject) {
        String res = "";
        res = res+ nonObject.id() +":"+"\n" ;
        for (Map.Entry m : nonObject.getFields().entrySet()) {
            NONField nf = (NONField) m.getValue();

            res = res + "."+m.getKey() +" " + nf.getNonFieldValue().accept(this) + "\n";

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
