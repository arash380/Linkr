package com.linkr.services.utils;

import com.linkr.models.Credentials;
import com.linkr.models.Employee;
import com.linkr.services.filters.AuthorizationLevels;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * The type Persistence utils.
 * @author Team Linkr.
 * @version 1.0
 */
public class PersistenceUtils {

    /**
     * Performs Base64 URL encoding on given byte array.
     *
     * @param byteArray .
     * @return Base64 URL encoded string.
     */
    public static String encodeBase64Url(byte[] byteArray) {
        return Base64.getUrlEncoder().withoutPadding().
                encodeToString(byteArray);
    }

    /**
     * Performs Base64 URL decoding on given JWT token string.
     *
     * @param encodedString Base64 URL encoded string.
     * @return Decoded URL string.
     */
    public static String decodeBase64Url(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    /**
     * Creates JWT.
     *
     * @param credentials .
     * @param level       .
     * @return Complete, signed JWT string.
     */
    public static String createJwt(Credentials credentials,
                                   AuthorizationLevels level) {
        final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        String payload = createJwtPayload(credentials, level);
        String payloadBase64 = Base64.getEncoder().
                encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String headerBase64 = Base64.getEncoder().
                encodeToString(JWT_HEADER.getBytes(StandardCharsets.UTF_8));

        String data = headerBase64 + "." + payloadBase64;
        String signature = hmacSha256(data);

        //returns JWT token
        return headerBase64 + "." + payloadBase64 + "." + signature;
    }

    /**
     * Creates JWT payload from credential object.
     *
     * @param credentials .
     * @param level       authorization level.
     * @return Payload String.
     */
    public static String createJwtPayload(Credentials credentials,
                                          AuthorizationLevels level) {

        final String JWT_TITLE_SUBJECT =
            ConfigurationSet.getProperty("JWT_TITLE_SUBJECT");
        final String JWT_TITLE_NAME =
            ConfigurationSet.getProperty("JWT_TITLE_NAME");
        final String JWT_TITLE_ROLE =
            ConfigurationSet.getProperty("JWT_TITLE_ROLE");
        final String JWT_TITLE_EXPIRY =
            ConfigurationSet.getProperty("JWT_TITLE_EXPIRY");

        long currentTime = Instant.now().toEpochMilli();
        long ACCESS_DURATION_MIN =
            Integer.parseInt(ConfigurationSet.getProperty("TOKEN_LIFE_ms"));
        long expTime =
            currentTime + TimeUnit.MILLISECONDS.toMillis(ACCESS_DURATION_MIN);

        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(JWT_TITLE_SUBJECT,
            Json.createValue(credentials.getEmployeeID()));
        builder.add(JWT_TITLE_NAME,
                Json.createValue(credentials.getUserName()));
        builder.add(JWT_TITLE_ROLE, Json.createValue(level.name()));
        builder.add(JWT_TITLE_EXPIRY, Json.createValue(expTime));

        if (ResourceUtils.isDevModeOn()) {
            expTime =
                currentTime + TimeUnit.MINUTES.toMillis(Integer.
                        parseInt(
                        ConfigurationSet.getProperty("DEV_MODE_TOKEN_LIFE")));

            String adminUsername =
                ConfigurationSet.getProperty("ADMIN_USERNAME");

            builder.add(JWT_TITLE_SUBJECT, Json.createValue(0));
            builder.add(JWT_TITLE_NAME, Json.createValue(adminUsername));
            builder.add(JWT_TITLE_ROLE,
                Json.createValue(AuthorizationLevels.ADMIN.name()));
            builder.add(JWT_TITLE_EXPIRY, Json.createValue(expTime));
        }

        return builder.build().toString();
    }

    /**
     * Hashes data alongside secret for JWT token.
     *
     * @param data JWT header and payload separated by period.
     * @return JWT signature string..
     */
    private static String hmacSha256(String data) {
        Mac mac;

        try {
            mac = Mac.getInstance("HmacSHA256");
            String secret = ConfigurationSet.getProperty("secret");

            byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec =
                new SecretKeySpec(secretBytes, "HmacSHA256");

            mac.init(secretKeySpec);

            byte[] signedTokenBytes =
                mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return encodeBase64Url(signedTokenBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * Reads JWT token string and returns payload if signature is valid.
     *
     * @param token JWT token
     * @return JsonObject json object
     * @throws CredentialNotFoundException thrown if signature does not match expected value.
     */
    public static JsonObject readJwt(String token)
        throws CredentialNotFoundException {
        //Splits JWT by period

        if (token == null || !token.matches("^.*\\..*\\..*$")) {
            throw new CredentialNotFoundException();
        }

        String[] parts = token.split("\\.");

        String headerString = decodeBase64Url(parts[0]);
        String payloadString = decodeBase64Url(parts[1]);
        String signature = parts[2];

        String payloadBase64 = Base64.getEncoder().
                encodeToString(payloadString.getBytes(StandardCharsets.UTF_8));
        String headerBase64 = Base64.getEncoder().
                encodeToString(headerString.getBytes(StandardCharsets.UTF_8));
        String signatureBase64 = Base64.getEncoder().
                encodeToString(signature.getBytes(StandardCharsets.UTF_8));

        checkSignature(headerBase64, payloadBase64, signature);

        return readJsonObject(payloadString);
    }

    /**
     * Checks if JWT signature is valid.
     *
     * @param headerBase64  JWT header in base64 format.
     * @param payloadBase64 JWT payload in base64 format
     * @param signature     JWT signature.
     * @throws CredentialNotFoundException thrown if signature does not match expect value.
     */
    private static void checkSignature(String headerBase64,
                                       String payloadBase64, String signature)
        throws CredentialNotFoundException {

        String data = headerBase64 + "." + payloadBase64;
        String signatureRegen = hmacSha256(data);

        if (signatureRegen == null || !signatureRegen.equals(signature)) {
            throw new CredentialNotFoundException();
        }
    }


    /**
     * Reads JSON object and returns JsonObject type.
     *
     * @param jsonString string representation of JSON object.
     * @return JsonObject created from given string.
     */
    public static JsonObject readJsonObject(String jsonString) {
        return Json.createReader(new StringReader(jsonString)).readObject();
    }

    /**
     * Checks if JWT token is expired.
     *
     * @param payload JWT payload.
     * @throws CredentialExpiredException thrown if expired.
     */
    public static void checkExpired(JsonObject payload)
        throws CredentialExpiredException {
        long currentTime = Instant.now().toEpochMilli();
        long expiryTime = payload.getJsonNumber("exp").longValue();

        if (currentTime > expiryTime) {
            throw new CredentialExpiredException();
        }
    }
}
