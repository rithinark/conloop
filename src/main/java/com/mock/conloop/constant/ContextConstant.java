package com.mock.conloop.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextConstant {
    public static final String EMAIL = "email";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String ROLE_ID = "role_id";
    public static final String ROLES = "roles";
    public static final String ROLE_NAME = "role_name";

    public static final String USER_DETAILS_ID = "user_details_id";
    public static final String BIO = "bio";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final String LOCATION = "location";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_GROUP_ADMIN = "ROLE_GROUP_ADMIN";
    public static final String ROLE_GROUP_USER = "ROLE_GROUP_USER";

    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String TOKEN_CACHE_PREFIX = "jwt-token";
    public final static String REFRESH_TOKEN_CACHE = "refresh-token";
}