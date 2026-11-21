package it.sara.pair.model;

import it.sara.pair.enums.ItemType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity 
@Table (name = "personal_information_item")
public class PersonalInformationItem extends VaultItem {

    @Override
    public ItemType getItemType() {
        return ItemType.PERSONAL_INFORMATION;
    }
    
    public PersonalInformationItem() {
        // Default constructor for JPA
    }
}
