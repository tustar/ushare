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







