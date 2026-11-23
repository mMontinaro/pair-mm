package it.sara.pair.common.constants;

public final class General {
    public static final String TRACE_ID = "TRACE_ID";

    public final static class Seed {
        private Seed() { }
        public static final String JSON_RESOURCE = "seed/vault-items.json";
        public static final String MSG_EMPTY_RESOURCE = "Seed resource contains no vault items";
        public static final String MSG_READ_FAILURE = "Cannot read vault seed resource";
        public static final int ITEMS_PER_TYPE = 10;
        public static final String WEB_TITLE_PREFIX = "Demo Web Account ";
        public static final String WEB_URL_PREFIX = "https://example.com/account/";
        public static final String WEB_USERNAME_PREFIX = "demo-user-";
        public static final String WEB_PASSWORD_PREFIX = "demo-password-";
        public static final String PERSONAL_TITLE_PREFIX = "Demo Personal Information ";
        public static final String MEMBERSHIP_KEY = "Membership number";
        public static final String MEMBERSHIP_PREFIX = "DEMO-MEMBER-";
        public static final String PRIVATE_NOTE_KEY = "Private note";
        public static final String PRIVATE_NOTE_PREFIX = "private-note-";
        public static final String CARD_TITLE_PREFIX = "Demo Payment Card ";
        public static final String CARDHOLDER = "DEMO USER";
        public static final String TEST_CARD_NUMBER = "4111111111111111";
        public static final String TEST_CVV = "123";
        public static final String EXPIRY = "12/30";
        public static final String PIN_KEY = "PIN";
        public static final String DEMO_PIN = "1234";
    }
}
