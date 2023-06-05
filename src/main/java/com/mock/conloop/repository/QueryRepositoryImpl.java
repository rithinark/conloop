package com.mock.conloop.repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mock.conloop.model.Query;

@Repository
public class QueryRepositoryImpl implements QueryRespository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public QueryRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public JsonArray query(Query query) {
        return jdbcTemplate.query(query.getSql(), query.getParameterSource(), (ResultSet resultSet) -> {
            JsonArray jsonArray = new JsonArray();
            Map<String, Integer> columns = getColumnDetails(resultSet);
            while (resultSet.next()) {
                JsonObject object = new JsonObject();
                for (Map.Entry<String,Integer> column : columns.entrySet())
                    object.add(column.getKey(), getValueByType(resultSet, column.getKey(), column.getValue()));
                jsonArray.add(object);
            }
            return jsonArray;
        });
    }

    private Map<String, Integer> getColumnDetails(ResultSet resultSet) {
        Map<String, Integer> columnDetails = new HashMap<>();
        ResultSetMetaData metaData;
        try {
            metaData = resultSet.getMetaData();
            OfInt iterator = IntStream.range(0, metaData.getColumnCount()).iterator();
            while (iterator.hasNext()) {
                Integer currIndex = iterator.nextInt() + 1;
                columnDetails.put(metaData.getColumnName(currIndex), metaData.getColumnType(currIndex));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnDetails;
    }

    private JsonElement getValueByType(ResultSet resultSet, String column, Integer type) throws SQLException {
        JsonElement jsonElement = null;
        
        switch (type) {
            case Types.TINYINT:
            case Types.BOOLEAN: 
                jsonElement = new JsonPrimitive(resultSet.getBoolean(column));
                break;
            case Types.SMALLINT:
            case Types.INTEGER: 
            case Types.BIGINT: 
                jsonElement = new JsonPrimitive((Integer) resultSet.getObject(column));
                break;
            case Types.DECIMAL:
            case Types.NUMERIC: 
            case Types.FLOAT: 
            case Types.DOUBLE: 
                new JsonPrimitive((Double) resultSet.getObject(column));
                break;
            case Types.VARCHAR: 
            case Types.TIMESTAMP: 
                jsonElement = new JsonPrimitive(resultSet.getString(column));
                break;
            default: throw new IllegalArgumentException();
        }
        return jsonElement;
    }

    @Override
    public void update(List<Query> queries) {
        for(Query query: queries) {
            update(query);
        }
    }

    @Override
    public int update(Query query) {
       return jdbcTemplate.update(query.getSql(), query.getParameterSource());
    }
}