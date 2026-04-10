package services

import models.Auditoria
import models.TipoAcao
import repository.AuditoriaRepository // <-- Arrumado para 'i'
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.util.UUID

class AuditoriaService(
    private val auditoriaRepository: AuditoriaRepository // <-- Arrumado para 'i'
) {
    fun log(acao: TipoAcao, entidade: String, detalhes: String, entidadeId: String?) {
        val auditoria = Auditoria(
            id = UUID.randomUUID().toString(),
            acao = acao,
            entidade = entidade,
            entidadeId = entidadeId,
            dados = JsonObject(mapOf("detalhes" to JsonPrimitive(detalhes)))
        )
        // auditoriaRepository.insert(auditoria) // Tire o comentário quando o repositório estiver pronto
    }
}