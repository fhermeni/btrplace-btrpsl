package btrpsl.tree;

import btrpsl.DefaultErrorReporter;
import btrpsl.element.BtrpOperand;
import btrpsl.element.IgnorableOperand;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;

/**
 * Tree to handle errors returned by the lexer.
 * @author Fabien Hermenier
 */
public class ErrorTree extends BtrPlaceTree {

    private Token end;

    public ErrorTree(TokenStream input, Token start, Token stop, RecognitionException e) {
        super(start, null);
        end = stop;
    }

    @Override
    public int getLine() {
        return end.getLine();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getCharPositionInLine() {
        return end.getCharPositionInLine();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        return IgnorableOperand.getInstance();
    }
}
