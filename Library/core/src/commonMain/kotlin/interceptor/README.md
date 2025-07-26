# Ktor Interceptor Usage

This module provides a Ktor interceptor for logging HTTP requests and responses.

## Dependencies Added

```kotlin
// In libs.versions.toml
ktor = "2.3.12"

// Common dependencies
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }

// Platform-specific engines
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" } // For JVM/Android
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" } // For iOS
```

## Usage Examples

### 1. Basic HttpClient with logging
```kotlin
import interceptor.KtorInterceptor

val client = KtorInterceptor.createHttpClient()

// Make a request
val response = client.get("https://api.example.com/data")
```

### 2. Custom log level
```kotlin
import io.ktor.client.plugins.logging.LogLevel

val client = KtorInterceptor.createHttpClient(
    logLevel = LogLevel.INFO
)
```

### 3. Custom logger
```kotlin
val client = KtorInterceptor.createHttpClient(
    logger = KtorInterceptor.CustomLogger()
)
```

### 4. Using extension functions
```kotlin
import interceptor.withInterceptor
import io.ktor.client.HttpClient

val client = HttpClient.withInterceptor(LogLevel.ALL)
```

### 5. Custom formatted logging
```kotlin
val client = KtorInterceptor.createHttpClientWithCustomLogging()
```

## What gets logged

The interceptor will log:
- 🚀 Outgoing requests (method, URL, headers)
- 📥 Incoming responses (status, headers, content type)
- 📋 Request methods
- 🌐 URLs
- 📦 Request/response bodies
- ℹ️ Other HTTP details

## Log levels available

- `LogLevel.ALL` - Logs everything
- `LogLevel.HEADERS` - Logs headers only
- `LogLevel.BODY` - Logs body only
- `LogLevel.INFO` - Basic information
- `LogLevel.NONE` - No logging

## Important Notes

1. Make sure to add the appropriate HTTP engine for your platform:
   - JVM/Android: `ktor-client-okhttp`
   - iOS: `ktor-client-darwin`
   - JS: `ktor-client-js`

2. Don't forget to close the HttpClient when done:
   ```kotlin
   client.close()
   ```

3. For production, consider using `LogLevel.INFO` or `LogLevel.NONE` to avoid logging sensitive data.
