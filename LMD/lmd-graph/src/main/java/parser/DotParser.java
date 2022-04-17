package parser;

import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import java.util.ArrayList;
import java.util.HashMap;

import static org.petitparser.parser.primitive.CharacterParser.letter;
import static org.petitparser.parser.primitive.CharacterParser.of;

public class DotParser {
    private static final Parser openedBracketParser = CharacterParser.of('[');
    private static final Parser closedBracketParser = CharacterParser.of(']');

    private static final Parser openedCurlyBracketParser = CharacterParser.of('{');
    private static final Parser closedCurlyBracketParser = CharacterParser.of('}');

    private static final Parser doubleQuoteParser = CharacterParser.of('"');
    private static final Parser equalParser = CharacterParser.of('=');
    private static final Parser semicolonParser = CharacterParser.of(';');


    private static final Parser optionalSpaceParser = of(' ').star();
    private static final Parser atLeastOneLetter = CharacterParser.letter().seq(letter().star());

    private static Parser arrowParser() {
        Parser arrowParser = CharacterParser.of('-').seq(CharacterParser.of('>'), optionalSpaceParser)
                .flatten();
        return arrowParser;
    }

    private static Parser stringParser() {
        Parser stringParser = atLeastOneLetter
                .flatten()
                .seq(optionalSpaceParser)
                .pick(0);
        return stringParser;
    }

    private static Parser actionParser() {
        Parser actionParser = doubleQuoteParser
                .seq(CharacterParser.noneOf("\""))
                .seq(CharacterParser.noneOf("\"").star())
                .seq(doubleQuoteParser).flatten()
                .seq(optionalSpaceParser).pick(0);
        return actionParser;
    }

    private static Parser actionDescriptionParser() {
        // parse [ label = "up" ]
        Parser actionParser = openedBracketParser
                .seq(optionalSpaceParser, stringParser(), equalParser, optionalSpaceParser, actionParser(), closedBracketParser, optionalSpaceParser)
                .pick(5);
        return actionParser;
    }

    /*To get the name of the interface*/
    private static Parser fileHeaderParser() {
        Parser fileHeaderParser = stringParser().seq(stringParser())
                .seq(optionalSpaceParser, openedCurlyBracketParser, optionalSpaceParser,CharacterParser.of('\n'))
                .pick(1);
        return fileHeaderParser;
    }

    private static Parser transitionParser1() {
        Parser endTransitionParser = actionDescriptionParser().seq(semicolonParser).pick(0);
        Parser transitionParser = stringParser()
                .seq(arrowParser())
                .pick(0)
                .seq(stringParser(), endTransitionParser);
        return transitionParser;
    }
    private static Parser transitionParser2() {
        Parser endTransitionParser = stringParser().seq(semicolonParser).pick(0);
        Parser skipArrowParser = arrowParser().seq(endTransitionParser).pick(1);
        Parser transitionParser = openedCurlyBracketParser
                .seq(stringParser(),CharacterParser.of(','),optionalSpaceParser,stringParser(),closedCurlyBracketParser,optionalSpaceParser)
                .flatten()
                .seq(skipArrowParser)
                ;
        return transitionParser;
    }
    private static Parser transitionParser() {
        Parser transitionParser = transitionParser1().or(transitionParser2()).seq(CharacterParser.of('\n')).pick(0);
        return transitionParser;
    }
    private static Parser fileParser() {
        Parser endFileParser = CharacterParser.of('}').seq(optionalSpaceParser).flatten().map(res -> res.toString().replace(res.toString(), ""));
        Parser fileParser = fileHeaderParser().seq(transitionParser().star()).seq();
        return fileParser;
    }

    public ArrayList parse(String text){
        return fileParser().parse(text).get();
    }

    public static void main(String[] arguments) {
        String text = "digraph HeroState {\n" +
                "Standing -> Jumping [ label = \"up\" ];\n" +
                "Jumping -> Diving [ label = \"down\" ];\n" +
                "{Jumping, Diving} -> Standing;\n" +
                "Standing -> Crouching [ label = \"down\" ];\n" +
                "Crouching -> Standing [ label = \"release\" ];\n" +
                "}";
        Result nonTree = fileParser().parse(text);

        System.out.println(nonTree.get().toString());
    }
}
