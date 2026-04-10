import config.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import services.AuthService
import org.koin.ktor.ext.inject
import models.Papel // Garanta que o enum Papel seja importado
/**
 * Rotas de autenticação: /auth/... (§9.1 e §9.2)
 */
fun Route.authRoutes() {
    val authService by inject<AuthService>()
    val jwtConfig by inject<JwtConfig>()

    route("/auth") {

        // POST /auth/login — público
        post("/login") {
            val request = call.receive<LoginRequest>()
            val token = authService.login(request)
            call.respond(HttpStatusCode.OK, ApiResponse.success(token))
        }

        // POST /auth/register — público
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val token = authService.register(request)
            call.respond(HttpStatusCode.Created, ApiResponse.success(token, "Usuário registrado"))
        }

        // POST /auth/refresh — público
        post("/refresh") {
            val request = call.receive<RefreshRequest>()
            val token = authService.refresh(request)
            call.respond(HttpStatusCode.OK, ApiResponse.success(token))
        }

        // POST /auth/introspect — público
        post("/introspect") {
            val request = call.receive<IntrospectRequest>()
            val decoded = try {
                val jwt = jwtConfig.verifier.verify(request.accessToken)
                IntrospectResponse(
                    active = true,
                    tokenType = "Bearer",
                    exp = jwt.expiresAt?.time?.div(1000),
                    iat = jwt.issuedAt?.time?.div(1000)
                )
            } catch (_: Exception) {
                IntrospectResponse(active = false)
            }
            call.respond(HttpStatusCode.OK, ApiResponse.success(decoded))
        }

        // POST /auth/logout — autenticado
        authenticate("auth-jwt") {
            post("/logout") {
                val userId = call.currentUserId() ?: throw ApiException(401, "Não autenticado")
                authService.logout(userId)
                call.respond(HttpStatusCode.OK, ApiResponse.success(mapOf<String, Any>(), "Logout realizado"))
            }
        }

        // POST /auth/recover — público
        post("/recover") {
            val request = call.receive<RecoverRequest>()
            val result = authService.recover(request)
            call.respond(HttpStatusCode.OK, ApiResponse.success(result, "E-mail de recuperação enviado"))
        }

        // PATCH /auth/password/reset/token — público
        patch("/password/reset/{token}") {
            val token = call.parameters["token"] ?: throw ApiException(400, "Token obrigatório")
            val request = call.receive<ResetSenhaByTokenRequest>()
            authService.resetByToken(token, request)
            call.respond(HttpStatusCode.OK, ApiResponse.success(mapOf<String, Any>(), "Senha redefinida"))
        }

        // PATCH /auth/password/reset/code — público
        patch("/password/reset/code") {
            val request = call.receive<ResetSenhaByCodeRequest>()
            authService.resetByCode(request)
            call.respond(HttpStatusCode.OK, ApiResponse.success(mapOf<String, Any>(), "Senha redefinida"))
        }
    }
}

