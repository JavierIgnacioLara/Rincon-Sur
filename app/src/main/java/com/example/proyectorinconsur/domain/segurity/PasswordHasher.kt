package com.example.proyectorinconsur.domain.segurity

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


object PasswordHasher {
    private const val ITERACIONES = 120_000
    private const val BITS_CLAVE = 256
    private const val BYTES_SAL = 16

    fun nuevaSal(): String {
        val sal = ByteArray(BYTES_SAL)
        SecureRandom().nextBytes(sal)
        return sal.aHex()
    }

    fun hash(clave: String, salHex: String): String {
        val spec = PBEKeySpec(clave.toCharArray(), salHex.deHexABytes(), ITERACIONES, BITS_CLAVE)
        try {
            return fabrica().generateSecret(spec).encoded.aHex()
        } finally {
            spec.clearPassword()
        }
    }

    fun verificar(clave: String, salHex: String, hashEsperado: String): Boolean =
        MessageDigest.isEqual(hash(clave, salHex).toByteArray(), hashEsperado.toByteArray())

    private fun fabrica(): SecretKeyFactory = try {
        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    } catch (e: NoSuchAlgorithmException) {
        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    }

    private fun ByteArray.aHex(): String = joinToString("") { "%02x".format(it) }

    private fun String.deHexABytes(): ByteArray =
        ByteArray(length / 2) { i -> substring(i * 2, i * 2 + 2).toInt(16).toByte() }
}
