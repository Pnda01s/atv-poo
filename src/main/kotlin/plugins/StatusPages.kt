package plugins
import ApiException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

/**
 * Plugin de tratamento centralizado de erros.
 *
 * Captura ApiException e exceções genéricas, retornando ApiResponse padronizado.
 */
fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<ApiException> { call, cause ->
            call.respond(
                HttpStatusCode.fromValue(cause.statusCode),
                ApiResponse.error<Any>(cause.message, cause.errors)
            )
        }

        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse.error<Any>(cause.message ?: "Requisição inválida")
            )
        }

        exception<Exception> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse.error<Any>("Erro interno do servidor")
            )
        }
    }
}

