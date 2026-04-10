package dtos.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import models.StatusInstituicao
import java.time.Instant


@Serializable
data class CreateInstituicaoRequest(
    val instituicaoId: String?,
    val nome: String,
    val cnpj: String,
    val responsavel: String,
    val status: Set<StatusInstituicao>,
    val solicitante: String,
    val contatoUrl: String,
    val motivoRejeicao: String,

    )

@Serializable
data class SolicitarInstituicaoRequest (
    val nome: String,
    val cnpj: String? = null,
    val email: String? = null,
    val telefone: String? = null,
    val responsavel: String? = null,
    val endereco: String? = null,
)

@Serializable
data class UpdateInstituicaoRequest(
    val instituicaoId: String?,
    val nome: String,
    val cnpj: String,
    val responsavel: String,
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

data class RejeitarInstituicaoRequest(
    val instituicaoId: String?,
    val motivoRejeicao: String?
)