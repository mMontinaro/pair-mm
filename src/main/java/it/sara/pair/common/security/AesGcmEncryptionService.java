package it.sara.pair.common.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import it.sara.pair.common.constants.CryptographyConstants;
import it.sara.pair.common.constants.Messages;
import it.sara.pair.common.error.CryptographyException;

/**
 * AesGcmEncryptionService AI Generated class - human reviewwed for correct implementation. This class is responsible for encrypting and decrypting sensitive values using AES-GCM algorithm.
 * It implements the EncryptionService interface and provides methods for encrypting and decrypting individual sensitive values
 * Stored representation: v1:Base64(nonce[12] || ciphertext || tag[16]).
 * An invalid key prevents the application from starting. 
 */
public class AesGcmEncryptionService implements EncryptionService {
    private final SecretKeySpec key;
    private final SecureRandom secureRandom = new SecureRandom();

    public AesGcmEncryptionService(
            @Value("${" + CryptographyConstants.KEY_PROPERTY_NAME + "}") 
            String encodedKey) {
        byte[] raw;
        try {
            raw = java.util.Base64.getDecoder().decode(encodedKey);
        } catch (IllegalArgumentException e) {
            throw new CryptographyException(Messages.Cryptography.INVALID_KEY);
        }
        if (raw.length != 32) { // AES-256 requires a 32-byte key
            java.util.Arrays.fill(raw, (byte) 0);
            throw new CryptographyException(Messages.Cryptography.INVALID_KEY);
        }
        this.key = new SecretKeySpec(raw, "AES");
        java.util.Arrays.fill(raw, (byte) 0);
    }
    
    @Override
    public String encrypt(String plaintext) {
        if (plaintext == null) {
            throw new CryptographyException(Messages.Cryptography.NULL_VALUE);
        }
        byte[] nonce = new byte[CryptographyConstants.AES_GCM_NONCE_SIZE_BYTES];
        secureRandom.nextBytes(nonce);
        try{
            Cipher cipher = Cipher.getInstance(CryptographyConstants.AES_GCM_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(CryptographyConstants.AES_GCM_TAG_SIZE_BITS, nonce));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            // Combine nonce, ciphertext, and tag into a single payload
            byte[] payload = new byte[nonce.length + ciphertext.length];
            System.arraycopy(nonce, 0, payload, 0, nonce.length);
            System.arraycopy(ciphertext, 0, payload, nonce.length, ciphertext.length);
            return CryptographyConstants.AES_GCM_VERSION_PREFIX + Base64.getEncoder().encodeToString(payload);
        } catch (Exception e) {
            throw new CryptographyException(Messages.Cryptography.ENCRYPTION_FAILED);
        }
    }

    @Override
    public String decrypt(String payload) {
        if (payload == null) {
            throw new CryptographyException(Messages.Cryptography.NULL_VALUE);
        }
        byte[] decodedPayload;
        try {
            decodedPayload = Base64
            .getDecoder().decode(
                payload.substring(
                    CryptographyConstants.AES_GCM_VERSION_PREFIX.length()));
        } catch (IllegalArgumentException e) {
            throw new CryptographyException(Messages.Cryptography.INVALID_PAYLOAD);
        }

        if(decodedPayload.length < CryptographyConstants.AES_GCM_NONCE_SIZE_BYTES + CryptographyConstants.AES_GCM_TAG_SIZE_BYTES) {
            throw new CryptographyException(Messages.Cryptography.INVALID_PAYLOAD);
        }

        try{
            byte[] nonce = Arrays.copyOfRange(decodedPayload, 0, CryptographyConstants.AES_GCM_NONCE_SIZE_BYTES);
            byte[] ciphertext = Arrays.copyOfRange(decodedPayload, CryptographyConstants.AES_GCM_NONCE_SIZE_BYTES, decodedPayload.length);

            Cipher cipher = Cipher.getInstance(CryptographyConstants.AES_GCM_ALGORITHM);
            cipher.init(
                Cipher.DECRYPT_MODE, 
                key, 
                new GCMParameterSpec(
                    CryptographyConstants.AES_GCM_TAG_SIZE_BITS, 
                    nonce
                ));
            byte[] plaintextBytes = cipher.doFinal(ciphertext);
            return new String(plaintextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CryptographyException(Messages.Cryptography.DECRYPTION_FAILED);
        }
    }
    
}
