package org.poo.account.card;

public class Card {
    private String cardNumber;
    private String status;

    public Card(final String cardNumber,
                final String status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }
    /**
     * Getter pentru numarul cardului curent.
     *
     */
    public String getCardNumber() {
        return cardNumber;
    }
    /**
     * Seteaza numarul cardului.
     *
     */
    public void setCardNumber(final String cardNUmber) {
        this.cardNumber = cardNUmber;
    }
    /**
     * Getter pentru status.
     *
     */
    public String getStatus() {
        return status;
    }
    /**
     * Seteaza statusul cardului.
     *
     */
    public void setStatus(final String status) {
        this.status = status;
    }

}
