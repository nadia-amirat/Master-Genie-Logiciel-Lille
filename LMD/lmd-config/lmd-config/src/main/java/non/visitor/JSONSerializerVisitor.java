package non.visitor;

import non.*;

import java.io.StringWriter;
import java.util.Map;

public class JSONSerializerVisitor implements Visitor{

    public JSONSerializerVisitor(){};


    @Override
    public String visitStringValue(NONStringValue stringValue) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("\"");
        stringWriter.write(stringValue.getValue());
        stringWriter.write("\"");
        return stringWriter.toString();
    }

    @Override
    public String visitObject(NONObject object) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("\"");
        stringWriter.write(object.getValue());
        stringWriter.write("\"");
        return stringWriter.toString();
    }

    @Override
    public String visitLocalFieldValue(NONLocalFieldValue localFieldValue) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("\"");
        stringWriter.write(localFieldValue.getValue());
        stringWriter.write("\"");
        return stringWriter.toString();
    }

    @Override
    public String visitGlobalFieldValue(NONGlobalFieldValue globalFieldValue) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("\"");
        stringWriter.write(globalFieldValue.getValue());
        stringWriter.write("\"");
        return stringWriter.toString();
    }

    @Override
    public String visitConcatValue(NONConcatValue concatValue) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("\"");
        stringWriter.write(concatValue.getValue());
        stringWriter.write("\"");
        return stringWriter.toString();
    }

    @Override
    public String serializeObject(NONObject nonObject) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("\t{\n");
        stringWriter.write("\t\t\"id\":\""+nonObject.getValue()+"\",\n");
        for (Map.Entry mapentry : nonObject.getFields().entrySet()) {
            stringWriter.write("\t\t\""+mapentry.getKey()+"\":"+((NONField)mapentry.getValue()).getNonFieldValue().accept(this)+",\n");
        }
        String result = stringWriter.toString();
        result=result.substring(0,result.length()-2);
        result+="\n\t}";
        return result;
    }

    @Override
    public String serializeNON(NONDefs non) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("[\n");
        for (NONObject object: NONDefs.getObjects().values()) {
            stringWriter.write(serializeObject(object));
            stringWriter.write(",\n");
        }
        String result = stringWriter.toString();
        result=result.substring(0,result.length()-2);
        result+="\n]";
        return result;
    }


}
