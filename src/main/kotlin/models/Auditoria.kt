package models
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.time.Instant

@Serializable
enum class TipoAcao {
    LOGIN,CRIAR,ATUALIZAR,DELETAR
}
@Serializable
data class Auditoria (
    val id: String?=null,
    val instituicaoId: String? = null,
    val usuarioId: String?=null,
    val acao: TipoAcao,
    val entidade: String,
    val entidadeId: String?= null,
    val dados: JsonElement? = null,
    @Contextual val createdAt: Instant? = Instant.now()
)


