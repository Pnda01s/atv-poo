package models

import java.time.Instant

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
data class Instituicao (
    val id: String? = null,
    val nome: String,
    val cnpj: String,
    val responsavel: String,
    val endereco: String,
    val descricao: String,
    val ativo: Boolean,
    val status: Set<StatusInstituicao>,
    val solicitante: String,
    val contatoUrl: String,
    val motivoRejeicao: String,
    val aprovadoPor: String? = null,
    @Contextual val aprovadoEm: Instant? = null,
    @Contextual val configuracoes: Any? =object {},
    @Contextual val createdAt: Instant = Instant.now(),
    @Contextual val updatedAt: Instant = createdAt
)

@Serializable
enum class StatusInstituicao {
    PENDENTE,
    APROVADA,
    REJEITADA
}
