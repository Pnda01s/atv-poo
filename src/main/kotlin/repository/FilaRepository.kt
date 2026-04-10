package repository
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import models.Filas
import org.litote.kmongo.*
interface FilaRepository {
    fun findAll(page: Int, limit: Int, filters: Map<String, Any?> = emptyMap()): Pair<List<Filas>, Long>
    fun insertFilas(fila: Filas):Filas?
    fun findByID(id: String): Filas?
    fun updateFila(id: String, updates: Map<String, Any?>): Boolean
    fun deleteFila(id:String): Boolean
    fun countSenhasByFila(id: String): Long
}