package non.mapper;

import non.NONDefs;
import non.NONObject;
import non.utils.ParserGenerator;
import org.petitparser.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class NONMapper {

    Parser parser;
    NONObjectMapper nonObjectMapper;
    HashMap<String,NONObject> mappedObjects;


    public NONMapper() {
        this.parser= ParserGenerator.GetGlobalParser();
        this.nonObjectMapper = new NONObjectMapper();
        this.mappedObjects = new HashMap<>();
    }

    public ArrayList<NONObject> mapFile(String filePath){

        try {
            Path path = Path.of(filePath);
            String content = Files.readString(path);
            return mapStringContent(content);
        } catch (IOException e) {
            System.out.println("Cant read file "+ filePath);
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<NONObject> mapStringContent(String content){
        try {
            content=content.replaceAll("\r","");
            ArrayList<NONObject> objects= new ArrayList<>();
            ArrayList<ArrayList> nonObjects = this.parser.parse(content).get();
            System.out.println(this.parser.parse(content).get().toString());

            if(nonObjects.get(0) instanceof ArrayList){
                for(ArrayList object:nonObjects){
                    NONObject mappedObject =this.nonObjectMapper.toNONObject(object,mappedObjects);
                    objects.add(this.nonObjectMapper.toNONObject(object,mappedObjects));
                    this.mappedObjects.put(mappedObject.id(),mappedObject);
                }
            }else {
                NONObject mappedObject =this.nonObjectMapper.toNONObject(nonObjects,mappedObjects);
                objects.add(this.nonObjectMapper.toNONObject(nonObjects,mappedObjects));
                this.mappedObjects.put(mappedObject.id(),mappedObject);
            }

            return objects;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public NONDefs fromFile(String filePath){
        this.mapFile(filePath);
        return new NONDefs(this.mappedObjects);
    }

    public NONDefs fromString(String filePath){
        this.mapStringContent(filePath);
        return new NONDefs(this.mappedObjects);
    }
}
