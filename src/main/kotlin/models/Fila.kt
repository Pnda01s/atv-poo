package models

import kotlinx.serialization.Serializable
import java.time.Instant
import kotlinx.serialization.Contextual


@Serializable
data class Filas (
    val id: String? = null,
    val instituicaoID: String? = null,
    val nome: String,
    val ativa: Boolean? = false,
    val prioridadeHabilidata: Boolean = false,
    val fidelidaeHabilitada: Boolean = false,
    val tempoMaximoAtendimento: Int = 0,
    @Serializable val configuracoes: ConfiguracaoQrCode? = null,
    @Serializable val mesa: List<Mesa> = emptyList(),
    @Contextual val createdAt: Instant = Instant.now(),
    @Contextual val updatedAt: Instant = createdAt,
    val tipoAtendimento: Set<TipoAtendimento> // Mantenha apenas este
)
@Serializable
data class ConfiguracaoQrCode(
    val modoQrCode: Set<ModoOperacao>,
    val tempoExibicaoMin: Int? = null,
    val tempoExpiracaoMin: Int? = null,
    val toleranciaMin: Int? = null,
    val tempoAlertaSegundos: Int? = null
)
@Serializable
enum class ModoOperacao {
    ROTATIVO,
    FIXO
}
@Serializable
data class Mesa(
    val numero:String,
    val nome: String,
    val ativa: Boolean,
)
@Serializable
enum class TipoAtendimento {
    ONLINE,
    PRESENCIAL,
    HIBRIDO
}