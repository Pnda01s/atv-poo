package services

import ApiException
import dtos.request.*
import models.*
import repository.*
import java.time.Instant
import java.util.UUID


class SenhaService(
    private val senhaRepository: SenhaRepository,
    private val filaRepository: FilaRepository,
    private val qrCodeRepository: QRcodeRepository,

) {


    private fun calcularProximaPosicao(filaId: String): Int {
        val filtros: Map<String, Any?> = mapOf("status" to Status.AGUARDANDO)
        val (_, total) = senhaRepository.findByFila(filaId, 1, 1, filtros)
        return (total + 1).toInt()
    }

}