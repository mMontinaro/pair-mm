package it.sara.pair.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.sara.pair.common.repository.BaseRepository;
import it.sara.pair.model.VaultItem;

public interface VaultItemRepository extends BaseRepository<VaultItem, UUID>, JpaSpecificationExecutor<VaultItem> {
    
}
