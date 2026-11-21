package it.sara.pair.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import it.sara.pair.enums.ItemType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table(name = "vault_item")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VaultItem {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column (name = "url", length = 2048)
    private String url;

    @OneToMany(mappedBy = "vaultItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemField> fields = new ArrayList<>();

    public abstract ItemType getItemType();
    
    public void addField(ItemField field) {
        fields.add(field);
        field.setVaultItem(this);
    }

    public void removeField(ItemField field) {
        fields.remove(field);
        field.setVaultItem(null);
    }

    public UUID getId() {
        return id;
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

    public List<ItemField> getFields() {
        return fields;
    }

    public void setFields(List<ItemField> newFields) {
        List<ItemField> copy = new ArrayList<>(newFields);
        
        for(ItemField field : new ArrayList<>(fields)) {
            removeField(field);
        }

        for(ItemField field : copy) {
            addField(field);
        }
    }
    
}
