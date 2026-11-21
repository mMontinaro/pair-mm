package it.sara.pair.common.security;

public interface EncryptionService {
    String encrypt(String plaintext);
    String decrypt(String payload);
}
