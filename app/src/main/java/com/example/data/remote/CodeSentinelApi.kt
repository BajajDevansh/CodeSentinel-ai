package com.example.data.remote

import com.example.data.model.*
import retrofit2.Response
import retrofit2.http.*

data class LoginDto(val email: String, val password: String)
data class RegisterDto(val email: String, val password: String, val fullName: String, val githubToken: String?)
data class AuthTokenDto(val accessToken: String, val refreshToken: String, val userId: String, val email: String, val fullName: String)
data class ConnectRepoDto(val name: String, val gitUrl: String, val defaultBranch: String?)
data class ChatQueryDto(val question: String, val conversationId: String?)
data class ChatResultDto(val answer: String, val citations: List<CitationDto>)
data class CitationDto(val filePath: String, val startLine: Int, val endLine: Int, val symbolName: String, val snippet: String, val retrievalMethod: String)
data class ExecuteTestDto(val testCaseId: String, val runInSandbox: Boolean)
data class CoverageResultDto(val baselineLineCoverage: Double, val postGenerationLineCoverage: Double, val lineCoverageDelta: Double, val executionLog: String)
data class PatchApproveDto(val approved: Boolean)

interface CodeSentinelApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginDto): Response<AuthTokenDto>

    @POST("api/v1/auth/register")
    suspend fun register(@Body body: RegisterDto): Response<AuthTokenDto>

    @GET("api/v1/repositories")
    suspend fun getRepositories(): Response<List<RepositoryItem>>

    @POST("api/v1/repositories")
    suspend fun connectRepository(@Body body: ConnectRepoDto): Response<RepositoryItem>

    @GET("api/v1/repositories/{id}/findings")
    suspend fun getFindings(@Path("id") repoId: String): Response<List<FindingItem>>

    @POST("api/v1/repositories/{id}/chat")
    suspend fun askQuestion(@Path("id") repoId: String, @Body body: ChatQueryDto): Response<ChatResultDto>

    @GET("api/v1/repositories/{id}/tests/gaps")
    suspend fun getTestGaps(@Path("id") repoId: String): Response<TestGapReport>

    @POST("api/v1/repositories/{id}/tests/execute")
    suspend fun executeTests(@Path("id") repoId: String, @Body body: ExecuteTestDto): Response<CoverageResultDto>

    @GET("api/v1/repositories/{id}/impact/latest")
    suspend fun getChangeImpact(@Path("id") repoId: String): Response<ChangeImpactReport>

    @GET("api/v1/repositories/{id}/patches")
    suspend fun getPatches(@Path("id") repoId: String): Response<List<PatchProposal>>

    @POST("api/v1/patches/{patchId}/approve")
    suspend fun approvePatch(@Path("patchId") patchId: String, @Body body: PatchApproveDto): Response<PatchProposal>
}
