package com.livepokernotes.framework.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.livepokernotes.framework.datasource.cache.model.SessionCacheEntity

const val SESSION_ORDER_ASC: String = ""
const val SESSION_ORDER_DESC: String = "-"
const val SESSION_FILTER_COMMENT = "comment"
const val SESSION_FILTER_DATE_CREATED = "created_at"

const val ORDER_BY_ASC_DATE_UPDATED = SESSION_ORDER_ASC + SESSION_FILTER_DATE_CREATED
const val ORDER_BY_DESC_DATE_UPDATED = SESSION_ORDER_DESC + SESSION_FILTER_DATE_CREATED
const val ORDER_BY_ASC_COMMENT = SESSION_ORDER_ASC + SESSION_FILTER_COMMENT
const val ORDER_BY_DESC_COMMENT = SESSION_ORDER_DESC + SESSION_FILTER_COMMENT

const val SESSION_PAGINATION_PAGE_SIZE = 30

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(sessionCacheEntity: SessionCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSessions(sessions: List<SessionCacheEntity>): LongArray

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun searchSessionById(id: String): SessionCacheEntity?

    @Query("DELETE FROM sessions WHERE id IN (:ids)")
    suspend fun deleteSessions(ids: List<String>): Int

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

    @Query("SELECT * FROM sessions")
    suspend fun getAllSessions(): List<SessionCacheEntity>

    @Query("""
    UPDATE sessions 
    SET 
    comment = :comment,
    updated_at = :updated_at
    WHERE id = :primaryKey
    """)
    suspend fun updateSession(
        primaryKey: String,
        comment: String,
        updated_at: String
    ): Int

    @Query("DELETE FROM sessions WHERE id = :primaryKey")
    suspend fun deleteSession(primaryKey: String): Int

    @Query(
        """
    SELECT * FROM sessions 
    WHERE comment LIKE '%' || :query || '%'
    ORDER BY 
        CASE WHEN :filterAndOrder = :updated_atASC THEN updated_at END ASC, 
        CASE WHEN :filterAndOrder = :updated_atDESC THEN updated_at END DESC,
        CASE WHEN :filterAndOrder = :commentASC THEN comment END ASC,
        CASE WHEN :filterAndOrder = :commentDESC THEN comment END DESC
    LIMIT (:page * :pageSize)
    """
    )
    suspend fun searchSessions(
        query: String,
        page: Int,
        filterAndOrder: String = ORDER_BY_ASC_DATE_UPDATED,
        pageSize: Int = SESSION_PAGINATION_PAGE_SIZE,
        updated_atASC: String = ORDER_BY_ASC_DATE_UPDATED,
        updated_atDESC: String = ORDER_BY_DESC_DATE_UPDATED,
        commentASC: String = ORDER_BY_ASC_COMMENT,
        commentDESC: String = ORDER_BY_DESC_COMMENT
    ): List<SessionCacheEntity>

    @Query("SELECT COUNT(*) FROM sessions")
    suspend fun getNumSessions(): Int
}