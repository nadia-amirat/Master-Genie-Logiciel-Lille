package non;

import non.mapper.NONMapper;

import java.util.ArrayList;

public class Main {
    public static void main(String[] arguments) {

        NONMapper nonMapper = new NONMapper();
        //String content = "univ:\n.name 'univ lille'\n.domain @\n";
        String content="univ:\n.name 'univ lille'\n.domain @\n.website 'univ-lille.fr'\n.ref @ '5643' .domain entity.name\n" +
                "univ:\n.name 'univ lille'\n.domain @\n.website 'univ-lille.fr'\n.ref @ '5643' .domain entity.name\n";
        ArrayList a = nonMapper.mapStringContent(content);

        //System.out.println(a.toString());
        //ArrayList b = fileMapper.mapFile("C:\\Users\\ALI\\OneDrive\\Desktop\\lmd.txt");

        NONDefs nds = nonMapper.fromString("univ:\n.name 'univ lille'\n.domain @\n.website 'univ-lille.fr'\n.ref @ '5643' .domain\n");
        NONObject object = nds.at("univ2");
        String id = object.id();
        String name = object.get("name");
        String domain =  object.get("domain");
        String website = object.get("website");
        String ref = object.get("ref");
    }
}
