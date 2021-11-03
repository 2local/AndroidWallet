package com.android.l2l.twolocal.dataSourse.utils.error


data class GeneralError constructor(
    var throwable: Throwable? = null,
    var message: String? = null,
    var messageRes: Int? = null,
    var baseResponse: ErrorApp? = null
)

fun GeneralError.withError(
    throwable: Throwable? = null,
    message: String? = null,
    messageRes: Int? = null,
    baseResponse: ErrorApp? = null
): GeneralError {
    this.throwable = throwable
    this.message = message
    this.messageRes = messageRes
    this.baseResponse = baseResponse

    baseResponse?.let {
        this.message = baseResponse.message
    }
    throwable?.let {
        this.message = throwable.message
    }
    return GeneralError(throwable = this.throwable,
        message = this.message,
        messageRes = this.messageRes,
        baseResponse = this.baseResponse)
}