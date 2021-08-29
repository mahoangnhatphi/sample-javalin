package net.mahoangnhatphi

import io.javalin.Javalin
import io.javalin.core.security.Role
import io.javalin.core.security.SecurityUtil
import io.javalin.core.util.Header
import io.javalin.http.Context
import io.javalin.plugin.rendering.vue.VueComponent
import net.mahoangnhatphi.controller.UserController

enum class AppRole : Role { ANYONE, LOGGED_IN }

fun main(args: Array<String>) {
    val app = Javalin.create { config ->
        config.enableWebjars()
        config.accessManager { handler, ctx, permittedRoles ->
            when {
                AppRole.ANYONE in permittedRoles -> handler.handle(ctx)
                AppRole.LOGGED_IN in permittedRoles && anyUsernameProvided(ctx) -> handler.handle(ctx)
                else -> ctx.status(401).header(Header.WWW_AUTHENTICATE, "Basic")
            }
        }
    }.start(7000)

    app.get("/", VueComponent("<hello-world></hello-world>"), SecurityUtil.roles(AppRole.ANYONE))
    app.get("/users", VueComponent("<user-overview></user-overview>"), SecurityUtil.roles(AppRole.ANYONE))
    app.get("/users/:user-id", VueComponent("<user-profile></user-profile>"), SecurityUtil.roles(AppRole.LOGGED_IN))
    app.error(404, "html", VueComponent("<not-found></not-found>"))

    app.get("/api/users", UserController::getAll, SecurityUtil.roles(AppRole.ANYONE))
    app.get("/api/users/:user-id", UserController::getOne, SecurityUtil.roles(AppRole.LOGGED_IN))
    /*
    val userDao = UserDao()
    val app = Javalin.create().apply {
        exception(Exception::class.java) {
            e, ctx -> e.printStackTrace()
        }
        error(404) {
            ctx -> ctx.json("not found")
        }
    }.start(7000)

    app.routes {
        get("/users") { ctx ->
            ctx.json(userDao.findAllUsers())
        }

        get("/users/:user-id") {
            ctx -> ctx.json(userDao.findById(ctx.pathParam("user-id").toInt())!!)
        }

        post("/users") {
            val user = it.body<User>();
            userDao.save(user.name, user.email)
            it.status(201)
        }

        put("/users/:user-id") {
            val user = it.body<User>();
            userDao.update(it.pathParam("user-id").toInt(),
                    user = user)
            it.status(204)
        }

        delete("/users/:user-id") {
            userDao.delete(it.pathParam("user-id").toInt())
            it.status(204)
        }
    }
    */
}

fun anyUsernameProvided(ctx: Context) = ctx.basicAuthCredentials()?.username?.isNotBlank()