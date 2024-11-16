package com.app.us_twogether.util;

import com.app.us_twogether.exception.CPF.CPFInvalidException;
import com.app.us_twogether.exception.DataAlreadyExistsException;

public class ValidationCPF {
    public static final int NRO_DIGITOS_SEM_DV = 9;
    public static final int NRO_DIGITOS_VERIFICADORES = 2;
    public static final int NRO_DIGITOS_COM_DV = NRO_DIGITOS_SEM_DV + NRO_DIGITOS_VERIFICADORES;

    /** Recebe uma cadeia completa de dígitos de CPF, sem formatação */
    public static void Valid(String cpf) {
        if (cpf.trim().isEmpty()) {
            throw new CPFInvalidException("O número do CPF não foi informado.");
        }
        else if (ehCadeiaDeNoveDigitosNaoIniciadaEmZero(cpf)) {
            cpf = cpf + moduloOnze(cpf);
        } else if (ehCadeiaDeOnzeDigitos(cpf) && isValido(cpf)) {
            cpf = cpf;
        } else {
            throw new CPFInvalidException("Número de CPF inválido: " + cpf);
        }
    }

    private static boolean ehCadeiaDeNoveDigitosNaoIniciadaEmZero(String cadeia) {
        return cadeia != null && cadeia.matches("[1-9]\\d{" + (NRO_DIGITOS_SEM_DV - 1) + "}");
    }

    private static boolean ehCadeiaDeOnzeDigitos(String cadeia) {
        return cadeia != null && cadeia.matches("[1-9]\\d{" + (NRO_DIGITOS_COM_DV - 1) + "}");
    }

    private static boolean isValido(String cpf) {
        return ehCadeiaDeOnzeDigitos(cpf)
                && cpf.substring(NRO_DIGITOS_SEM_DV, NRO_DIGITOS_COM_DV).
                equals(moduloOnze(cpf.substring(0, NRO_DIGITOS_SEM_DV)));
    }

    /** Recebe uma cadeia de 9 dígitos decimais e retorna o módulo-11 dessa cadeia */
    private static String moduloOnze(String digitos) {
        if (false == ehCadeiaDeNoveDigitosNaoIniciadaEmZero(digitos)) {
            throw new IllegalArgumentException("Dígitos de CPF inválidos: " + digitos);
        }

        int dv1 = calcularDigitoVerificador(digitos);
        int dv2 = calcularDigitoVerificador(digitos + dv1);

        return String.valueOf(dv1) + String.valueOf(dv2);
    }

    private static int calcularDigitoVerificador(String digitos) {
        int peso = digitos.length() + 1;
        int dv = 0;
        for (int i = 0; i < digitos.length(); i++) {
            dv += (digitos.charAt(i) - '0') * peso;
            peso--;
        }

        dv = 11 - (dv % 11);

        return dv > 9 ? 0 : dv;
    }
}
