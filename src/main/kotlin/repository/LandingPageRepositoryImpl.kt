package repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReplaceOptions
import models.LandingPage
import org.litote.kmongo.*

class LandingPageRepositoryImpl(
    private val collection: MongoCollection<LandingPage>
) : LandingPageRepository {

    override fun findDefault(): LandingPage? {
        // Busca pela chave única "default"
        return collection.findOne(LandingPage::key eq "default")
    }

    override fun upsertDefault(content: LandingPage): LandingPage {
        // ReplaceOptions().upsert(true) cria o documento se não existir
        val options = ReplaceOptions().upsert(true)

        collection.replaceOne(
            LandingPage::key eq "default",
            content,
            options
        )
        return content
    }
}