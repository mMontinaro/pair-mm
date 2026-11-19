package it.sara.pair.common.constants;

public final class ValidationRules {
    public static final int ITEM_NOT_FOUND_CODE = 418;
    public static final String CARD_NUMBER_REGEX = "^[0-9]{13,19}$";
    public static final String CVV_REGEX = "^[0-9]{3,4}$";
    public static final String EXPIRY_DATE_REGEX = "^(0[1-9]|1[0-2])\\/([0-9]{2})$";
    public static final Integer MAX_SEARCH_PAGE_SIZE = 20;
}
