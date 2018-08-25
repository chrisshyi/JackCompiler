package symboltable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import main.MemorySegment;
import symbol.Symbol;
import symbol.SymbolKind;

public abstract class SymbolTable {

    Map<String, Symbol> table;

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
    public Optional<Symbol> lookUp(String varName) {
        if (this.table.containsKey(varName)) {
            return Optional.of(this.table.get(varName));
        }
        return Optional.empty();
    }

    /**
     * Checks if the symbol table contains a symbol
     * @param varName the symbol to check
     * @return true if the symbol is present, false if otherwise
     */
    public boolean hasSymbol(String varName) {
        return this.table.containsKey(varName);
    }
}
