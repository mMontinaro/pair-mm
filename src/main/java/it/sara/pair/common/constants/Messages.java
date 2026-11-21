package it.sara.pair.common.constants;

public final class Messages {
    private Messages() {}
    public final static String ITEM_NOT_FOUND = "Item not found";
    public final static String VALIDATION_FAILED = "Validation failed";
    public final static String UNEXPECTED_ERROR = "An unexpected error occurred";
    public final static String SUCCESS = "Success";

    public static final class Validation {
        private Validation() {}
        public final static String TITLE_REQUIRED = "Title is required";
        public final static String TYPE_REQUIRED = "Type is required";
        public final static String USERNAME_REQUIRED = "Username is required";
        public final static String PASSWORD_REQUIRED = "Password is required";
        public final static String CARDHOLDER_NAME_REQUIRED = "Cardholder name is required";
        public final static String CARD_NUMBER_NOT_VALID = "Card number is not valid";
        public final static String CVV_NOT_VALID = "CVV is not valid";
        public final static String EXPIRY_DATE_NOT_VALID = "Expiry date is not valid";
        public final static String REQUEST_REQUIRED = "Request is required";
    }

    public final static class Cryptography {
        private Cryptography() {}
        public final static String INVALID_KEY = "Invalid encryption key";
        public final static String ENCRYPTION_FAILED = "Encryption failed";
        public final static String DECRYPTION_FAILED = "Decryption failed";
        public final static String INVALID_PAYLOAD = "Invalid encrypted payload";
        public final static String NULL_VALUE = "Sensitive value cannot be null";
    }
}