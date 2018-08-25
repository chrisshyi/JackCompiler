package main;

public class ExpressionList {

    private String vmCode; // the compiled VM code for this expression list
    private int numExpressions; // number of expressions, needed for subroutine calls (nArgs)

    public ExpressionList(String vmCode, int numExpressions) {
        this.vmCode = vmCode;
        this.numExpressions = numExpressions;
    }

    public String getVmCode() {
        return vmCode;
    }

    public int getNumExpressions() {
        return numExpressions;
    }
}
