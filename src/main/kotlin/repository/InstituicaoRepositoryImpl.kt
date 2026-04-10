package repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import models.Instituicao
import models.StatusInstituicao
import org.litote.kmongo.*
import org.bson.conversions.Bson

class InstituicaoRepositoryImpl(
    private val collection: MongoCollection<Instituicao>
) : InstituicaoRepository {

    override fun findAll(page: Int, limit: Int, filters: Map<String, Any?>): Pair<List<Instituicao>, Long> {
        val bsonFilters = buildFilters(filters)
        val query = if (bsonFilters.isEmpty()) EMPTY_BSON else and(bsonFilters)

        val total = collection.countDocuments(query)
        val docs = collection.find(query)
            .skip((page - 1) * limit)
            .limit(limit)
            .toList()

        return Pair(docs, total)
    }

    override fun findById(id: String): Instituicao? = collection.findOneById(id)

    override fun insert(instituicao: Instituicao): Instituicao {
        collection.insertOne(instituicao)
        return instituicao
    }

    override fun update(id: String, updates: Map<String, Any?>): Boolean {
        if (updates.isEmpty()) return false
        
        val setUpdates = updates.map { (key, value) -> Updates.set(key, value) }
        val result = collection.updateOneById(id, combine(setUpdates))
        
        return result.modifiedCount > 0
    }

    override fun delete(id: String): Boolean {
        return collection.deleteOneById(id).deletedCount > 0
    }

    private fun buildFilters(filters: Map<String, Any?>): List<Bson> {
        return filters.mapNotNull { (key, value) ->
            if (value == null) return@mapNotNull null
            
            when (key) {
                "nome" -> Instituicao::nome.regex(".*$value.*", "i")
                "cnpj" -> Instituicao::cnpj eq (value as String)
                "ativo" -> Instituicao::ativo eq (value as Boolean)
                "status" -> Instituicao::status contains (value as StatusInstituicao)
                else -> null
            }
        }
    }
}