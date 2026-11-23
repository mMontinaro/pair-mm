package it.sara.pair.seed;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import it.sara.pair.dto.ItemDetailDto;
import it.sara.pair.facades.VaultItemFacade;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.sara.pair.common.constants.General;
import it.sara.pair.common.security.VaultSecretService;
import it.sara.pair.model.ItemField;
import it.sara.pair.model.PaymentCardItem;
import it.sara.pair.model.PersonalInformationItem;
import it.sara.pair.model.WebAccountItem;
import it.sara.pair.repositories.VaultItemRepository;

/** 
 * AI GENERATED CLASS
 * Seeds 30 development-only records; never stores plaintext secrets. */
@Component 
@ConditionalOnProperty (prefix = "pair.seed", name = "enabled", havingValue = "true", matchIfMissing = true)
public class VaultItemSeeder implements CommandLineRunner  {
    private final VaultItemRepository repository;
    private final VaultSecretService secrets;
    private final VaultItemFacade facade;
    private final JsonMapper jsonMapper;


    public VaultItemSeeder(
            VaultItemRepository repository,
            VaultSecretService secrets,
            VaultItemFacade facade,
            JsonMapper jsonMapper) {
        this.repository = repository;
        this.secrets = secrets;
        this.facade = facade;
        this.jsonMapper = jsonMapper;
    }

    @Override
    @Transactional 
    public void run(String... args) {
        if (repository.count() > 0) return;
        for (int number = 1; number <= General.Seed.ITEMS_PER_TYPE; number++) {
            seedWeb(number);
            seedPersonal(number);
            seedCard(number);
        }
        seedJsonExamples();
    }

    private void seedJsonExamples() {
        ClassPathResource resource = new ClassPathResource(General.Seed.JSON_RESOURCE);
        try (InputStream stream = resource.getInputStream()) {
            List<ItemDetailDto> requests = jsonMapper.readValue(
                    stream, new TypeReference<List<ItemDetailDto>>() {});
            if (requests == null || requests.isEmpty()) {
                throw new IllegalStateException(General.Seed.MSG_EMPTY_RESOURCE);
            }
            for (ItemDetailDto request : requests) {
                facade.create(request);
            }
        } catch (IOException e) {
            throw new IllegalStateException(General.Seed.MSG_READ_FAILURE, e);
        }
    }

    private void seedWeb(int n) {
        WebAccountItem item = new WebAccountItem();
        item.setTitle(General.Seed.WEB_TITLE_PREFIX + n);
        item.setUrl(General.Seed.WEB_URL_PREFIX + n);
        item.setUsername(General.Seed.WEB_USERNAME_PREFIX + n);
        item.setPassword(secrets.protect(General.Seed.WEB_PASSWORD_PREFIX + n));
        repository.save(item);
    }

    private void seedPersonal(int n) {
        PersonalInformationItem item = new PersonalInformationItem();
        item.setTitle(General.Seed.PERSONAL_TITLE_PREFIX + n);
        item.addField(field(General.Seed.MEMBERSHIP_KEY,
                General.Seed.MEMBERSHIP_PREFIX + n, false));
        item.addField(field(General.Seed.PRIVATE_NOTE_KEY,
                General.Seed.PRIVATE_NOTE_PREFIX + n, true));
        repository.save(item);
    }

    private void seedCard(int n) {
        PaymentCardItem item = new PaymentCardItem();
        item.setTitle(General.Seed.CARD_TITLE_PREFIX + n);
        item.setCardHolder(General.Seed.CARDHOLDER);
        item.setCardNumber(secrets.protect(General.Seed.TEST_CARD_NUMBER));
        item.setCvv(secrets.protect(General.Seed.TEST_CVV));
        item.setExpirationDate(General.Seed.EXPIRY);
        item.addField(field(General.Seed.PIN_KEY, General.Seed.DEMO_PIN, true));
        repository.save(item);
    }

    private ItemField field(String key, String value, boolean sensitive) {
        return new ItemField(key, secrets.storeFieldValue(value, sensitive), sensitive);
    }
}
