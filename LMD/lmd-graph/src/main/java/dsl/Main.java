package dsl;

import dsl.exemple.Context;
import generator.DotGenerator;

public class Main {

    public static void main(String[] arguments) {
        DotGenerator generator = new DotGenerator();
        String text = "digraph HeroState {\n" +
                "Standing -> Jumping [ label = \"up\" ];\n" +
                "Jumping -> Diving [ label = \"down\" ];\n" +
                "{Jumping, Diving} -> Standing;\n" +
                "Standing -> Crouching [ label = \"down\" ];\n" +
                "Crouching -> Standing [ label = \"release\" ];\n" +
                "}";


        try {
            System.out.println(generator.generateFromString(text,"HeroState.java"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
