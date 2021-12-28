package ir.hodhod.hodhod.data.db

import androidx.room.Dao

@Dao
interface GeneralDao

/**
 * this extension function can be reduce boilerplate code
 *
 * @param block will be execute related method
 *
 * @param onError will be called whenever exception occur
 *
 * @return [R] is type of scope of function for example if your scope is kind of string it will be return string
 * */
inline fun <T : GeneralDao, R> T.executeQuery(
    block: T.() -> R,
    onError: (String?) -> Unit = {}
): R? {

    return try {
        block()
    } catch (e: Exception) {
        onError(e.message)
        null
    }
}