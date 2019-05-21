package com.tustar.ushare.data.entry

data class Response<out D, out E>(
        val data: D,
        val message: String,
        val code: Int,
        val extra: E
) {
    companion object {
        const val OK = 200
        const val FAILURE = 500
    }
}

fun <D, E> Response<out D, out E>.execute(ok: (D) -> Unit,
                                          failure: (String) -> Unit,
                                          other: ((D) -> Unit)? = null) {
    when (code) {
        Response.OK -> {
            ok(data)
        }
        Response.FAILURE -> {
            failure(message)
        }
        else -> {
            other?.invoke(data)
        }
    }
}






