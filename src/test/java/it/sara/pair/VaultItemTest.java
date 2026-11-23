package it.sara.pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * AI GENERATED TEST CLASS
 * Black-box API integration tests. Each test starts from a VALID request and
 * changes ONE requirement at a time. Uses the real HTTP/controller/facade/
 * service/JPA/H2 pipeline; no separately running server is required.
 *
 * Assumptions documented in TESTING.md: fields/isSensitive JSON names; status/data
 * envelopes; ASC/DESC sorting; response detail includes decrypted secrets.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VaultItemApiIntegrationTest {

    private static final String BASE_PATH = "/item/v1/item";
    private static final String SEARCH_PATH = BASE_PATH + "/search";
    private static final String WEB_TYPE = "WEB_ACCOUNT";
    private static final String CARD_TYPE = "PAYMENT_CARD";
    private static final String PERSONAL_TYPE = "PERSONAL_INFORMATION";
    private static final String VALID_CARD = "4111111111111111"; // public test-only card
    private static final String INVALID_LUHN_CARD = "4111111111111112";
    private static final String VALID_PASSWORD = "ValidPass123!";
    private static final String VALID_USERNAME = "sample_user";
    private static final String VALID_CARDHOLDER = "Test Person";
    private static final String VALID_CVV = "123";
    private static final String VALID_FIELD = "{\"key\":\"Category\",\"value\":\"Demo\",\"isSensitive\":false}";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ObjectMapper json;

    @Autowired
    private JdbcTemplate jdbc;

    private final HttpClient client = HttpClient.newHttpClient();

    private String uniqueTitle() { return "Integration " + UUID.randomUUID(); }

    private String futureExpiry() {
        YearMonth date = YearMonth.now().plusYears(3);
        return "%02d/%02d".formatted(date.getMonthValue(), date.getYear() % 100);
    }

    private String web(String title, String username, String password) {
        return """
                {"type":"WEB_ACCOUNT","title":"%s","url":"https://example.com/login",
                 "username":"%s","password":"%s"}
                """.formatted(title, username, password);
    }

    private String webWithFields(String title, String fields) {
        String body = web(title, VALID_USERNAME, VALID_PASSWORD);
        return body.substring(0, body.lastIndexOf('}')) + ",\"fields\":" + fields + "}";
    }

    private String card(String title, String cardholder, String number, String cvv, String expiry) {
        return """
                {"type":"PAYMENT_CARD","title":"%s","cardholder":"%s",
                 "cardNumber":"%s","cvv":"%s","expiry":"%s"}
                """.formatted(title, cardholder, number, cvv, expiry);
    }

    private String personal(String title) {
        return """
                {"type":"PERSONAL_INFORMATION","title":"%s",
                 "fields":[{"key":"Membership","value":"TEST-100","isSensitive":false}]}
                """.formatted(title);
    }

    private String search(String query, int offset, int pageSize, String order) {
        return """
                {"search":"%s","offset":%d,"pageSize":%d,"order":"%s"}
                """.formatted(query, offset, pageSize, order);
    }

    private JsonNode send(String method, String path, String body) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Accept", "application/json");
        if (body != null) builder.header("Content-Type", "application/json");
        HttpRequest request = builder.method(method, body == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
        try {
            HttpResponse<String> result = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, result.statusCode(), "Actual HTTP status; body=" + result.body());
            JsonNode parsed = json.readTree(result.body());
            assertFalse(parsed.path("status").isMissingNode(), "Response envelope requires status");
            assertFalse(parsed.path("status").path("traceId").asText().isBlank(), "Missing trace ID");
            return parsed;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new AssertionError("HTTP call failed: " + method + " " + path, e);
        }
    }

    private JsonNode create(String body) { return send("PUT", BASE_PATH, body); }
    private JsonNode update(String id, String body) { return send("POST", BASE_PATH + "/" + id, body); }
    private JsonNode detail(String id) { return send("GET", BASE_PATH + "/" + id, null); }
    private JsonNode delete(String id) { return send("DELETE", BASE_PATH + "/" + id, null); }
    private JsonNode searchItems(String body) { return send("POST", SEARCH_PATH, body); }

    private void assertCode(JsonNode body, int expected) {
        assertEquals(expected, body.path("status").path("code").asInt(), body.toString());
    }

    private String idOfCreated(JsonNode body) {
        assertCode(body, 200);
        String id = body.path("data").path("id").asText();
        assertDoesNotThrow(() -> UUID.fromString(id), "Create must return a UUID identifier");
        return id;
    }

    // ---------------- CREATE: one variable per test ----------------

    @Test void title_valid_accepts() {
        idOfCreated(create(web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)));
    }

    @Test void title_blank_rejects() {
        assertCode(create(web("   ", VALID_USERNAME, VALID_PASSWORD)), 400);
    }

    @Test void type_valid_accepts() {
        idOfCreated(create(web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)));
    }

    @Test void type_missing_rejects() {
        String body = web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)
                .replace("\"type\":\"WEB_ACCOUNT\",", "");
        assertCode(create(body), 400);
    }

    @Test void username_valid_accepts() {
        idOfCreated(create(web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)));
    }

    @Test void username_blank_rejects() {
        assertCode(create(web(uniqueTitle(), " ", VALID_PASSWORD)), 400);
    }

    @Test void password_valid_accepts() {
        idOfCreated(create(web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)));
    }

    @Test void password_blank_rejects() {
        assertCode(create(web(uniqueTitle(), VALID_USERNAME, "")), 400);
    }

    @Test void personal_information_valid_accepts() {
        idOfCreated(create(personal(uniqueTitle())));
    }

    @Test void cardholder_valid_accepts() {
        idOfCreated(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, VALID_CVV, futureExpiry())));
    }

    @Test void cardholder_blank_rejects() {
        assertCode(create(card(uniqueTitle(), " ", VALID_CARD, VALID_CVV, futureExpiry())), 400);
    }

    @Test void card_number_luhn_valid_accepts() {
        idOfCreated(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, VALID_CVV, futureExpiry())));
    }

    @Test void card_number_invalid_checksum_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, INVALID_LUHN_CARD, VALID_CVV, futureExpiry())), 400);
    }

    @Test void card_number_too_short_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, "411111111111", VALID_CVV, futureExpiry())), 400);
    }

    @Test void card_number_too_long_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, "41111111111111111111", VALID_CVV, futureExpiry())), 400);
    }

    @Test void card_number_nondigits_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, "411111111111111A", VALID_CVV, futureExpiry())), 400);
    }

    @Test void cvv_valid_accepts() {
        idOfCreated(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, VALID_CVV, futureExpiry())));
    }

    @Test void cvv_two_digits_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, "12", futureExpiry())), 400);
    }

    @Test void cvv_five_digits_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, "12345", futureExpiry())), 400);
    }

    @Test void cvv_nondigits_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, "1A3", futureExpiry())), 400);
    }

    @Test void expiry_future_accepts() {
        idOfCreated(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, VALID_CVV, futureExpiry())));
    }

    @Test void expiry_past_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, VALID_CVV, "01/20")), 400);
    }

    @Test void expiry_month_zero_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, VALID_CVV, "00/30")), 400);
    }

    @Test void expiry_month_thirteen_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, VALID_CVV, "13/30")), 400);
    }

    @Test void expiry_wrong_format_rejects() {
        assertCode(create(card(uniqueTitle(), VALID_CARDHOLDER, VALID_CARD, VALID_CVV, "12-30")), 400);
    }

    @Test void fields_omitted_accepts() {
        idOfCreated(create(web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)));
    }

    @Test void fields_populated_accepts() {
        idOfCreated(create(webWithFields(uniqueTitle(), "[" + VALID_FIELD + "]")));
    }

    @Test void fields_empty_array_rejects() {
        assertCode(create(webWithFields(uniqueTitle(), "[]")), 400);
    }

    @Test void field_key_valid_accepts() {
        idOfCreated(create(webWithFields(uniqueTitle(), "[" + VALID_FIELD + "]")));
    }

    @Test void field_key_blank_rejects() {
        String bad = "{\"key\":\" \" ,\"value\":\"demo\",\"isSensitive\":false}";
        assertCode(create(webWithFields(uniqueTitle(), "[" + bad + "]")), 400);
    }

    // ---------------- SEARCH: isolated filter tests ----------------

    @Test void search_valid_accepts() {
        assertCode(searchItems(search("", 0, 10, "ASC")), 200);
    }

    @Test void search_negative_offset_rejects() {
        assertCode(searchItems(search("", -1, 10, "ASC")), 400);
    }

    @Test void search_offset_zero_accepts() {
        assertCode(searchItems(search("", 0, 10, "ASC")), 200);
    }

    @Test void search_page_size_twenty_accepts() {
        assertCode(searchItems(search("", 0, 20, "ASC")), 200);
    }

    @Test void search_page_size_twenty_one_rejects() {
        assertCode(searchItems(search("", 0, 21, "ASC")), 400);
    }

    @Test void search_page_size_zero_rejects() {
        assertCode(searchItems(search("", 0, 0, "ASC")), 400);
    }

    @Test void search_order_asc_accepts() {
        assertCode(searchItems(search("", 0, 10, "ASC")), 200);
    }

    @Test void search_finds_title_and_only_returns_safe_fields() {
        String title = uniqueTitle();
        String id = idOfCreated(create(web(title, VALID_USERNAME, VALID_PASSWORD)));
        JsonNode result = searchItems(search(title, 0, 20, "ASC"));
        assertCode(result, 200);
        assertTrue(result.path("total").asLong() >= 1);
        JsonNode matches = result.path("data");
        assertTrue(matches.isArray());
        boolean found = false;
        for (JsonNode item : matches) {
            if (id.equals(item.path("id").asText())) {
                found = true;
                assertEquals(WEB_TYPE, item.path("type").asText());
                assertEquals(title, item.path("title").asText());
                assertEquals("https://example.com/login", item.path("url").asText());
            }
            for (String forbidden : new String[]{"password", "cardNumber", "cvv", "fields", "username"}) {
                assertFalse(item.has(forbidden), "Search must omit " + forbidden);
            }
        }
        assertTrue(found, "Created item not found in search results");
    }

    @Test void search_matches_url() {
        String title = uniqueTitle();
        String id = idOfCreated(create(web(title, VALID_USERNAME, VALID_PASSWORD)));
        JsonNode result = searchItems(search("example.com/login", 0, 20, "ASC"));
        assertCode(result, 200);
        boolean found = false;
        for (JsonNode item : result.path("data")) {
            if (id.equals(item.path("id").asText())) found = true;
        }
        assertTrue(found, "Search must match URL as implemented by your service");
    }

    // ---------------- DETAIL / UPDATE / DELETE ----------------

    @Test void detail_existing_item_returns_decrypted_password() {
        String id = idOfCreated(create(web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)));
        JsonNode response = detail(id);
        assertCode(response, 200);
        assertEquals(VALID_PASSWORD, response.path("data").path("password").asText(),
                "Detail must contain plaintext (after decrypting into the DTO), not v1: ciphertext");
    }

    @Test void detail_missing_item_returns_418() {
        assertCode(detail(UUID.randomUUID().toString()), 418);
    }

    @Test void update_valid_accepts_and_preserves_id() {
        String id = idOfCreated(create(web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)));
        String newTitle = uniqueTitle();
        JsonNode response = update(id, web(newTitle, VALID_USERNAME, VALID_PASSWORD));
        assertCode(response, 200);
        JsonNode updated = detail(id);
        assertCode(updated, 200);
        assertEquals(id, updated.path("data").path("id").asText());
        assertEquals(newTitle, updated.path("data").path("title").asText());
    }

    @Test void update_blank_title_rejects_without_modifying_database() {
        String title = uniqueTitle();
        String id = idOfCreated(create(web(title, VALID_USERNAME, VALID_PASSWORD)));
        assertCode(update(id, web(" ", VALID_USERNAME, VALID_PASSWORD)), 400);
        assertEquals(title, detail(id).path("data").path("title").asText());
    }

    @Test void update_missing_item_returns_418() {
        assertCode(update(UUID.randomUUID().toString(), web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)), 418);
    }

    @Test void delete_existing_item_succeeds() {
        String id = idOfCreated(create(web(uniqueTitle(), VALID_USERNAME, VALID_PASSWORD)));
        assertCode(delete(id), 200);
        assertCode(detail(id), 418);
    }

    @Test void delete_missing_item_returns_418() {
        assertCode(delete(UUID.randomUUID().toString()), 418);
    }

    // ---------------- DATABASE / CRYPTO / ENVELOPES ----------------

    @Test void sensitive_field_encrypted_and_nonsensitive_field_not_encrypted() {
        String fields = """
                [{"key":"PrivateNote","value":"TOP-SECRET-123","isSensitive":true},
                 {"key":"PublicNote","value":"VISIBLE-123","isSensitive":false}]
                """;
        String id = idOfCreated(create(webWithFields(uniqueTitle(), fields)));
        var saved = jdbc.queryForList(
                "SELECT field_key, field_value, is_sensitive FROM item_field WHERE vault_item_id = ?",
                UUID.fromString(id));
        assertEquals(2, saved.size());
        for (var field : saved) {
            String key = (String) field.get("FIELD_KEY");
            String value = (String) field.get("FIELD_VALUE");
            if ("PrivateNote".equals(key)) {
                assertTrue(value.startsWith("v1:"));
                assertNotEquals("TOP-SECRET-123", value);
            } else if ("PublicNote".equals(key)) {
                assertEquals("VISIBLE-123", value);
            } else fail("Unexpected field: " + key);
        }
    }

    @Test void success_status_contains_trace_id() {
        JsonNode response = searchItems(search("", 0, 10, "ASC"));
        assertCode(response, 200);
        assertFalse(response.path("status").path("traceId").asText().isBlank());
    }

    @Test void error_status_contains_trace_id() {
        JsonNode response = detail(UUID.randomUUID().toString());
        assertCode(response, 418);
        assertFalse(response.path("status").path("traceId").asText().isBlank());
    }
}
