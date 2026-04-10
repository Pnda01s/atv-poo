package dtos.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateSenhaRequest(
    val qrCode: String? = null,
    val prioridade: String? = "BAIXA"
)

@Serializable
data class CreateSenhaPresencialRequest(
    val nomeCidadao: String,
    val prioridade: String? = "BAIXA",
    val mesa: String,
    val mesaNome: String
)