package com.johanpmeert;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.time.Duration;

public class YubikeyValidationClient {

    private final String otp;
    private final String OTPserial;
    private static final String YUBI_http = "https://api.yubico.com/wsapi/2.0/verify?";
    private final String nonce;
    private final String response;

    public YubikeyValidationClient(String OtpString) throws IOException, InterruptedException {
        otp = OtpString.strip();
        if (otp.length() != 44) throw new IllegalArgumentException("Not a Yubikey OTP string, invalid length");
        OTPserial = otp.substring(0, 12);
        nonce = generateRandomNonce(30);
        String values = "otp=" + otp + "&id=1&nonce=" + nonce;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(YUBI_http + values))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();
        response = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(5))
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString())
                .body();
    }

    private static String generateRandomNonce(int length) {
        final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder();
        for (int counter = 0; counter < length; counter++) {
            token.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        return token.toString();
    }

    public boolean isValidResponse() {
        return (response.contains("status=OK") && response.contains(otp) && response.contains(nonce));
    }

    public String getSerial() {
        return OTPserial;
    }

}