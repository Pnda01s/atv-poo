package dtos.request

import kotlinx.serialization.Serializable
import models.ModoOperacao

@Serializable
data class CreateFilaRequest(
    val nome: String,
    val tipoAtendimento: Set<String>, // Recebe Strings para converter em Enum
    val ativa: Boolean = false,
    val prioridadeHabilidata: Boolean = false,
    val fidelidaeHabilitada: Boolean = false,
    val tempoMaximoAtendimento: Int = 0,
    val configuracoes: ConfiguracaoQrCodeRequest? = null,
    val mesas: List<MesaRequest> = emptyList()
)

@Serializable
data class UpdateFilaRequest(
    val nome: String? = null,
    val tipoAtendimento: Set<String>? = null,
    val ativa: Boolean? = null,
    val prioridadeHabilidata: Boolean? = null,
    val fidelidaeHabilitada: Boolean? = null,
    val tempoMaximoAtendimento: Int? = null,
    val configuracoes: ConfiguracaoQrCodeRequest? = null,
    val mesas: List<MesaRequest>? = null
)

@Serializable
data class ConfiguracaoQrCodeRequest(
    val modoQrCode: Set<ModoOperacao>,
    val tempoExibicaoMin: Int? = null,
    val tempoExpiracaoMin: Int? = null,
    val toleranciaMin: Int? = null,
    val tempoAlertaSegundos: Int? = null
)

@Serializable
data class MesaRequest(
    val numero: String,
    val nome: String,
    val ativa: Boolean
)