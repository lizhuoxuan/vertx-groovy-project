package com.core.database

import com.core.log.LzxLog
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.asyncsql.AsyncSQLClient
import io.vertx.ext.asyncsql.MySQLClient
import io.vertx.ext.sql.SQLConnection

class DatabaseServiceImpl implements DatabaseService, LzxLog {

    AsyncSQLClient dbClient
    Vertx vertx

    DatabaseServiceImpl(Vertx vertx, JsonObject dbConfig, Handler<AsyncResult<DatabaseService>> readyHandler) {

        this.vertx = vertx
        dbClient = MySQLClient.createShared(vertx, dbConfig)
        readyHandler.handle(Future.succeededFuture(this))
    }

    void getConnection(Closure<SQLConnection> back) {

        dbClient.getConnection({ res ->
            if (res.succeeded()) {
                back(res.result())
            }
        })
    }

    @Override
    DatabaseService rows(String sql, JsonObject param, Handler<AsyncResult<JsonArray>> resultHandler) {
        return this
    }

    @Override
    DatabaseService firstRow(String sql, JsonObject param, Handler<AsyncResult<JsonObject>> resultHandler) {
        return null
    }

    @Override
    DatabaseService execute(String sql, JsonObject param, Handler<AsyncResult<Boolean>> resultHandler) {
        return this
    }

    @Override
    DatabaseService saveWithBatch(String sql, JsonArray param, Handler<AsyncResult<Boolean>> resultHandler) {
        return null
    }
}
