package com.core.verticle

import com.core.database.DatabaseService
import com.core.log.LzxLog
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.AuthHandler
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.ext.web.handler.RedirectAuthHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.handler.UserSessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore

class HttpVerticle extends AbstractVerticle implements LzxLog {

    DatabaseService dbService

    @Override
    void start(Future<Void> startFuture) throws Exception {
        dbService = DatabaseService.createProxy(vertx, "aruistar.database")

        int port = config().getInteger("port", 8080)

        HttpServer server = vertx.createHttpServer()

        Router router = Router.router(vertx)

        def config = [
                keyStore:[
                        path:"keystore.jceks",
                        type:"jceks",
                        password:"secret"
                ]
        ]

        def provider = JWTAuth.create(vertx, config)

//        router.route().handler(UserSessionHandler.create(provider))

        // protect the API
        router.route("/api/*").handler(JWTAuthHandler.create(provider, "/api/newToken"))

        // this route is excluded from the auth handler
        router.get("/api/newToken").handler({ ctx ->
            ctx.response().putHeader("Content-Type", "text/plain")
            JsonObject creds = new JsonObject()
                    .put("username", "root")
                    .put("password", "root")
            String token = provider.generateToken( // <3>
                                        new JsonObject()
                                                .put("username", "root")
                                                .put("canCreate", "1")
                                                .put("canDelete", "1")
                                                .put("canUpdate", "1"),
                                        new JWTOptions()
                                                .setSubject("Wiki API")
                                                .setIssuer("Vert.x")
                                                .setExpiresInSeconds(60))
                                ctx.response().putHeader("Content-Type", "text/plain").end(token)
        })

        // this is the secret API
        router.get("/api/protected").handler({ ctx ->
            ctx.response().putHeader("Content-Type", "text/plain")
            ctx.user()
            ctx.response().end("a secret you should keep for yourself...")


        })

        //
        router.get("/api/test").handler({ ctx ->
            ctx.response().putHeader("Content-Type", "text/plain")
            ctx.user()
            ctx.response().end("a secret you should keep for yourself...")


        })

        server.requestHandler(router.&accept)
                .listen(port, { ar ->
            if (ar.succeeded()) {
                log.info("HTTP server running on port " + port)
                startFuture.complete()
            } else {
                log.error("Could not start a HTTP server", ar.cause())
                startFuture.fail(ar.cause())
            }

        })
    }
}
