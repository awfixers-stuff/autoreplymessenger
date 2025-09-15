package com.awfixer.autoreplymessenger.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.awfixer.autoreplymessenger.data.model.DraftEntity

@Dao
interface DraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertDraft(draft: DraftEntity)

    @Update suspend fun updateDraft(draft: DraftEntity)

    @Query("SELECT * FROM drafts WHERE threadId = :threadId")
    suspend fun getDraftByThreadId(threadId: Long): DraftEntity?

    @Query("DELETE FROM drafts WHERE threadId = :threadId") suspend fun deleteDraft(threadId: Long)
}
