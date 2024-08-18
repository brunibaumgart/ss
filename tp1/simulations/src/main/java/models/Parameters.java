package models;

public class Parameters implements Cloneable {
    private int N;
    private double L;
    private double rc;
    private int M;
    private double r;
    private boolean isPeriodic;
    private String method;
    private int runs;
    private int steps;
    private boolean plotTimeVsN;
    private boolean plotTimeVsM;

    private int plotTimeVsMIterations;

    public Parameters() {
    }

    public Parameters(int n, double l, double rc, int m, double r, boolean isPeriodic, String method, int runs, int steps, boolean plotTimeVsN, boolean plotTimeVsM
    , int plotTimeVsMIterations) {
        this.N = n;
        this.L = l;
        this.rc = rc;
        this.M = m;
        this.r = r;
        this.isPeriodic = isPeriodic;
        this.method = method;
        this.runs = runs;
        this.steps = steps;
        this.plotTimeVsN = plotTimeVsN;
        this.plotTimeVsM = plotTimeVsM;
        this.plotTimeVsMIterations = plotTimeVsMIterations;
    }

    @Override
    public Parameters clone() {
        return new Parameters(
                this.N,
                this.L,
                this.rc,
                this.M,
                this.r,
                this.isPeriodic,
                this.method,
                this.runs,
                this.steps,
                this.plotTimeVsN,
                this.plotTimeVsM,
                this.plotTimeVsMIterations
        );
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
        if(this.L / m < this.rc + this.r)
            throw new IllegalArgumentException("The cutoff radius + radius of particles must be smaller than L/M, M was " + m);

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

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public boolean isPlotTimeVsN() {
        return plotTimeVsN;
    }

    public void setPlotTimeVsN(boolean plotTimeVsN) {
        this.plotTimeVsN = plotTimeVsN;
    }

    public boolean isPlotTimeVsM() {
        return plotTimeVsM;
    }

    public void setPlotTimeVsM(boolean plotTimeVsM) {
        this.plotTimeVsM = plotTimeVsM;
    }

    public int getPlotTimeVsMIterations() {
        return plotTimeVsMIterations;
    }

    public void setPlotTimeVsMIterations(int plotTimeVsMIterations) {
        this.plotTimeVsMIterations = plotTimeVsMIterations;
    }
}
