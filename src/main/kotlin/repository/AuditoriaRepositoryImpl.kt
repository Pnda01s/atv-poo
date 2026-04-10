package repository

import com.mongodb.client.MongoCollection
import models.Auditoria
import models.TipoAcao
import org.litote.kmongo.*
import org.bson.conversions.Bson

class AuditoriaRepositoryImpl(
    private val collection: MongoCollection<Auditoria>
) : AuditoriaRepository {

    override fun listAll(page: Int, limit: Int, filters: Map<String, Any?>): Pair<List<Auditoria>, Long> {
        val bsonFilters = buildFilters(filters)
        val query = if (bsonFilters.isEmpty()) EMPTY_BSON else and(bsonFilters)
        val total = collection.countDocuments(query)
        val docs = collection.find(query)
            .sort(descending(Auditoria::createdAt))
            .skip((page - 1) * limit)
            .limit(limit)
            .toList()

        return Pair(docs, total)
    }

    private fun buildFilters(filters: Map<String, Any?>): List<Bson> {
        return filters.mapNotNull { (key, value) ->
            if (value == null) return@mapNotNull null
            
            when (key) {
                "instituicaoId" -> Auditoria::instituicaoId eq (value as String)
                "usuarioId" -> Auditoria::usuarioId eq (value as String)
                "entidade" -> Auditoria::entidade eq (value as String)
                "entidadeId" -> Auditoria::entidadeId eq (value as String)
                // Para filtrar pelo Enum TipoAcao
                "acao" -> Auditoria::acao eq (value as TipoAcao)
                else -> null
            }
        }
    }
}