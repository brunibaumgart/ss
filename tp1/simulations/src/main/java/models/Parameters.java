package models;

public class Parameters {
    private int N;
    private double L;
    private double rc;
    private int M;
    private double r;
    private boolean isPeriodic;
    private String method;

    public Parameters() {
    }

    public Parameters(int n, double l, double rc, int m, double r, boolean isPeriodic, String method) {
        this.N = n;
        this.L = l;
        this.rc = rc;
        this.M = m;
        this.r = r;
        this.isPeriodic = isPeriodic;
        this.method = method;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public double getRc() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isPeriodic() {
        return isPeriodic;
    }

    public void setPeriodic(boolean periodic) {
        isPeriodic = periodic;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
