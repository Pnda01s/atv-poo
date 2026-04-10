package repository
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import models.Senha
import org.litote.kmongo.*

interface SenhaRepository {
    fun findAll(page: Int, limit: Int, filters: Map<String, Any?> = emptyMap()): Pair<List<Senha>, Long>
    fun findById(id: String): Senha?
    fun findByFila(filaId: String, page: Int, limit: Int, filters: Map<String, Any?> = emptyMap()): Pair<List<Senha>, Long>
    fun insertSenha(senha: Senha): Senha
    fun insertSenhaPresencial(senha: Senha): Senha
    fun setStatusCanceladaSenha(id: String): Senha?
    fun setStatusAtendimentoSenha(id: String): Senha?
    fun setStatusFinalizada(id: String): Senha?
    fun assumirSenha(id: String, admId: String): Senha?
}