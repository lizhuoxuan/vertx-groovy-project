package com.core.database;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface DatabaseService {

    @Fluent
    DatabaseService rows(String sql, JsonObject param,Handler<AsyncResult<JsonArray>> resultHandler);

    @Fluent
    DatabaseService firstRow(String sql, JsonObject param,Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    DatabaseService execute(String sql, JsonObject param, Handler<AsyncResult<Boolean>> resultHandler);

    @Fluent
    DatabaseService saveWithBatch(String sql, JsonArray params, Handler<AsyncResult<Boolean>> resultHandler);

    static DatabaseService create(Vertx vertx, JsonObject dbConfig, Handler<AsyncResult<DatabaseService>> readyHandler) {
        return new DatabaseServiceImpl(vertx, dbConfig, readyHandler);
    }

    static DatabaseService createProxy(Vertx vertx, String address) {
        return new DatabaseServiceVertxEBProxy(vertx, address);
    }
}