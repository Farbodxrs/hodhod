package ir.hodhod.hodhod.repositories.server.persistence

sealed class BaseResult<out T : Any, out U : Any> {
    data class Success<T : Any>(val data: T?) : BaseResult<T, Nothing>()
    data class Error<U : Any>(val message: U?) : BaseResult<Nothing, U>()
}
