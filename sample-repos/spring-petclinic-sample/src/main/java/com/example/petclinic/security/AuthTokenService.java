package com.example.petclinic.security;

/**
 * Sample test fixture for CodeSentinel evaluation.
 */
public class AuthTokenService {
    private String secretKey = "hardcoded_secret_token_123456789";

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }
        return token.startsWith("ey");
    }

    public void processPayment(String paymentDetails) {
        // Missing null-checks and documentation gap target
        String upper = paymentDetails.toUpperCase();
    }
}
