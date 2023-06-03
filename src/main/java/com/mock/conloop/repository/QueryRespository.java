package com.mock.conloop.repository;

import java.util.List;

import com.google.gson.JsonArray;
import com.mock.conloop.model.Query;

public interface QueryRespository {
    public JsonArray query(Query query);

    public void update(List<Query> queries);

    public int update(Query query);
}
