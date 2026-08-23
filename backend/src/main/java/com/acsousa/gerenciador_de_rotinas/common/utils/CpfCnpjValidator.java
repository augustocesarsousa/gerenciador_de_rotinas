package com.acsousa.gerenciador_de_rotinas.common.utils;

public class CpfCnpjValidator {

    public static boolean isValidCpf(String cpf) {
        if (cpf == null) return false;
        
        // Remove caracteres não numéricos
        String cleanCpf = cpf.replaceAll("\\D", "");

        if (cleanCpf.length() != 11 || cleanCpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += (cleanCpf.charAt(i) - '0') * (10 - i);
            }
            int r1 = 11 - (sum % 11);
            int d1 = (r1 == 10 || r1 == 11) ? 0 : r1;

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += (cleanCpf.charAt(i) - '0') * (11 - i);
            }
            int r2 = 11 - (sum % 11);
            int d2 = (r2 == 10 || r2 == 11) ? 0 : r2;

            return (cleanCpf.charAt(9) - '0' == d1) && (cleanCpf.charAt(10) - '0' == d2);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidCnpj(String cnpj) {
        if (cnpj == null) return false;

        // Remove caracteres não numéricos
        String cleanCnpj = cnpj.replaceAll("\\D", "");

        if (cleanCnpj.length() != 14 || cleanCnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                sum += (cleanCnpj.charAt(i) - '0') * weights1[i];
            }
            int r1 = sum % 11;
            int d1 = (r1 < 2) ? 0 : 11 - r1;

            int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            sum = 0;
            for (int i = 0; i < 13; i++) {
                sum += (cleanCnpj.charAt(i) - '0') * weights2[i];
            }
            int r2 = sum % 11;
            int d2 = (r2 < 2) ? 0 : 11 - r2;

            return (cleanCnpj.charAt(12) - '0' == d1) && (cleanCnpj.charAt(13) - '0' == d2);
        } catch (Exception e) {
            return false;
        }
    }
}
