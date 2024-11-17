package com.app.us_twogether.enums;

public enum ENotificationUser {
    TOTAL,
    SOMENTE_ATIVIDADE,
    SOMENTE_PLANEJAMENTO_FINANCEIRO;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
