package it.sara.pair.common.security;

import org.springframework.stereotype.Service;

@Service 
public class VaultSecretService {
    private final EncryptionService service;

    public VaultSecretService(EncryptionService service) {
        this.service = service;
    }

    public String protect(String plaintext) {
        return service.encrypt(plaintext);
    }

    public String reveal(String encrypted) {
        return service.decrypt(encrypted);
    }

    public String storeFieldValue(String value, boolean sensitive) {
        return sensitive && value != null ? service.encrypt(value) : value;
    }

    public String readFieldValue(String storedValue, boolean sensitive) {
        return sensitive && storedValue != null ? service.decrypt(storedValue) : storedValue;
    }
}
