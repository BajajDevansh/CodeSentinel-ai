// MongoDB initialization script for CodeSentinel collections & indexes
db = db.getSiblingDB('codesentinel');

// Users indexes
db.createCollection('users');
db.users.createIndex({ "email": 1 }, { unique: true });

// Repositories indexes
db.createCollection('repositories');
db.repositories.createIndex({ "userId": 1 });
db.repositories.createIndex({ "gitUrl": 1 });

// Analysis jobs indexes
db.createCollection('analysis_jobs');
db.analysis_jobs.createIndex({ "repositoryId": 1, "createdAt": -1 });

// Findings indexes
db.createCollection('findings');
db.findings.createIndex({ "repositoryId": 1, "severity": 1 });
db.findings.createIndex({ "repositoryId": 1, "category": 1 });

// Documentation gaps
db.createCollection('documentation_gaps');
db.documentation_gaps.createIndex({ "repositoryId": 1 });

// Test reports & Change Impact
db.createCollection('test_gap_reports');
db.test_gap_reports.createIndex({ "repositoryId": 1, "createdAt": -1 });

db.createCollection('change_impact_reports');
db.change_impact_reports.createIndex({ "repositoryId": 1, "createdAt": -1 });

print("CodeSentinel MongoDB indexes initialized successfully.");
