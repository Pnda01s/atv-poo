package repository

import models.LandingPage

interface LandingPageRepository {
    fun findDefault(): LandingPage?
    fun upsertDefault(content: LandingPage): LandingPage
}