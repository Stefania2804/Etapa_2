package org.poo.account;

public final class Client {
    private String name;
    private int timestamp;
    private String email;
    public Client(final String name, final int timestamp,
                  final String email) {
        this.name = name;
        this.timestamp = timestamp;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
