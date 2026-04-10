package repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import models.Filas
import models.QRcode
import org.litote.kmongo.*
import org.bson.conversions.Bson

import java.time.Instant // ADICIONE ESTA LINHA
class QRcodeRepositoryImpl(
    private val qrcodeCollection: MongoCollection<QRcode>,
    private val filasCollection: MongoCollection<Filas>
) : QRcodeRepository {

    override fun dataFilaQRcode(id: String): Filas? {
        return filasCollection.findOneById(id)
    }

    override fun listAll(page: Int, limit: Int, filters: Map<String, Any?>): Pair<List<QRcode>, Long> {
        val bsonFilters = buildFilters(filters)
        val query = if (bsonFilters.isEmpty()) EMPTY_BSON else and(bsonFilters)

        val total = qrcodeCollection.countDocuments(query)
        val docs = qrcodeCollection.find(query)
            .skip((page - 1) * limit)
            .limit(limit)
            .toList()

        return Pair(docs, total)
    }

    override fun insertQRcode(qrcode: QRcode): QRcode {
        qrcodeCollection.insertOne(qrcode)
        return qrcode
    }

    override fun listById(id: String): QRcode? {
        return qrcodeCollection.findOneById(id)
    }

    override fun regenerateQrcode(idAntigo: String): QRcode {
        val qrcodeAntigo = qrcodeCollection.findOneById(idAntigo) 
            ?: throw NoSuchElementException("QR Code com ID $idAntigo não encontrado.")

        val novoQrcode = qrcodeAntigo.copy(
            id = null, // Se o seu modelo permitir gerar novo ID automático
            createdAt = Instant.now() 
        )

        qrcodeCollection.deleteOneById(idAntigo)
        qrcodeCollection.insertOne(novoQrcode)
        
        return novoQrcode
    }

    private fun buildFilters(filters: Map<String, Any?>): List<Bson> {
        return filters.mapNotNull { (key, value) ->
            if (value == null) return@mapNotNull null
            
            when (key) {
                "filaId" -> QRcode::filaId eq (value as String)
                "ativo" -> QRcode::ativo eq (value as Boolean)
                else -> null
            }
        }
    }
}