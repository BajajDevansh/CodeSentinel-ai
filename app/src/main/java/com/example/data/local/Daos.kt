package com.example.data.local

import androidx.room.*
import com.example.data.model.ChatMessage
import com.example.data.model.FindingItem
import com.example.data.model.RepositoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM cached_repositories ORDER BY name ASC")
    fun getAllRepositories(): Flow<List<RepositoryItem>>

    @Query("SELECT * FROM cached_repositories WHERE id = :repoId")
    suspend fun getRepositoryById(repoId: String): RepositoryItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<RepositoryItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepository(repo: RepositoryItem)

    @Delete
    suspend fun deleteRepository(repo: RepositoryItem)
}

@Dao
interface FindingDao {
    @Query("SELECT * FROM cached_findings WHERE repositoryId = :repoId")
    fun getFindingsByRepo(repoId: String): Flow<List<FindingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFindings(findings: List<FindingItem>)

    @Query("DELETE FROM cached_findings WHERE repositoryId = :repoId")
    suspend fun deleteFindingsByRepo(repoId: String)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM cached_chat_messages WHERE repositoryId = :repoId ORDER BY timestamp ASC")
    fun getMessagesByRepo(repoId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)
}
