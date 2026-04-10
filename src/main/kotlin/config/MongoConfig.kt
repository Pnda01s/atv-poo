package config

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import models.Usuario
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

/**
 * Configuração da conexão MongoDB via KMongo.
 */
class MongoConfig(
    connectionString: String = System.getenv("MONGO_URI") ?: "mongodb+srv://fs_aula:60egYQtIbESPneFx@cluster0.bpx8s93.mongodb.net/Aula",
    databaseName: String = System.getenv("MONGO_DB") ?: "Aula"
) {
    private val client = KMongo.createClient(connectionString)
    val database: MongoDatabase = client.getDatabase(databaseName)

    // Defina o tipo explicitamente para ajudar o Koin a não se perder
    val usuarios: MongoCollection<Usuario> get() = database.getCollection<Usuario>("usuarios")
}