package it.sara.pair.common.constants;

public final class CryptographyConstants {
    private CryptographyConstants() {
        // Private constructor to prevent instantiation
    }
    public static final String KEY_PROPERTY_NAME = "KEY_BASE_64";
    public static final String AES_GCM_ALGORITHM = "AES/GCM/NoPadding";
    public static final String AES_KEY_ALGORITHM = "AES";
    public static final String AES_GCM_VERSION_PREFIX = "v1:";
    public static final int AES_KEY_SIZE_BYTES = 32; // in bytes
    public static final int AES_KEY_SIZE = 256; // in bits
    public static final int AES_GCM_NONCE_SIZE_BYTES = 12; // in bytes AES-GCM standard nonce size
    public static final int AES_GCM_TAG_SIZE_BITS = 128; // in bits
    public static final int AES_GCM_TAG_SIZE_BYTES = AES_GCM_TAG_SIZE_BITS / Byte.SIZE; // in bytes
}
