package it.sara.pair.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "item_field")
public class ItemField {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "field_key", nullable = false, length = 255)
    private String key;

    @Column(name = "field_value", columnDefinition = "CLOB")
    private String value;

    @Column(name = "isSensitive", nullable = false)
    private boolean isSensitive;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vault_item_id", nullable = false)
    private VaultItem vaultItem;

    protected ItemField() {
        // Default constructor for JPA
    }

    public ItemField(String key, String value, boolean isSensitive) {
        this.key = key;
        this.value = value;
        this.isSensitive = isSensitive;
    }

    public ItemField(String key, String value, boolean isSensitive, VaultItem vaultItem) {
        this.key = key;
        this.value = value;
        this.isSensitive = isSensitive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSensitive() {
        return isSensitive;
    }

    public void setSensitive(boolean isSensitive) {
        this.isSensitive = isSensitive;
    }

    public VaultItem getVaultItem() {
        return vaultItem;
    }

    public void setVaultItem(VaultItem vaultItem) {
        this.vaultItem = vaultItem;
    }

}
