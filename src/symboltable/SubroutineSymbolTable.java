package symboltable;

import symbol.Symbol;
import symbol.SymbolKind;

public class SubroutineSymbolTable extends SymbolTable {

    private int localVarCount = 0;
    private int argumentCount = 0;

    @Override
    public void define(String varName, String varType, SymbolKind varKind) {
        switch(varKind) {
            case LOCAL:
                this.table.put(varName, new Symbol(varType, varKind, varName, localVarCount));
                this.localVarCount++;
                break;
            case ARGUMENT:
                this.table.put(varName, new Symbol(varType, varKind, varName, argumentCount));
                this.argumentCount++;
                break;
        }
    }
}
