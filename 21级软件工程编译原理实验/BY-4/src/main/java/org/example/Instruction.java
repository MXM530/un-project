package org.example;

public class Instruction {
    private String op;
    private int r, d, s;
    private String comment;

    public Instruction(String op, int r, int d, int s, String comment) {
        this.op = op;
        this.r = r;
        this.d = d;
        this.s = s;
        this.comment = comment;
    }

    public void setD(int d) {
        this.d = d;
    }

    public String getOp() {
        return op;
    }

    public int getR() {
        return r;
    }

    public int getD() {
        return d;
    }

    public int getS() {
        return s;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return String.format("%5s %d,%d(%d) %s", op, r, d, s, comment);
    }

    public void setOp(String op) {
        this.op = op;
    }


}
