package com.ps.cinema.server.model;

public enum Role {
    ROLE_ADMIN,ROLE_PRODUCER,ROLE_VIEWER;
    public int getRole() {
        return switch (this) {
            case ROLE_ADMIN -> 0;
            case ROLE_PRODUCER -> 1;
            case ROLE_VIEWER -> 2;
            default -> Integer.MIN_VALUE;
        };
    }

    public static String getRole(int i){
        return switch (i) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_PRODUCER";
            case 3 -> "ROLE_VIEWER";
            default -> "None";
        };
    }
}
