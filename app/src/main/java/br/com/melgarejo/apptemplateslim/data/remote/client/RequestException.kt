package br.com.melgarejo.apptemplateslim.data.remote.client

import java.io.IOException

import okhttp3.ResponseBody
import java.net.SocketTimeoutException

class RequestException private constructor(val errorCode: Int?,
    val errorMessage: String?,
    val errorType: ErrorType,
    val throwable: Throwable?) : Exception() {

    companion object {
        fun httpError(errorCode: Int, errorBody: ResponseBody? = null): RequestException {
            val message = ApiErrorsFormatter.deserialize(errorBody)?.let {
                if (it.errors != null) {
                    it.errors.joinToString("\n")
                } else {
                    it.errorMessage
                }
            }

            return RequestException(errorCode, message, ErrorType.HTTP, null)
        }

        fun networkError(exception: IOException): RequestException {
            return RequestException(null, null, ErrorType.NETWORK, exception)
        }

        fun timeoutError(exception: SocketTimeoutException): RequestException {
            return RequestException(null, null, ErrorType.TIMEOUT, exception)
        }

        fun unexpectedError(throwable: Throwable): RequestException {
            throwable.printStackTrace()
            return RequestException(null, throwable.message, ErrorType.UNEXPECTED, throwable)
        }
    }

    fun isHttpError(): Boolean {
        return errorType == ErrorType.HTTP
    }

    fun isNetworkError(): Boolean {
        return errorType == ErrorType.NETWORK
    }

    fun isUnexpectedError(): Boolean {
        return errorType == ErrorType.UNEXPECTED
    }

    fun isUnauthorizedError(): Boolean {
        return isHttpError() && HttpError.getErrorForCode(errorCode) == HttpError.UNAUTHORIZED
    }

    fun isTimeOutException(): Boolean {
        return errorType == ErrorType.TIMEOUT || HttpError.getErrorForCode(errorCode) == HttpError.TIMEOUT
    }

    fun isNotFoundException(): Boolean {
        return isHttpError() && HttpError.getErrorForCode(errorCode) == HttpError.NOT_FOUND
    }

    fun isUnprocessableEntity(): Boolean {
        return isHttpError() && HttpError.getErrorForCode(errorCode) == HttpError.UNPROCESSABLE_ENTITY
    }

    fun isConflictException(): Boolean {
        return isHttpError() && HttpError.getErrorForCode(errorCode) == HttpError.CONFLICT
    }

    private enum class ErrorType {
        HTTP, NETWORK, UNEXPECTED, TIMEOUT
    }

    enum class HttpError {
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        TIMEOUT,
        CONFLICT,
        UNPROCESSABLE_ENTITY,
        INTERNAL_SERVER_ERROR,
        UNEXPECTED_ERROR;

        companion object {
            fun getErrorForCode(errorCode: Int?): HttpError {
                errorCode?.let {
                    return when (it) {
                        400 -> BAD_REQUEST
                        401 -> UNAUTHORIZED
                        403 -> FORBIDDEN
                        404 -> NOT_FOUND
                        408 -> TIMEOUT
                        409 -> CONFLICT
                        422 -> UNPROCESSABLE_ENTITY
                        500 -> INTERNAL_SERVER_ERROR
                        else -> UNEXPECTED_ERROR
                    }
                }
                return UNEXPECTED_ERROR
            }
        }
    }
}
