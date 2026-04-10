package services

import dtos.request.CreateInstituicaoRequest
import dtos.request.UpdateFilaRequest
import models.Instituicao
import models.StatusInstituicao
import repository.InstituicaoRepository
import java.time.Instant

class InstituicaoService(
    private val repository: InstituicaoRepository
)  {

   fun criarInstituicao(request: InstituicaoRequest): Instituicao {
        val novaInstituicao = Instituicao(
            nome = request.nome,
            cnpj = request.cnpj,
            responsavel = request.responsavel,
            solicitante = request.solicitante,
            contatoUrl = request.contatoUrl,
            motivoRejeicao = request.motivoRejeicao,
            status = setOf(StatusInstituicao.PENDENTE),
            ativo = true,
            endereco = "", // Adicione se vier no request
            descricao = ""  // Adicione se vier no request
        )
        return repository.insert(novaInstituicao)
    }

     fun buscarPorId(id: String): Instituicao? = repository.findById(id)

     fun listarTodas(page: Int, limit: Int): Pair<List<Instituicao>, Long> {
        return repository.findAll(page, limit)
    }

     fun atualizarInstituicao(id: String, request: UpdateFilaRequest): Boolean {
        val updates = mapOf(
            "nome" to request.nome,
            "cnpj" to request.cnpj,
            "responsavel" to request.responsavel,
            "updatedAt" to Instant.now()
        )
        return repository.update(id, updates)
    }

     fun aprovarInstituicao(id: String, aprovadorId: String): Boolean {
        val updates = mapOf(
            "status" to setOf(StatusInstituicao.APROVADA),
            "aprovadoPor" to aprovadorId,
            "aprovadoEm" to Instant.now(),
            "updatedAt" to Instant.now()
        )
        return repository.update(id, updates)
    }
}


