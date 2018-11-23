package com.tustar.ushare.data.entry

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "topics")
data class Topic @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Long,
        var user_id: Long,
        var title: String,
        var description: String,
        var created_at: Date,
        var updated_at: Date,
        var begin: Date,
        var end: Date,
        var shared: Boolean
)