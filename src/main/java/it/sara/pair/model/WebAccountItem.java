package it.sara.pair.model;

import it.sara.pair.enums.ItemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity 
@Table (name = "web_account_item")
public class WebAccountItem extends VaultItem {
    
    @Column (nullable = false, length = 255)
    private String username;

    @Column (nullable = false, length = 255)
    private String password;

    @Override
    public ItemType getItemType() {
        return ItemType.WEB_ACCOUNT;
    }

    public WebAccountItem() {
        // Default constructor for JPA
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
