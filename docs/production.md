# Production Environment

Use `src/main/resources/application-prod.yml` in production. All sensitive values must come from environment variables or a secret manager.

Start command:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
mvn spring-boot:run
```

Required variables are listed in `.env.production.example`:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION`
- `DASHSCOPE_API_KEY`
- `FILE_STORAGE_TYPE=oss`
- `OSS_ENDPOINT`, `OSS_BUCKET`, `OSS_ACCESS_KEY_ID`, `OSS_ACCESS_KEY_SECRET`

Do not commit real API keys, database passwords, or JWT secrets.
