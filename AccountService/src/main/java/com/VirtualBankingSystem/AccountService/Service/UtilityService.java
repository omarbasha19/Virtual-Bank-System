package com.VirtualBankingSystem.AccountService.Service;

import java.security.SecureRandom;

public class UtilityService {
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandom20DigitNumber() {
        StringBuilder sb = new StringBuilder(20);
        // Ensure the first digit is not 0
        sb.append(random.nextInt(9) + 1); // 1 to 9
        for (int i = 1; i < 20; i++) {
            sb.append(random.nextInt(10)); // 0 to 9
        }
        return sb.toString();
    }

    private UtilityService() {
        // Private constructor to prevent instantiation
    }
}
