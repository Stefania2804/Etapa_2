package org.poo.bank;

public final class Exchange {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    public Exchange(final String from, final String to,
                    final double rate, final int timestamp) {
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
}
