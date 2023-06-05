package com.mock.conloop.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserQueryConstant {
    public static final String USER_BY_EMAIL = 
            "SELECT\n"+
                "U.user_id,\n"+
                "U.username,\n"+
                "U.email,\n"+
                "U.password,\n"+
                "U.created_at,\n"+
                "U.updated_at,\n" +
                "R.role_id,\n"+
                "R.role_name\n" +
            "FROM\n"+
                "conloop.users U\n"+
                    "INNER JOIN\n"+
                "conloop.user_roles UR ON U.user_id = UR.user_id\n"+
                    "INNER JOIN\n"+
                "roles R ON R.role_id = UR.role_id\n"+
           "WHERE\n"+
                "email = :email;";

    public static final String USER_QUERY = "SELECT * FROM users";

    public static final String INSERT_USER_QUERY = "INSERT INTO users (user_id, username, email, password) Values"+ 
    "(:user_id, :username, :email, :password)";

    public static final String INSERT_USER_DETAILS_QUERY = "INSERT INTO user_details (user_details_id, user_id) VALUES(:user_details_id, :user_id)";

    public static final String INSERT_USER_ROLES_QUERY = "INSERT INTO user_roles (user_id, role_id) VALUES (:user_id, (SELECT role_id FROM roles WHERE role_name = :role_name))";
}
