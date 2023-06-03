package com.mock.conloop.model;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class Query {
    private String sql;
    private SqlParameterSource parameterSource;

    public Query() {
    }

    public Query(String sql, SqlParameterSource parameterSource) {
        this.sql = sql;
        this.parameterSource = parameterSource;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SqlParameterSource getParameterSource() {
        return parameterSource;
    }

    public void setParameterSource(SqlParameterSource parameterSource) {
        this.parameterSource = parameterSource;
    }

}
