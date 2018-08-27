package symboltable;

import symbol.Symbol;
import symbol.SymbolKind;

public class ClassSymbolTable extends SymbolTable {

    private int staticVarCount = 0;
    private int fieldVarCount = 0;

    @Override
    public void define(String varName, String varType, SymbolKind varKind) {
        switch (varKind) {
            case STATIC:
                this.table.put(varName, new Symbol(varType, varKind, varName, staticVarCount));
                this.staticVarCount++;
                break;
            case FIELD:
                this.table.put(varName, new Symbol(varType, varKind, varName, fieldVarCount));
                this.fieldVarCount++;
                break;
        }
    }

    public int getFieldVarCount() {
        return fieldVarCount;
    }
}
