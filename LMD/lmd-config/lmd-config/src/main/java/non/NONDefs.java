package non;

import java.util.ArrayList;
import java.util.HashMap;

public class NONDefs {
    static HashMap<String,NONObject> objects;

    public NONDefs(HashMap<String,NONObject> objects) {
        this.objects = objects;
    }

    public static NONObject at(String id){
        return objects.get(id);
    }

    public static HashMap<String,NONObject> getObjects(){
        return objects;
    }

}
