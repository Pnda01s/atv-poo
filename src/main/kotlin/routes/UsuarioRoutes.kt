import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Papel
import org.koin.ktor.ext.inject
import plugins.authorize
import services.UsuarioService

/**
 * Rotas de usuários: /usuarios/... e /instituicoes/{id}/usuarios (§9.4)
 */
fun Route.usuarioRoutes() {
    val usuarioService by inject<UsuarioService>()

    authenticate("auth-jwt") {

        route("/usuarios") {
            // GET /usuarios — AP, AI
            authorize(Papel.ADMIN_PLATAFORMA, Papel.ADMIN_INSTITUICAO) {
                get {
                    val pagination = call.parsePagination()
                    val filters = mapOf(
                        "nome" to call.request.queryParameters["nome"],
                        "email" to call.request.queryParameters["email"],
                        "ativo" to call.request.queryParameters["ativo"]?.toBooleanStrictOrNull()
                    )
                    val result = usuarioService.listar(pagination, filters)
                    call.respond(HttpStatusCode.OK, ApiResponse.success(result))
                }
            }

            // GET /usuarios/{id} — AP, AI
            authorize(Papel.ADMIN_PLATAFORMA, Papel.ADMIN_INSTITUICAO) {
                get("/{id}") {
                    val id = call.parameters["id"]!!
                    val usuario = usuarioService.buscarPorId(id)
                    call.respond(HttpStatusCode.OK, ApiResponse.success(usuario))
                }
            }

            // PATCH /usuarios/{id} — AP, AI
            authorize(Papel.ADMIN_PLATAFORMA, Papel.ADMIN_INSTITUICAO) {
                patch("/{id}") {
                    val id = call.parameters["id"]!!
                    val executorId = call.currentUserId()!!
                    val request = call.receive<UpdateUsuarioRequest>()
                    usuarioService.atualizar(id, request, executorId)
                    val atualizado = usuarioService.buscarPorId(id)
                    call.respond(HttpStatusCode.OK, ApiResponse.success(atualizado, "Usuário atualizado"))
                }
            }

            // DELETE /usuarios/{id} — AP
            authorize(Papel.ADMIN_PLATAFORMA) {
                delete("/{id}") {
                    val id = call.parameters["id"]!!
                    val executorId = call.currentUserId()!!
                    usuarioService.deletar(id, executorId)
                    call.respond(HttpStatusCode.OK, ApiResponse.success(mapOf<String, Any>(), "Usuário removido"))
                }
            }
        }

        // GET /instituicoes/{id}/usuarios — AP, AI
        route("/instituicoes/{instituicaoId}/usuarios") {
            authorize(Papel.ADMIN_PLATAFORMA, Papel.ADMIN_INSTITUICAO) {
                get {
                    val instituicaoId = call.parameters["instituicaoId"]!!
                    val pagination = call.parsePagination()
                    val result = usuarioService.listarPorInstituicao(instituicaoId, pagination, emptyMap())
                    call.respond(HttpStatusCode.OK, ApiResponse.success(result))
                }
            }

            // POST /instituicoes/{id}/usuarios — AP, AI (criação password-less)
            authorize(Papel.ADMIN_PLATAFORMA, Papel.ADMIN_INSTITUICAO) {
                post {
                    val instituicaoId = call.parameters["instituicaoId"]!!
                    val criadorId = call.currentUserId()!!
                    val request = call.receive<CreateUsuarioInstituicaoRequest>()
                    val criado = usuarioService.criarParaInstituicao(request, instituicaoId, criadorId)
                    call.respond(HttpStatusCode.Created, ApiResponse.success(criado, "Usuário criado"))
                }
            }
        }
    }
}

