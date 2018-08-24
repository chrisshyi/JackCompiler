public class Symbol {

    private String dataType; // the data type of the symbol, e.g. int, boolean, char, etc
    private SymbolKind symbolKind; // the kind of symbol, e.g. local, argument, field, etc
    private String symbolName;
    private int numKind; // Of all the symbols of this kind, which one is this?

    public Symbol(String dataType, SymbolKind symbolKind, String symbolName, int numKind) {
        this.dataType = dataType;
        this.symbolKind = symbolKind;
        this.symbolName = symbolName;
        this.numKind = numKind;
    }

    public String getDataType() {
        return dataType;
    }

    public SymbolKind getSymbolKind() {
        return symbolKind;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public int getNumKind() {
        return numKind;
    }
}
