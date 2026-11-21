package it.sara.pair.model;

import it.sara.pair.enums.ItemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity 
@Table (name = "payment_card_item")
public class PaymentCardItem extends VaultItem {

    @Column (nullable = false, length = 255)
    private String cardHolder;

    @Column (nullable = false, length = 255)
    private String cardNumber;

    @Column (nullable = false, length = 255)
    private String cvv;

    @Column (nullable = false, length = 255)
    private String expirationDate;

    @Override
    public ItemType getItemType() {
        return ItemType.PAYMENT_CARD;
    }
    
    public PaymentCardItem() {
        // Default constructor for JPA
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
