package it.sara.pair.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import it.sara.pair.enums.ItemType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table(name = "vault_item")
public class VaultItem {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column (name = "url", length = 2048)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column (name = "item_type", nullable = false, length = 50)
    private ItemType itemType;

    @OneToMany(mappedBy = "vaultItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemField> fields = new ArrayList<>();

    public void addField(ItemField field) {
        fields.add(field);
        field.setVaultItem(this);
    }

    protected VaultItem() {
        // Default constructor for JPA
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public List<ItemField> getFields() {
        return fields;
    }

    public void setFields(List<ItemField> fields) {
        this.fields = fields;
    }

    
}
