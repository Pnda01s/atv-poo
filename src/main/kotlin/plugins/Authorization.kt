package plugins

import currentUserPapeis
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import models.Papel


/**
 * Plugin custom de autorização por papéis.
 */
fun Route.authorize(vararg papeis: Papel, build: Route.() -> Unit): Route {
    val route = createChild(AuthorizationRouteSelector())

    route.intercept(ApplicationCallPipeline.Plugins) { // Recomendado usar Plugins em vez de Call
        val userPapeis = call.currentUserPapeis()

        // Verifica se o usuário tem pelo menos um dos papéis exigidos
        if (userPapeis.none { it in papeis }) {
            call.respond(HttpStatusCode.Forbidden, ApiResponse.error<Any>("Acesso negado"))
            finish()
        }
    }

    route.build()
    return route
}

class AuthorizationRouteSelector : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) =
        RouteSelectorEvaluation.Transparent
}