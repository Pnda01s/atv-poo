package repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import models.Filas
import models.Senha // Certifique-se que o nome da model é Senha ou Senhas
import org.litote.kmongo.*
import org.bson.conversions.Bson

class FilaRepositoryImpl(
    private val collection: MongoCollection<Filas>,
    private val senhasCollection: MongoCollection<Senha> // CORREÇÃO: Injetando a coleção necessária
) : FilaRepository {

    override fun findAll(page: Int, limit: Int, filters: Map<String, Any?>): Pair<List<Filas>, Long> {
        val bsonFilters = buildFilters(filters)
        val finalFilter = if (bsonFilters.isEmpty()) EMPTY_BSON else and(bsonFilters)

        val total = collection.countDocuments(finalFilter)
        val docs = collection.find(finalFilter)
            .skip((page - 1) * limit)
            .limit(limit)
            .toList()

        return Pair(docs, total)
    }

    override fun insertFilas(fila: Filas): Filas? { // Adicione o ? se a interface permitir nulo
        collection.insertOne(fila)
        return fila
    }

    override fun findByID(id: String): Filas? {
        return collection.findOneById(id)
    }

    override fun updateFila(id: String, updates: Map<String, Any?>): Boolean {
        if (updates.isEmpty()) return false
        val setUpdates = updates.map { (key, value) -> Updates.set(key, value) }
        val result = collection.updateOneById(id, combine(setUpdates))
        return result.modifiedCount > 0
    }

    override fun deleteFila(id: String): Boolean {
        return collection.deleteOneById(id).deletedCount > 0
    }

    override fun countSenhasByFila(id: String): Long {
        // Verifique se o import models.Senha (ou Senhas) está correto no topo do arquivo
        return senhasCollection.countDocuments(Senha::filaId eq id)
    }

    private fun buildFilters(filters: Map<String, Any?>): List<Bson> {
        return filters.mapNotNull { (key, value) ->
            if (value == null) return@mapNotNull null

            when (key) {
                "nome" -> Filas::nome.regex(".*$value.*", "i")
                "ativa" -> Filas::ativa eq (value as Boolean)
                "instituicaoId" -> Filas::instituicaoID eq (value as String)
                else -> null
            }
        }
    }
}