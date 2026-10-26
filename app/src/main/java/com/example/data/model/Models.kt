package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cached_repositories")
data class RepositoryItem(
    @PrimaryKey val id: String,
    val name: String,
    val gitUrl: String,
    val defaultBranch: String = "main",
    val currentCommitSha: String = "a3f89c1",
    val status: String = "INDEXED",
    val fileCount: Int = 48,
    val linesOfCode: Int = 5120,
    val primaryLanguage: String = "Kotlin",
    val languagePercent: Int = 55,
    val lastAnalyzed: String = "Today at 10:30 AM",
    val criticalIssues: Int = 1,
    val highIssues: Int = 2,
    val testCoverage: Double = 64.2,
    val regressionRisk: Double = 42.5
) : Serializable

@Entity(tableName = "cached_findings")
data class FindingItem(
    @PrimaryKey val id: String,
    val repositoryId: String,
    val title: String,
    val category: String, // BUG, VULNERABILITY, CODE_SMELL
    val severity: String, // CRITICAL, HIGH, MEDIUM, LOW
    val confidence: Double,
    val toolConfirmed: Boolean,
    val detector: String,
    val filePath: String,
    val startLine: Int,
    val endLine: Int,
    val snippet: String,
    val explanation: String,
    val impact: String,
    val remediation: String,
    val cweRule: String
) : Serializable

@Entity(tableName = "cached_chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String,
    val repositoryId: String,
    val sender: String, // "USER" or "AI"
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val citationsJson: String = "" // Serialized citations
)

data class Citation(
    val filePath: String,
    val startLine: Int,
    val endLine: Int,
    val symbolName: String,
    val snippet: String,
    val retrievalMethod: String
)

data class DocumentationGap(
    val id: String,
    val symbolName: String,
    val symbolType: String,
    val filePath: String,
    val lineNumber: Int,
    val priority: String,
    val reason: String,
    val proposedDoc: String
)

data class TestGapReport(
    val repositoryId: String,
    val baselineLineCoverage: Double,
    val baselineBranchCoverage: Double,
    val postGenerationLineCoverage: Double,
    val postGenerationBranchCoverage: Double,
    val uncoveredMethodsCount: Int,
    val generatedTestCount: Int,
    val testsPassed: Int,
    val testsFailed: Int,
    val executionLog: String,
    val uncoveredTargets: List<UncoveredTarget>,
    val generatedTests: List<GeneratedTestCase>
)

data class UncoveredTarget(
    val className: String,
    val methodName: String,
    val filePath: String,
    val startLine: Int,
    val endLine: Int,
    val riskLevel: String
)

data class GeneratedTestCase(
    val testName: String,
    val targetMethod: String,
    val targetFile: String,
    val testCode: String,
    val passed: Boolean,
    val outputLog: String
)

data class ChangeImpactReport(
    val id: String,
    val baseCommit: String,
    val targetCommit: String,
    val regressionRiskScore: Double,
    val riskCategory: String,
    val directlyAffectedCount: Int,
    val transitivelyAffectedCount: Int,
    val riskFactors: List<RiskFactor>,
    val affectedComponents: List<AffectedComponent>,
    val affectedTests: List<String>,
    val missingTests: List<String>,
    val validationChecklist: List<String>
)

data class RiskFactor(
    val name: String,
    val weight: Double,
    val explanation: String
)

data class AffectedComponent(
    val name: String,
    val type: String,
    val filePath: String,
    val impactType: String,
    val dependencyDepth: Int
)

data class PatchProposal(
    val id: String,
    val findingId: String,
    val title: String,
    val filePath: String,
    val originalCode: String,
    val proposedCode: String,
    val diffText: String,
    val explanation: String,
    val status: String, // PENDING, APPROVED, REJECTED
    val validationStatus: String
)

data class ScanJob(
    val id: String,
    val type: String,
    val status: String,
    val progress: Int,
    val currentStage: String,
    val timestamp: String
)
