package repository

import models.Filas
import models.QRcode
import org.litote.kmongo.*

interface QRcodeRepository {

    fun dataFilaQRcode(id: String): Filas?
    fun listAll(page: Int, limit: Int, filters: Map<String, Any?> = emptyMap()): Pair<List<QRcode>, Long>
    fun insertQRcode(qrcode: QRcode): QRcode
    fun listById(id: String): QRcode?
    fun regenerateQrcode(idAntigo: String): QRcode
}