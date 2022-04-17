package non.utils;

import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import static org.petitparser.parser.primitive.CharacterParser.letter;
import static org.petitparser.parser.primitive.CharacterParser.of;

public class ParserGenerator {

    private static final Parser optionalSpaceParser = of(' ').star();
    private static final Parser dotParser = CharacterParser.of('.');
    private static final Parser doubleDotParser = CharacterParser.of(':');
    private static final Parser apostropheParser = CharacterParser.of('\'');
    private static final Parser atLeastOneLetter = CharacterParser.letter().seq(letter().star());

    public static Parser idParser() {
        Parser idParser = atLeastOneLetter
                .seq(doubleDotParser,optionalSpaceParser,atLeastOneLetter.star())
                .flatten()
                .map(res -> res.toString().replace(":", ""))
                .seq(optionalSpaceParser,of('\n').star())
                .flatten()
                .map(res -> res.toString().replace("\n", ""))
                .map(res -> res.toString().replace(" ", ""));
        return idParser;
    }

    public static Parser stringParser() {
        // Parseur chaine de caractères (consomme string+espaces)
        Parser stringParser = apostropheParser
                .seq(CharacterParser.noneOf("'"))
                .seq(CharacterParser.noneOf("'").star())
                .seq(apostropheParser).flatten()
                .seq(optionalSpaceParser).pick(0);
        return stringParser;
    }

    public static Parser selfParser() {
        return CharacterParser.of('@').seq(optionalSpaceParser).pick(0);
    }

    public static Parser fieldLocalRefParser() {
        return dotParser.seq(atLeastOneLetter, optionalSpaceParser).flatten();
    }

    public static Parser fieldGlobalRefParser() {
        return atLeastOneLetter.seq(dotParser).seq(atLeastOneLetter, optionalSpaceParser).flatten();
    }

    public static Parser fieldParser() {
        Parser fieldParser = fieldLocalRefParser().seq(
                selfParser().or(fieldLocalRefParser())
                        .or(stringParser())
                        .or(selfParser())
                        .or(fieldGlobalRefParser()).star()).seq(of('\n')).flatten().map(res -> res.toString().replace("\n", ""));
        return fieldParser;
    }

    public static Parser nonObjectParser() {
        return idParser().seq(fieldParser(), fieldParser().star()).or(idParser().seq(fieldParser().star()));
    }

    public static Parser nonTreeParser() {
        return nonObjectParser().star().end();
    }

    public static Parser GetGlobalParser(){
        return nonTreeParser();
    }
}
