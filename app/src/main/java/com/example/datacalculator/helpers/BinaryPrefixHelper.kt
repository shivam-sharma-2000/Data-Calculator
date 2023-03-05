package com.example.datacalculator.helpers

import java.text.DecimalFormat

private const val BYTE = 1L
private const val KB = BYTE shl 10 // Byte *1024
private const val MB = KB shl 10 // KB *1024
private const val GB = MB shl 10 // MB *1024
private const val TB = GB shl 10 // GB *1024
private const val PB = TB shl 10 // TB *1024
private const val EB = PB shl 10 // PB *1024

class BinaryPrefixHelper {
    private val decimalFormat: DecimalFormat = DecimalFormat("#.##")

    private fun formatSize(size: Double, divider: Long, unitName: String): String? {
        return "${decimalFormat.format(size / divider)} $unitName"
    }

    fun toBinaryPrefixes(size: Double): String? {
        require(size >= 0) { "Invalid file size: $size" }
        if (size >= EB) return formatSize(size, EB, "EB")
        if (size >= PB) return formatSize(size, PB, "PB")
        if (size >= TB) return formatSize(size, TB, "TB")
        if (size >= GB) return formatSize(size, GB, "GB")
        if (size >= MB) return formatSize(size, MB, "MB")
        return if (size >= KB) formatSize(size, KB, "KB")else formatSize(size, BYTE, "Bytes")
    }

}