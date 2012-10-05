package btrpsl.constraint;

import btrpsl.DefaultErrorReporter;
import btrpsl.tree.BtrPlaceTree;
import entropy.vjob.DefaultVJob;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.Token;

/**
 * A mock BtrPlaceTree to ease PlacementConstraintBuilder tests.
 *
 * @author Fabien Hermenier
 */
public class MockBtrPlaceTree extends BtrPlaceTree {

    public MockBtrPlaceTree() {
        super(new MockToken(), new DefaultErrorReporter(new DefaultVJob("f")));
    }

    static class MockToken implements Token {
        @Override
        public String getText() {
            return null;
        }

        @Override
        public void setText(String s) {

        }

        @Override
        public int getType() {
            return 0;
        }

        @Override
        public void setType(int i) {

        }

        @Override
        public int getLine() {
            return 0;
        }

        @Override
        public void setLine(int i) {
        }

        @Override
        public int getCharPositionInLine() {
            return 0;
        }

        @Override
        public void setCharPositionInLine(int i) {

        }

        @Override
        public int getChannel() {
            return 0;
        }

        @Override
        public void setChannel(int i) {

        }

        @Override
        public int getTokenIndex() {
            return 0;
        }

        @Override
        public void setTokenIndex(int i) {

        }

        @Override
        public CharStream getInputStream() {
            return null;
        }

        @Override
        public void setInputStream(CharStream charStream) {

        }
    }
}
