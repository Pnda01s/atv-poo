package models
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
data class QRcode(
    val id: String? = null,
    val filaId: String? = null,
    val codigo: String,
    @Contextual val validaAte: Instant,
    @Contextual val toleranciaAte: Instant,
    val ativo: Boolean,
    @Contextual val createdAt: Instant = Instant.now(),
    @Contextual val updatedAt: Instant? = Instant.now(),
)