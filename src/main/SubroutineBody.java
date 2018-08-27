package main;

public class SubroutineBody {

    private int numLocals; // number of local variables, needed for subroutine declarations
    private String vmCode; // the compiled VM code for this subroutine body

    public SubroutineBody(int numLocals, String vmCode) {
        this.numLocals = numLocals;
        this.vmCode = vmCode;
    }

    public int getNumLocals() {
        return numLocals;
    }

    public String getVmCode() {
        return vmCode;
    }
}
