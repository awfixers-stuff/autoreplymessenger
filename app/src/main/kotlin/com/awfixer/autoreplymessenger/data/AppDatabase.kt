package com.awfixer.autoreplymessenger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.awfixer.autoreplymessenger.data.dao.DraftDao
import com.awfixer.autoreplymessenger.data.dao.MessageDao
import com.awfixer.autoreplymessenger.data.dao.ThreadDao
import com.awfixer.autoreplymessenger.data.model.DraftEntity
import com.awfixer.autoreplymessenger.data.model.MessageEntity
import com.awfixer.autoreplymessenger.data.model.MmsPartEntity
import com.awfixer.autoreplymessenger.data.model.ThreadEntity

@Database(
        entities =
                [
                        MessageEntity::class,
                        MmsPartEntity::class,
                        ThreadEntity::class,
                        DraftEntity::class],
        version = 2,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun threadDao(): ThreadDao
    abstract fun draftDao(): DraftDao
}
