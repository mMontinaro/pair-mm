package it.sara.pair.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import it.sara.pair.common.service.BaseService;
import it.sara.pair.model.VaultItem;

public interface VaultItemService extends BaseService<VaultItem, UUID> {
    Page<VaultItem> search(String query, int offset, int pageSize, Sort.Direction direction);
}
