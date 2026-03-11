package com.app_will.geedrapplication.utils

import com.app_will.geedrapplication.R

class AuthValidator(
    var isValid: Boolean,
    var errorMessage: Int? = null
)

fun validateLogin(
    userIdentifier: String,
    userPassword: String
): AuthValidator {

    val strUserIdentifier = userIdentifier.trim()
    val strUserPassword = userPassword.trim()

    val validator = AuthValidator(true)

    if (strUserIdentifier.isNullOrEmpty() || strUserPassword.isNullOrEmpty()) {
        validator.apply {
            isValid = false
            errorMessage = R.string.auth_validator_utils_error_message
        }
    }

    return validator
}