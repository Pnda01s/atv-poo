package repository
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import models.Auditoria
import org.litote.kmongo.*
interface AuditoriaRepository {

    fun listAll(
        page: Int,
        limit: Int,
        filters: Map<String, Any?> = emptyMap()
    ): Pair<List<Auditoria>, Long>


}