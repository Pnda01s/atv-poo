package config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import models.Papel
import java.util.*

// Mantenha apenas o caminho correto onde o enum Papel realmente está:


class JwtConfig(
    private val secret: String = System.getenv("JWT_SECRET") ?: "filacidada-secret-dev",
    private val issuer: String = "seu-issuer", // Substitua pelo valor de Constants se preferir
    private val audience: String = "seu-audience",
    val realm: String = "seu-realm"
) {
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    /**
     * Gera um access token JWT de curta duração.
     */
    fun generateAccessToken(userId: String, papeis: Set<Papel>, instituicaoId: String?): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(userId)
            .withClaim("id", userId)
            // Alterado para withClaim com toList() ou withArrayClaim
            .withClaim("papeis", papeis.map { it.name }.toList())
            .withClaim("instituicaoId", instituicaoId)
            .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
            .withIssuedAt(Date())
            .sign(algorithm)
    }

    /**
     * Gera um refresh token JWT de longa duração.
     */
    fun generateRefreshToken(userId: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("id", userId)
            .withClaim("type", "refresh")
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000 * 7)) // 7 dias
            .withIssuedAt(Date())
            .sign(algorithm)
    }

    fun accessTokenExpiresAt(): String {
        val expiry = Date(System.currentTimeMillis() + 3600000)
        return expiry.toInstant().toString()
    }
}