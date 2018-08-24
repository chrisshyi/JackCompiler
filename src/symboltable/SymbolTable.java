package symboltable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import symbol.Symbol;
import symbol.SymbolKind;

public abstract class SymbolTable {

    private Map<String, Symbol> table;

    public SymbolTable() {
        this.table = new HashMap<>();
    }

    /**
     * Resets the symbol table, use when starting compile a new subroutine
     */
    public void reset() {
        this.table = new HashMap<>();
    }

    /**
     * Adds a new symbol to the symbol table
     * @param varName name of the symbol
     * @param varType the type of the symbol
     * @param varKind what kind of symbol is it? (argument, local, etc)
     */
    public abstract void define(String varName, String varType, SymbolKind varKind);

    /**
     * Looks up a symbol
     * @param varName the name of the symbol
     * @return an Optional object, since the symbol may or may not exist
     */
    public abstract Optional<Symbol> lookUp(String varName);
}
