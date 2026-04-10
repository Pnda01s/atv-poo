package services

import ApiException

import PaginatedResponse
import PaginationParams
import dtos.request.*
import buildPaginatedResponse
import models.Filas
import models.TipoAtendimento
import models.Mesa
import models.ConfiguracaoQrCode
import repository.FilaRepository
import java.time.Instant
import java.util.UUID

/**
 * Service de Filas: Gerenciamento de filas e pontos de atendimento (mesas).
 */
class FilaService(
    private val filaRepository: FilaRepository,
    private val auditoriaService: AuditoriaService
) {

    fun listar(pagination: PaginationParams, filters: Map<String, Any?>): PaginatedResponse<Filas> {
        val (docs, total) = filaRepository.findAll(pagination.page, pagination.limit, filters)
        return buildPaginatedResponse(docs, total, pagination)
    }

    fun buscarPorId(id: String): Filas {
        return filaRepository.findByID(id) ?: throw ApiException(404, "Fila não encontrada")
    }

    fun criar(request: CreateFilaRequest, instituicaoId: String): Filas {
        val tiposAtendimento = request.tipoAtendimento.map {
            try {
                TipoAtendimento.valueOf(it)
            } catch (_: Exception) {
                throw ApiException(400, "Tipo de atendimento inválido: $it")
            }
        }.toSet()

        val fila = Filas(
            id = UUID.randomUUID().toString(),
            instituicaoID = instituicaoId,
            nome = request.nome,
            ativa = request.ativa,
            prioridadeHabilidata = request.prioridadeHabilidata,
            fidelidaeHabilitada = request.fidelidaeHabilitada,
            tempoMaximoAtendimento = request.tempoMaximoAtendimento,
            tipoAtendimento = tiposAtendimento,
            configuracoes = request.configuracoes?.let {
                ConfiguracaoQrCode(
                    modoQrCode = it.modoQrCode,
                    tempoExibicaoMin = it.tempoExibicaoMin,
                    tempoExpiracaoMin = it.tempoExpiracaoMin,
                    toleranciaMin = it.toleranciaMin,
                    tempoAlertaSegundos = it.tempoAlertaSegundos
                )
            },
            mesa = request.mesas.map { Mesa(it.numero, it.nome, it.ativa) },
            createdAt = Instant.now()
        )

        val criada = filaRepository.insertFilas(fila) ?: throw ApiException(500, "Erro ao criar fila")
        auditoriaService.log("FILA_CREATE", "Fila ${criada.nome} criada", criada.id)

        return criada
    }

    fun atualizar(id: String, request: UpdateFilaRequest): Boolean {
        buscarPorId(id) // verifica existência

        val updates = mutableMapOf<String, Any?>()
        request.nome?.let { updates["nome"] = it }
        request.ativa?.let { updates["ativa"] = it }
        request.prioridadeHabilidata?.let { updates["prioridadeHabilidata"] = it }
        request.fidelidaeHabilitada?.let { updates["fidelidaeHabilitada"] = it }
        request.tempoMaximoAtendimento?.let { updates["tempoMaximoAtendimento"] = it }

        request.tipoAtendimento?.let { list ->
            updates["tipoAtendimento"] = list.map { TipoAtendimento.valueOf(it) }.toSet()
        }

        request.configuracoes?.let { updates["configuracoes"] = it }
        request.mesas?.let { list ->
            updates["mesa"] = list.map { Mesa(it.numero, it.nome, it.ativa) }
        }

        updates["updatedAt"] = Instant.now()

        return filaRepository.updateFila(id, updates)
    }

    fun deletar(id: String): Boolean {
        buscarPorId(id)
        return filaRepository.deleteFila(id)
    }
}