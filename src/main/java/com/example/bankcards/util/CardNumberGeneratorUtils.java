package com.example.bankcards.util;

import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CardNumberGeneratorUtils {
    private static final String BIN = "400000"; // Example VISA BIN
    private static final Random RANDOM = new Random();

    public static String generateCardNumber() {
        // Generate 9 random digits for account number
        StringBuilder sb = new StringBuilder(BIN);
        for (int i = 0; i < 9; i++) {
            sb.append(RANDOM.nextInt(10));
        }

        // Add check digit using Luhn algorithm
        int checkDigit = getLuhnCheckDigit(sb.toString());
        sb.append(checkDigit);

        return sb.toString();
    }

    private static int getLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }
}
