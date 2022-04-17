package dsl.exemple;

import generator.DotGenerator;

public class Main {

    public static void main(String[] arguments) {
        Context context = new Context();
        try {
            context.test();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
