package com.example.proyectorinconsur.validation

import com.example.proyectorinconsur.model.MAX_ACOMPANANTES

const val MIN_PASSWORD_LENGTH = 8

private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
private val NAME_REGEX = Regex("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$")


fun validateNameLettersOnly(name: String): String? {
    val clean = name.trim()
    return when {
        clean.isEmpty() -> "El nombre completo es obligatorio"
        !NAME_REGEX.matches(clean) -> "Solo letras y espacios"
        clean.length < 3 -> "Ingresa al menos 3 caracteres"
        else -> null
    }
}


fun validateEmail(email: String): String? {
    val clean = email.trim()
    return when {
        clean.isEmpty() -> "El correo es obligatorio"
        !EMAIL_REGEX.matches(clean) -> "Ingresa un correo válido (ej. nombre@correo.cl)"
        else -> null
    }
}


fun validateLoginPassword(pass: String): String? =
    if (pass.isEmpty()) "La contraseña es obligatoria" else null


fun validateStrongPassword(pass: String): String? {
    if (pass.isEmpty()) return "La contraseña es obligatoria"
    if (pass.length < MIN_PASSWORD_LENGTH) return "Mínimo $MIN_PASSWORD_LENGTH caracteres"
    if (pass.contains(' ')) return "No debe contener espacios"
    if (!pass.any { it.isUpperCase() }) return "Debe incluir una mayúscula"
    if (!pass.any { it.isLowerCase() }) return "Debe incluir una minúscula"
    if (!pass.any { it.isDigit() }) return "Debe incluir un número"
    if (!pass.any { !it.isLetterOrDigit() }) return "Debe incluir un símbolo"
    return null
}


fun validateConfirm(pass: String, confirm: String): String? = when {
    confirm.isEmpty() -> "Confirma tu contraseña"
    pass != confirm -> "Las contraseñas no coinciden"
    else -> null
}


fun validateEntryAccess(id: Int?): String? =
    if (id == null) "Selecciona el acceso de entrada" else null

fun validateExitAccess(id: Int?): String? =
    if (id == null) "Selecciona el acceso de salida" else null

fun validateGender(id: Int?): String? =
    if (id == null) "Selecciona el género" else null

fun validateAgeRange(id: Int?): String? =
    if (id == null) "Selecciona el rango de edad" else null

fun validateRecordType(id: Int?): String? =
    if (id == null) "Indica si transitó o compró" else null


fun validateCompanions(adultos: Int, ninos: Int): String? = when {
    adultos < 0 || ninos < 0 -> "Los acompañantes no pueden ser negativos"
    adultos > MAX_ACOMPANANTES || ninos > MAX_ACOMPANANTES -> "Máximo $MAX_ACOMPANANTES acompañantes por tipo"
    else -> null
}

fun validateActiveShift(hasActiveShift: Boolean): String? =
    if (!hasActiveShift) "Debes iniciar un turno antes de registrar personas" else null


