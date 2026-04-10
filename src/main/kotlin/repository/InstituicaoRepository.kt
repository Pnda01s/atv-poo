package repository

import models.Instituicao

interface InstituicaoRepository {
    fun findAll(page: Int, limit: Int, filters: Map<String, Any?> = emptyMap()): Pair<List<Instituicao>, Long>
    fun findById(id: String): Instituicao?
    fun insert(instituicao: Instituicao): Instituicao
    fun update(id: String, updates: Map<String, Any?>): Boolean
    fun delete(id: String): Boolean
}