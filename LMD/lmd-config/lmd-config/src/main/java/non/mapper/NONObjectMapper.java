package non.mapper;

import non.*;

import java.util.ArrayList;
import java.util.HashMap;

public class NONObjectMapper {
    //private Result nonObjectResult;
    private NONObject nonObject;

    public NONObjectMapper() {
        //this.nonObjectResult = nonObjectResult;
    }

    public NONObject toNONObject(ArrayList arrayList,HashMap<String,NONObject> mappedObjects) throws Exception{
        if(arrayList== null || arrayList.size()<=1){
            throw new Exception("An object should have an id and one field at least");
        }
        NONObject nonObject = new NONObject();
        // object id
        String id =(String) arrayList.get(0);
        if(id.split(":").length==2 &&  !id.split(":")[1].isEmpty()){
            // object copy
            String[] ids = id.split(":");
            String objectId = ids[0];
            String refObjectId= ids[1];
            nonObject = new NONObject(mappedObjects.get(refObjectId));
            nonObject.setId(objectId.replace(":",""));
        }else {
            nonObject.setId((String) ((String) arrayList.get(0)).replace(":",""));
        }

        this.nonObject=nonObject;

        //HashMap<String, NONField> fields = new HashMap<>();

        //object first field
        if(arrayList.get(1) instanceof String){
            String[] field = ((String)arrayList.get(1)).split(" ");
            nonObject.addField(field[0].replace(".",""),generateNonField((String)arrayList.get(1)));
        }


        //other fields
        if(arrayList.size()>2){
            if(arrayList.get(2) instanceof ArrayList){
                ArrayList<String> otherfields = (ArrayList<String>) arrayList.get(2);
                for(String otherfield : otherfields){
                    String[] _field = otherfield.split(" ");
                    nonObject.addField(_field[0].replace(".",""),generateNonField(otherfield));
                }
            }
        }
        //nonObject.setFields(fields);

        return nonObject;
    }

    public NONField generateNonField(String field)throws Exception{
        if(!field.contains(" ")){
            throw new Exception("field doesn't contain it's name and value");
        }else{
            String[] nameValue = field.split(" ");
            String name = nameValue[0];
            NONField nonField = new NONField();
            nonField.setName(name);

            String fieldValue = field.substring(name.length()+1,field.length());
            nonField.setValue(generateNonFieldValue(fieldValue));
            nonField.setParent(this.nonObject);

            return nonField;
        }
    }

    public NONFieldValue generateNonFieldValue(String fieldValue)throws Exception{
        if(fieldValue.startsWith("\'") && fieldValue.endsWith("\'")){
            return generateNonStringValue(fieldValue);
        }else if(fieldValue.equals("@")){
            return generateNonObject();
        }else if(fieldValue.startsWith(".") && !fieldValue.contains(" ")){
            return generateNonLocalFieldValue(fieldValue);
        }else if(fieldValue.split("\\.").length==2 && !fieldValue.contains(" ")){
            return generateNonGlobalFieldValue(fieldValue);
        }else if(fieldValue.contains(" ")){
            return generateNONonConcatValue(fieldValue);
        }

        throw new Exception("Can't map this fieldValue: "+fieldValue);
    }

    private NONConcatValue generateNONonConcatValue(String value) throws Exception {
        NONConcatValue nonConcatValue = new NONConcatValue();
        String[] values=value.split(" ");
        for (String nonFielValue:values){
            nonConcatValue.addNonFieldValue(generateNonFieldValue(nonFielValue));
        }
        return nonConcatValue;
    }

    public NONObject generateNonObject(){
        return this.nonObject;
    }

    public NONStringValue generateNonStringValue(String value){

        return new NONStringValue(value.replaceAll("\'",""));
    }

    public NONLocalFieldValue generateNonLocalFieldValue(String value){
        value=value.replace(".","");
        return new NONLocalFieldValue(value,this.nonObject.id());
    }

    public NONGlobalFieldValue generateNonGlobalFieldValue(String value) throws Exception{
        String[] values = value.split("\\.");
        if(values.length<2)
            throw new Exception("NonGlobalFieldValue value should have an id and a name separeted by a point");

        NONGlobalFieldValue nonGlobalFieldValue = new NONGlobalFieldValue(values[1],values[0]);
        return nonGlobalFieldValue;
    }

    static int countChar(String str, char x)
    {
        int count = 0;
        int n = 10;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == x)
                count++;

        int repetitions = n / str.length();
        count = count * repetitions;

        for (int i = 0;
             i < n % str.length(); i++)
        {
            if (str.charAt(i) == x)
                count++;
        }

        return count;
    }
}
