package non.visitor;

import non.*;

public interface Visitor {

    String visitStringValue(NONStringValue stringValue);

    String visitObject(NONObject object);

    String visitLocalFieldValue(NONLocalFieldValue localFieldValue);

    String visitGlobalFieldValue(NONGlobalFieldValue globalFieldValue);

    String visitConcatValue(NONConcatValue concatValue);

    String serializeObject(NONObject nonObject);

    String serializeNON(NONDefs non);


}
