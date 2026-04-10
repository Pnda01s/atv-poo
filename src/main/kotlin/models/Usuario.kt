package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
enum class Papel {
    ADMIN_PLATAFORMA,
    ADMIN_INSTITUICAO,
    OPERADOR,
    USUARIO_FINAL
}

@Serializable
data class Usuario(
    val id: String? = null,
    val nome: String,
    val email: String,
    val senhaHash: String,
    val papeis: Set<Papel>,
    val instituicaoId: String? = null,
    val ativo: Boolean = true,
    val avatar: String? = null,
    val fusoHorario: String,
    val accessToken: String? = null,  // Sugestão: use camelCase (t estava minúsculo)
    val refreshToken: String? = null, // Sugestão: use camelCase
    val tokenUnico: String? = null,
    val codigoRecuperaSenha: String? = null,
    @Contextual val expCodigoRecuperaSenha: Instant? = null,
    @Contextual val ultimoLoginEm: Instant? = null,
    @Contextual val createdAt: Instant = Instant.now(),
    @Contextual val updatedAt: Instant? = null
)