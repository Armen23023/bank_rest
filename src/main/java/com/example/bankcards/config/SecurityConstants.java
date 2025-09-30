package com.example.bankcards.config;

public final class SecurityConstants {
    private SecurityConstants() {
        /* prevent instantiation */
    }

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String TEMPLATE_HASROLE_PREFIX = "hasRole('";
    private static final String TEMPLATE_HASROLE_SUFFIX = "')";

    public static final String ROLE_USER = ROLE_PREFIX + USER_ROLE;
    public static final String ROLE_ADMIN = ROLE_PREFIX + ADMIN_ROLE;

    public static final String HAS_ROLE_USER = TEMPLATE_HASROLE_PREFIX + ROLE_USER + TEMPLATE_HASROLE_SUFFIX;
    public static final String HAS_ROLE_ADMIN = TEMPLATE_HASROLE_PREFIX + ROLE_ADMIN + TEMPLATE_HASROLE_SUFFIX;
}
