## Tech Stack — Patient Appointment Booking Web Application

### Frontend
- Framework: AngularJS (1.x) per team skills; confirm if modern Angular (v≥15) is acceptable for long-term support.
- Language: JavaScript (ES2015+). If modern Angular: TypeScript.
- UI: Bootstrap 5 (or Angular Material if modern Angular).
- State/HTTP: `$http`/`$resource` (AngularJS) or `HttpClient` + RxJS (modern Angular).
- Build: Webpack (AngularJS) or Angular CLI (modern Angular).
- Testing: Jasmine/Karma (AngularJS) or Jest/TestBed (modern Angular).
- Lint/format: ESLint, Prettier.
- Hosting/Delivery: S3 static hosting + CloudFront CDN, protected by AWS WAF.

### Backend (Serverless-first leveraging Spring Boot experience)
- API Edge: Amazon API Gateway (REST). Auth via Amazon Cognito (OAuth2/OIDC, JWTs).
- Compute: AWS Lambda using Spring Cloud Function (Java 21) for handlers; Step Functions for long-running orchestration (notifications, AI workflows).
- Alternative/augmentation: Spring Boot 3 on ECS Fargate for components needing persistent connections or heavy JVM warmup.
- Data persistence:
  - Amazon RDS (PostgreSQL or MySQL) for transactional entities (Patient, Provider, Appointment, etc.).
  - Amazon DynamoDB for availability/slot search, preference indices, and high-read workloads.
  - Amazon S3 for static assets or pre-visit document storage.
- Messaging/Async: Amazon SQS (queues) and Amazon EventBridge (event bus) for domain events.
- Caching: Amazon ElastiCache (Redis) for hot availability lookups; API Gateway caching where suitable.
- Notifications: Amazon SES (email); Amazon SNS or Twilio (SMS).
- AI services: Amazon Bedrock for preference understanding and doctor/itinerary/budget suggestions.
- Secrets and keys: AWS Secrets Manager and AWS KMS.
- Security/Networking: AWS WAF, AWS Shield, private subnets for data tiers, VPC endpoints, IAM least-privilege.
- Observability: Amazon CloudWatch (logs/metrics/alarms), AWS X-Ray (tracing), optional OpenSearch for log analytics.

### DevEx, Quality, and CI/CD
- VCS/CI: GitHub + GitHub Actions (preferred) or AWS CodePipeline/CodeBuild.
- IaC: AWS CDK (TypeScript) or Terraform (HCL) — standardize on one.
- Testing
  - Unit: Jasmine/Karma (frontend), JUnit 5 (backend).
  - Integration: REST Assured or Postman/newman.
  - E2E: Playwright or Cypress.
- Static analysis / security
  - Linting: ESLint (web), Checkstyle/SpotBugs (Java).
  - SAST: Semgrep or CodeQL.
  - DAST: OWASP ZAP in CI against ephemeral env.
  - Dependency scanning: OWASP Dependency-Check or Snyk.
- Release
  - Semantic versioning; dev → stage → prod with manual approval gates.
  - Blue/green or canary via CloudFront and Lambda aliases when applicable.

### Data and Compliance
- Encryption: TLS 1.2+ in transit; KMS-managed keys at rest for RDS/DynamoDB/S3.
- PHI handling: minimum necessary principle, structured audit logs, access reviews.
- Backups and recovery: automated RDS snapshots; DynamoDB PITR; cross-region replication policies where required.

### Local/Developer Tooling
- Local DB: H2 for dev-only (mirrors schema), seeded test data.
- Local emulation: LocalStack (optional) for AWS services; Testcontainers for integration tests.
- Package managers: npm (frontend), Maven/Gradle (backend).
