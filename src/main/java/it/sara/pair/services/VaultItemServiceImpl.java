package it.sara.pair.services;

import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.sara.pair.common.constants.SearchConstants;
import it.sara.pair.common.service.BaseServiceImpl;
import it.sara.pair.model.VaultItem;
import it.sara.pair.repositories.VaultItemRepository;

@Service 
public class VaultItemServiceImpl extends BaseServiceImpl<VaultItem, UUID> implements VaultItemService {

    private final VaultItemRepository repo;

    public VaultItemServiceImpl(VaultItemRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    @Transactional (readOnly = true)
    public Page<VaultItem> search(String query, int offset, int pageSize, Direction direction) {
        Sort sort = Sort.by(direction, SearchConstants.TITLE)
                        .and(Sort.by(SearchConstants.ID));
        if (offset < 0 || pageSize < 1 || sort == null) {
            throw new IllegalArgumentException();
        }
        Pageable pageable = PageRequest.of(offset / pageSize, pageSize, sort);
        String normalized = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        Specification<VaultItem> specification = (root, criteriaQuery, cb) -> {
            if (normalized.isEmpty()) return cb.conjunction();
            // LIKE escaping avoids treating user-supplied % and _ as wildcard operators.
            String escaped = normalized.replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
            String pattern = "%" + escaped + "%";
            return cb.or(
                    cb.like(cb.lower(root.get(SearchConstants.TITLE)), pattern, '\\'),
                    cb.like(cb.lower(root.get(SearchConstants.URL)), pattern, '\\')
            );
        };
        return repo.findAll(specification, pageable);
    }

}
