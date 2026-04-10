package models
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
data class Senha (
    val id: String? = null,
    val filaId : String? = null,
    val instituicaoId : String?= null,
    val usuarioId : String?= null,
    val nomeCidadao: String? = null,
    val precencial: Boolean,
    val posicao : Int,
    val status:Set<Status>,
    val priority: Set<Prioridade>,
    val mesa: String,
    val mesaNome: String,
    val operadorId: String? = null,
    @Contextual val createdAt: Instant? = Instant.now(),
    @Contextual val updatedAt: Instant = Instant.now()

)
@Serializable
enum class Status {
    AGUARDANDO, EM_ATENDNIMENTO, CACELADA, FINALIZADA
}
@Serializable
enum class Prioridade {
    ALTA, MEDIA, BAIXA
}