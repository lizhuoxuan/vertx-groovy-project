package com.core

import com.core.log.LzxLog
import com.core.verticle.DatabaseVerticle
import com.core.verticle.HttpVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Vertx

class MainVerticle extends AbstractVerticle implements LzxLog {

    static void main(def args) {
        Vertx.vertx().deployVerticle(MainVerticle.newInstance())
    }

    @Override
    void start(Future<Void> startFuture) throws Exception {

        vertx.deployVerticle(DatabaseVerticle.newInstance(), { dbHandler ->
            if (dbHandler.succeeded()) {
                vertx.deployVerticle(HttpVerticle.newInstance(), { httpHandler ->
                    if (httpHandler.succeeded()) {
                        startFuture.complete()
                        log.info("success")
                    } else {
                        log.info(httpHandler.cause().message)
                    }
                })
            } else {
                log.info(dbHandler.cause().message)
            }
        })
    }
}
