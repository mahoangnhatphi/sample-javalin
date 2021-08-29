package net.mahoangnhatphi

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put
import io.javalin.plugin.rendering.vue.VueComponent
import net.mahoangnhatphi.controller.UserController
//import net.mahoangnhatphi.dao.UserDao
import net.mahoangnhatphi.model.User

fun main(args: Array<String>) {
    val app = Javalin.create { config ->
        config.enableWebjars()
    }.start(7000)

    app.get("/", VueComponent("<hello-world></hello-world>"))
    app.get("/users", VueComponent("<user-overview></user-overview>"))
    app.get("/users/:user-id", VueComponent("<user-profile></user-profile>"))
    app.error(404, "html", VueComponent("<not-found></not-found>"))

    app.get("/api/users", UserController::getAll)
    app.get("/api/users/:user-id", UserController::getOne)
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