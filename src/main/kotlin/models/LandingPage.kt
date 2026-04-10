package models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.time.Instant

@Serializable
data class LandingPage (
    val id: String? = null,
    val key: String? = "default",
    val header: Header,
    val hero: Hero,
    val statistics: Statistics,
    val features: Features,
    val targets: Targets,
    val testimonials: Testimonials,
    val faq: Faq,
    val cta: Cta,
    val footer: Footer,
    @Contextual val createAt: Instant? = Instant.now(),
    @Contextual val updatedAt: Instant? = null
)

// Classes auxiliares com nomes padronizados
@Serializable data class Statistics(val icone: String, val valor: String, val label: String)
@Serializable data class Header(val logo: String, val nomeDoApp: String, val textosDeLoginCadastro: String)
@Serializable data class Hero(val badge: String, val titulo: String, val descricao: String, val botoes: String, val linksDeDownload: String)
@Serializable data class Features(val titulo: String, val subTitulo: String, val itens: String)
@Serializable data class Targets(val publicoAlvo: String)
@Serializable data class Testimonials(val stats: String, val itens: String)
@Serializable data class Faq(val question: String, val answer: String)
@Serializable data class Cta(val titulo: String, val descricao: String, val botoes: String)
@Serializable data class Footer(val logo: String, val nomeDoApp: String, val copyright: String)