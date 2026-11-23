## Local Setup

### Prerequisites

- Java 17
- Maven
- Git

The application uses an in-memory H2 database. No external database setup is required.

### Encryption key configuration

The application uses AES-256-GCM to encrypt sensitive vault data. A 256-bit master key must be configured before starting the application.

Create a `.env-secret` file in the project root:

```properties
KEY_BASE_64=<YOUR_GENERATED_KEY>
```

**Generate a secure key**

On Windows PowerShell 5.1 or later:

```powershell
$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
try {
    $rng.GetBytes($bytes)
    [Convert]::ToBase64String($bytes)
}
finally {
    $rng.Dispose()
}
```

On Linux/macOS:

```bash
openssl rand -base64 32
```

Copy the generated Base64 value into `.env-secret`.

**Security:** Never commit `.env-secret` or share the generated key. The file is excluded through `.gitignore`.

### Run the application

```bash
mvn clean spring-boot:run
```

On startup, Hibernate creates the H2 schema and the application populates the database with fictitious vault entries.

### API documentation

Once the application is running, access Swagger UI at:

http://localhost:8080/swagger-ui/index.html

The application runs on port 8080 by default.