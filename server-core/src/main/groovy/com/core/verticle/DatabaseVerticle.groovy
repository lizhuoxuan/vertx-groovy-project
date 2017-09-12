package com.core.verticle

import com.core.database.DatabaseService
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.serviceproxy.ProxyHelper

class DatabaseVerticle extends AbstractVerticle {

    @Override
    void start(Future<Void> startFuture) throws Exception {


        def mySQLClientConfig =
                [host        : "127.0.0.1",
                 port        : 3306,
                 maxPoolSize : 1000,
                 username    : "root",
                 password    : "root",
                 database    : "jdcos",
                 charset     : "UTF-8",
                 queryTimeout: 10000]

        DatabaseService.create(vertx, new JsonObject(mySQLClientConfig), { ready ->
            if (ready.succeeded()) {
                ProxyHelper.registerService(DatabaseService.class, vertx, ready.result(), "aruistar.database")

                super.start(startFuture)
            } else {
                startFuture.fail(ready.cause())
            }
        })


    }
}
