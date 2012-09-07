package btrpsl.constraint;

import btrpsl.SemanticErrors;
import btrpsl.tree.BtrPlaceTree;
import entropy.vjob.DefaultVJob;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.Token;

/**
 * Created with IntelliJ IDEA.
 * User: fhermeni
 * Date: 07/09/12
 * Time: 11:34
 * To change this template use File | Settings | File Templates.
 */
public class MockBtrPlaceTree extends BtrPlaceTree {

    public MockBtrPlaceTree() {
        super(new MockToken(), new SemanticErrors(new DefaultVJob("f")));
    }

    static class MockToken implements Token {
        @Override
        public String getText() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setText(String s) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getType() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setType(int i) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getLine() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setLine(int i) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getCharPositionInLine() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setCharPositionInLine(int i) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getChannel() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setChannel(int i) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getTokenIndex() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setTokenIndex(int i) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public CharStream getInputStream() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setInputStream(CharStream charStream) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
