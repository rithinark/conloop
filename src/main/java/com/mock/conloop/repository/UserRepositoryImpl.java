package com.mock.conloop.repository;

import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.mock.conloop.model.Query;
import com.mock.conloop.model.Role;
import com.mock.conloop.model.User;
import com.mock.conloop.util.DateTimeUtils;
import com.mock.conloop.constant.UserQueryConstant;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mock.conloop.constant.ContextConstant;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final QueryRespository queryRespository;
    private final PasswordEncoder passwordEncoder;

    public UserRepositoryImpl(QueryRespository queryRespository, PasswordEncoder passwordEncoder) {
        this.queryRespository = queryRespository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ContextConstant.EMAIL, email);
        JsonArray jsonArray = queryRespository.query(new Query(UserQueryConstant.USER_BY_EMAIL, parameterSource));
        if (jsonArray.isEmpty()) {
            return Optional.empty();
        }
        User user = new User();
        List<Role> roles = new LinkedList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject object = jsonElement.getAsJsonObject();
            user.setUserId(object.get(ContextConstant.USER_ID).getAsString());
            user.setUsername(object.get(ContextConstant.USERNAME).getAsString());
            user.setEmail(object.get(ContextConstant.EMAIL).getAsString());
            user.setPassword(object.get(ContextConstant.PASSWORD).getAsString());
            user.setCreatedAt(DateTimeUtils.getDate(object.get(ContextConstant.CREATED_AT).getAsString()));
            user.setUpdatedAt(DateTimeUtils.getDate(object.get(ContextConstant.UPDATED_AT).getAsString()));
            roles.add(new Role(object.get(ContextConstant.ROLE_ID).getAsString(),
                    object.get(ContextConstant.ROLE_NAME).getAsString()));
        }
        user.setRoles(roles);
        return Optional.of(user);
    }

    @Override
    public void delete(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public User save(User user) {
        List<Query> queries = prepareInsertQueries(user);
        queryRespository.update(queries);
        return user;
    }

    private List<Query> prepareInsertQueries(User user) {
        List<Query> queries = new LinkedList<>();
        user.setUserId(UUID.randomUUID().toString());
        queries.add(loadInsertUserQuery(user));
        queries.add(loadInsertUserDetailsQuery(user));
        for (Role role : user.getRoles()) {
            queries.add(loadInsertUserRolesQuery(user.getUserId(), role.getRoleName()));
        }
        return queries;
    }

    private Query loadInsertUserQuery(User user) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource
                .addValue(ContextConstant.USER_ID, user.getUserId(), Types.VARCHAR)
                .addValue(ContextConstant.EMAIL, user.getEmail(), Types.VARCHAR)
                .addValue(ContextConstant.USERNAME, user.getUsername(), Types.VARCHAR)
                .addValue(ContextConstant.PASSWORD, passwordEncoder.encode(user.getPassword()), Types.VARCHAR);
        return new Query(UserQueryConstant.INSERT_USER_QUERY, parameterSource);
    }

    private Query loadInsertUserDetailsQuery(User user) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource
                .addValue(ContextConstant.USER_DETAILS_ID, UUID.randomUUID().toString(), Types.VARCHAR)
                .addValue(ContextConstant.USER_ID, user.getUserId(), Types.VARCHAR);
        return new Query(UserQueryConstant.INSERT_USER_DETAILS_QUERY, parameterSource);
    }

    private Query loadInsertUserRolesQuery(String userId, String roleName) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource
                .addValue(ContextConstant.USER_ID, userId, Types.VARCHAR)
                .addValue(ContextConstant.ROLE_NAME, roleName, Types.VARCHAR);
        return new Query(UserQueryConstant.INSERT_USER_ROLES_QUERY, parameterSource);
    }
}
