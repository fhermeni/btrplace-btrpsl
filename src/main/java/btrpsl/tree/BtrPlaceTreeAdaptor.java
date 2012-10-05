/*
 * Copyright (c) Fabien Hermenier
 *
 *        This file is part of Entropy.
 *
 *        Entropy is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU Lesser General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *
 *        Entropy is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU Lesser General Public License for more details.
 *
 *        You should have received a copy of the GNU Lesser General Public License
 *        along with Entropy.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrpsl.tree;

import btrpsl.ANTLRBtrplaceSL2Lexer;
import btrpsl.BtrPlaceVJob;
import btrpsl.DefaultErrorReporter;
import btrpsl.SymbolsTable;
import btrpsl.constraint.ConstraintsCatalog;
import btrpsl.element.BtrpOperand;
import btrpsl.includes.Includes;
import entropy.vjob.builder.VJobElementBuilder;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

/**
 * An adapter to instantiate the right VJobTree depending on the token.
 *
 * @author Fabien Hermenier
 */
public class BtrPlaceTreeAdaptor extends CommonTreeAdaptor {

    private DefaultErrorReporter errors;

    private SymbolsTable symbols;

    private ConstraintsCatalog catalog;

    private VJobElementBuilder elementBuilder;

    private Includes includes;

    private BtrPlaceVJob vjob;

    /**
     * Build a new adaptor.
     *
     * @param errs the errors to report
     * @param s    the symbol table to use
     */
    public BtrPlaceTreeAdaptor(BtrPlaceVJob vjob, DefaultErrorReporter errs, SymbolsTable s, VJobElementBuilder eb, Includes incs, ConstraintsCatalog cat) {
        this.errors = errs;
        this.symbols = s;
        this.catalog = cat;
        this.elementBuilder = eb;
        this.includes = incs;
        this.vjob = vjob;
    }


    @Override
    public Object create(Token payload) {
        if (payload == null) {
            return new BtrPlaceTree(payload, errors);
        }
        switch (payload.getType()) {
            case ANTLRBtrplaceSL2Lexer.RANGE:
                return new Range(payload, errors);
            case ANTLRBtrplaceSL2Lexer.ENUM_VAR:
                return new EnumVar(payload, symbols, errors);
            case ANTLRBtrplaceSL2Lexer.ENUM_FQDN:
                return new EnumElement(payload, vjob, elementBuilder, BtrpOperand.Type.node, errors);
            case ANTLRBtrplaceSL2Lexer.ENUM_ID:
                return new EnumElement(payload, vjob, elementBuilder, BtrpOperand.Type.vm, errors);
            case ANTLRBtrplaceSL2Lexer.AND:
                return new AndOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.OR:
                return new OrOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.BLOCK:
                return new BlockStatement(payload, errors);
            case ANTLRBtrplaceSL2Lexer.FLOAT:
            case ANTLRBtrplaceSL2Lexer.OCTAL:
            case ANTLRBtrplaceSL2Lexer.DECIMAL:
            case ANTLRBtrplaceSL2Lexer.HEXA:
                return new NumberTree(payload, errors);
            case ANTLRBtrplaceSL2Lexer.STRING:
                return new StringTree(payload, errors);
            case ANTLRBtrplaceSL2Lexer.PLUS:
                return new AddOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.MINUS:
                return new MinusOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.TIMES:
                return new TimesOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.POWER:
                return new PowerOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.EQUALS:
                return new AssignmentStatement(payload, errors, symbols);
            case ANTLRBtrplaceSL2Lexer.PLUS_EQUALS:
                return new SelfAssignmentStatement(SelfAssignmentStatement.Type.plus_equals, payload, errors, symbols);
            case ANTLRBtrplaceSL2Lexer.MINUS_EQUALS:
                return new SelfAssignmentStatement(SelfAssignmentStatement.Type.minus_equals, payload, errors, symbols);
            case ANTLRBtrplaceSL2Lexer.DIV_EQUALS:
                return new SelfAssignmentStatement(SelfAssignmentStatement.Type.div_equals, payload, errors, symbols);
            case ANTLRBtrplaceSL2Lexer.TIMES_EQUALS:
                return new SelfAssignmentStatement(SelfAssignmentStatement.Type.times_equals, payload, errors, symbols);
            case ANTLRBtrplaceSL2Lexer.REMAINDER_EQUALS:
                return new SelfAssignmentStatement(SelfAssignmentStatement.Type.remainder_equals, payload, errors, symbols);
            case ANTLRBtrplaceSL2Lexer.VARIABLE:
                return new VariableTree(payload, errors, symbols);
            case ANTLRBtrplaceSL2Lexer.DIV:
                return new DivideOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.REMAINDER:
                return new RemainderOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.EQ:
                return new EqComparisonOperator(payload, false, errors);
            case ANTLRBtrplaceSL2Lexer.NOT:
                return new NotOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.NEQ:
                return new EqComparisonOperator(payload, true, errors);
            case ANTLRBtrplaceSL2Lexer.GT:
                return new StrictComparisonOperator(payload, false, errors);
            case ANTLRBtrplaceSL2Lexer.LT:
                return new StrictComparisonOperator(payload, true, errors);
            case ANTLRBtrplaceSL2Lexer.LEQ:
                return new NonStrictComparisonOperator(payload, true, errors);
            case ANTLRBtrplaceSL2Lexer.GEQ:
                return new NonStrictComparisonOperator(payload, false, errors);
            case ANTLRBtrplaceSL2Lexer.IF:
                return new IfStatement(payload, symbols, errors);
            case ANTLRBtrplaceSL2Lexer.FOR:
                return new ForStatement(payload, symbols, errors);
            case ANTLRBtrplaceSL2Lexer.IDENTIFIER:
                return new ElementTree(payload, vjob, errors, elementBuilder);
            case ANTLRBtrplaceSL2Lexer.NODE_NAME:
                return new ElementTree(payload, vjob, errors, elementBuilder);
            case ANTLRBtrplaceSL2Lexer.EXPLODED_SET:
                return new ExplodedSetTree(payload, errors);
            case ANTLRBtrplaceSL2Lexer.CARDINALITY:
                return new CardinalityOperator(payload, errors);
            case ANTLRBtrplaceSL2Lexer.CONSTRAINTIDENTIFIER:
                return new ConstraintStatement(payload, vjob, catalog, errors);
            case ANTLRBtrplaceSL2Lexer.TYPE_DEFINITION:
                return new TemplateAssignment(payload, vjob, elementBuilder, symbols, errors);
            case ANTLRBtrplaceSL2Lexer.EXPORT:
                return new ExportStatement(payload, vjob, errors);
            case ANTLRBtrplaceSL2Lexer.USE:
                return new ImportStatement(payload, includes, symbols, vjob, errors);
            case ANTLRBtrplaceSL2Lexer.NAMESPACE:
                return new NameSpaceStatement(payload, vjob, errors);
            case ANTLRBtrplaceSL2Lexer.TEMPLATE_OPTION:
                return new TemplateOptionTree(payload, errors);
            case ANTLRBtrplaceSL2Lexer.BLANK:
            default:
                return new BtrPlaceTree(payload, errors);
        }
    }
}
