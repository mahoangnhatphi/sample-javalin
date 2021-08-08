package net.mahoangnhatphi

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put
import net.mahoangnhatphi.dao.UserDao
import net.mahoangnhatphi.model.User

fun main(args: Array<String>) {
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
}