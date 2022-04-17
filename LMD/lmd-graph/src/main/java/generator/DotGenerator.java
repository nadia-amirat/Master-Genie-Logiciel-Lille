package generator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import parser.DotParser;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.*;

public class DotGenerator {

    private DotParser dotParser = new DotParser();
    private HashMap<String,ClassOrInterfaceDeclaration> classesHashMap = new HashMap<>();

    public DotGenerator(){
        this.classesHashMap = new HashMap<>();
        this.dotParser = new DotParser();
    }

    public String generateFromString(String text,String filename)throws Exception{
        ArrayList list = dotParser.parse(text);
        String dir = System.getProperty("user.dir");
        File tmp = new File(dir, filename);
        tmp.createNewFile();
        FileWriter myWriter = new FileWriter(tmp.getAbsolutePath());
        String content = generate(list);
        myWriter.write(content);
        myWriter.close();
        return content;
    }

    private String generate(ArrayList list) throws Exception {
        ClassOrInterfaceDeclaration interfaceDeclaration = generateInterface(list);
        List<Transition> transitionList = generateTransitions((List<List>)list.get(1));
        List<ClassOrInterfaceDeclaration> classes = new ArrayList<>();
        List<String> classesNames = getClasses((List)list.get(1));
        classesNames.forEach(x->classes.add(generateClasseByName(x)));
        classes.forEach(x->implementInterface(x,interfaceDeclaration));
        classes.forEach(x->classesHashMap.put(x.getNameAsString(),x));
        transitionList.forEach(x->implementMethode(x));
        implementUnmplementedMethods();
        StringWriter stringWriter = new StringWriter();
        stringWriter.write(interfaceDeclaration.toString());
        classesHashMap.forEach((key,value)-> stringWriter.write("\n"+value.toString()));

        return  stringWriter.toString();
    }

   private void implementUnmplementedMethods(){
        classesHashMap.forEach((k,v)->{
            for(MethodDeclaration method : v.getMethods()){
                Optional optional= method.getBody();
                if(method.getBody().isEmpty()){
                    NodeList nodeList = new NodeList();
                    nodeList.add(new ReturnStmt("this"));
                    method.setBody(new BlockStmt(nodeList));
                }
            }
        });
    }

    private ClassOrInterfaceDeclaration implementMethode(Transition transition){
        ClassOrInterfaceDeclaration _class = classesHashMap.get(transition.getStartState());
        NodeList nodeList = new NodeList();
        nodeList.add(new ReturnStmt(" new "+transition.getTargetState()+"()"));

        _class.getMethodsByName(transition.getAction()).get(0).setBody(new BlockStmt(nodeList));
        return _class;
    }

    private List<Transition> generateTransitions(List<List> list) throws Exception {
        List transitions = new ArrayList<Transition>();
        for(List<String> line:list){
            String from = line.get(0);
            String target = line.get(1);
            String action =line.size()>=3?line.get(2).replaceAll("\"",""):"next";
            if(from.contains(",")){
                resolveClasses(from).forEach(x->{
                    try {
                        transitions.add(generateTransition(List.of(x,target,action)));
                    } catch (Exception e) {

                    }
                });
            }else {
                transitions.add(generateTransition(List.of(from,target,action)));
            }
        }
        return transitions;
    }

    private Transition generateTransition(List<String> list)throws Exception{
        if(list.size()<3){
            throw new Exception("Transition nrequire three attributes");
        }
        Transition transition = new Transition(list.get(0),list.get(1),list.get(2));
        return transition;
    }

    private ClassOrInterfaceDeclaration implementInterface(ClassOrInterfaceDeclaration _class, ClassOrInterfaceDeclaration _interface)
    {
        ClassOrInterfaceType type = new ClassOrInterfaceType(_interface.getName().asString());
        _class.addImplementedType(type);
        _interface.getMethods().forEach(x->_class.addMember(x.clone()));
        return _class;
    }



    private List<String> getClasses(List<List> list){
        List<String> classes = new ArrayList<>();
        for(List<String> line: list){
            String startState = line.get(0);
            resolveClasses(startState).forEach(x->{
                if(!classes.contains(x)){
                    classes.add(x);
                }
            });
            String targetState = line.get(1);
            resolveClasses(targetState).forEach(x->{
                if(!classes.contains(x)){
                    classes.add(x);
                }
            });
        }
        return classes;
    }

    private List<String> resolveClasses(String text){
        ArrayList list = new ArrayList<String>();
        String cleantext = text.trim().replaceAll("\\{","").replaceAll("\\}","").replaceAll(" ","");
        String[] states = cleantext.split(",");
        Arrays.stream(states).forEach(x->list.add(x));
        return list;
    }

    private ClassOrInterfaceDeclaration generateClasse(Transition transition){
        String className = transition.getStartState();
        CompilationUnit compilationUnit = new CompilationUnit();
        ClassOrInterfaceDeclaration myClass = compilationUnit
                .addClass(className)
                .setPublic(true);

        return myClass;
    }
    private ClassOrInterfaceDeclaration generateClasseByName(String name){
        CompilationUnit compilationUnit = new CompilationUnit();
        ClassOrInterfaceDeclaration myClass = compilationUnit
                .addClass(name)
                .setPublic(true);

        return myClass;
    }

    private ClassOrInterfaceDeclaration generateInterface(ArrayList list){
        String interfaceName = (String)list.get(0);
        CompilationUnit compilationUnit = new CompilationUnit();
        ClassOrInterfaceType returnType = new ClassOrInterfaceType(interfaceName);
        ClassOrInterfaceDeclaration myInterface = compilationUnit
                .addInterface(interfaceName)
                .setPublic(true);

        List<String> methods = getMethodes((ArrayList) list.get(1));
        methods.add("next");
        methods.stream().forEach(x->{
            NodeList nodeList = new NodeList();
            nodeList.add(new Modifier(Modifier.Keyword.PUBLIC));
            MethodDeclaration method = new MethodDeclaration( nodeList,returnType,x);
            myInterface.addMember(method.removeBody());

        });

        return myInterface;
    }


    private List<String> getMethodes(ArrayList list){
        List<String> methodes = new ArrayList<>();
        for (int i =0;i<list.size();i++){
            List<String> line = (List<String>)list.get(i);
            if(line.size()<=2){
                continue;
            }
            else{
                String method = line.get(2).replaceAll("\"","");
                if(!methodes.contains(method)){
                    methodes.add(method);
                }
            }
        }
        return methodes;
    }


}
