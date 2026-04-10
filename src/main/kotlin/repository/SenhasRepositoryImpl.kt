package repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.FindOneAndUpdateOptions
import models.Senha
import models.Status
import org.litote.kmongo.* import org.bson.conversions.Bson
import java.time.Instant

class SenhaRepositoryImpl(
    private val collection: MongoCollection<Senha>
) : SenhaRepository {

    override fun findAll(page: Int, limit: Int, filters: Map<String, Any?>): Pair<List<Senha>, Long> {
        val bsonFilters = buildFilters(filters)
        val query = if (bsonFilters.isEmpty()) EMPTY_BSON else and(bsonFilters)

        val total = collection.countDocuments(query)
        val docs = collection.find(query)
            .sort(ascending(Senha::posicao))
            .skip((page - 1) * limit)
            .limit(limit)
            .toList()

        return Pair(docs, total)
    }

    override fun findById(id: String): Senha? = collection.findOneById(id)

    override fun findByFila(filaId: String, page: Int, limit: Int, filters: Map<String, Any?>): Pair<List<Senha>, Long> {
        val newFilters = filters.toMutableMap()
        newFilters["filaId"] = filaId
        return findAll(page, limit, newFilters)
    }

    override fun insertSenha(senha: Senha): Senha {
        collection.insertOne(senha)
        return senha
    }

    override fun insertSenhaPresencial(senha: Senha): Senha {
        val presencial = senha.copy(precencial = true)
        collection.insertOne(presencial)
        return presencial
    }

    override fun setStatusCanceladaSenha(id: String): Senha? = updateStatus(id, Status.CACELADA)
    override fun setStatusAtendimentoSenha(id: String): Senha? = updateStatus(id, Status.EM_ATENDNIMENTO)
    override fun setStatusFinalizada(id: String): Senha? = updateStatus(id, Status.FINALIZADA)

    override fun assumirSenha(id: String, admId: String): Senha? {
        return collection.findOneAndUpdate(
            Senha::id eq id,
            combine(
                // Usando a classe Updates do driver oficial para não ter erro de tipo
                com.mongodb.client.model.Updates.set("operadorId", admId),
                com.mongodb.client.model.Updates.set("updatedAt", Instant.now())
            ),
            FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        )
    }

    private fun updateStatus(id: String, novoStatus: Status): Senha? {
        return collection.findOneAndUpdate(
            Senha::id eq id,
            combine(
                // Aqui passamos o Set de status e o Instant de forma explícita
                com.mongodb.client.model.Updates.set("status", setOf(novoStatus)),
                com.mongodb.client.model.Updates.set("updatedAt", Instant.now())
            ),
            FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        )
    }

    private fun buildFilters(filters: Map<String, Any?>): List<Bson> {
        return filters.mapNotNull { (key, value) ->
            if (value == null) return@mapNotNull null
            when (key) {
                "filaId" -> Senha::filaId eq (value as String)
                "status" -> Senha::status contains (value as Status)
                else -> null
            }
        }
    }
}