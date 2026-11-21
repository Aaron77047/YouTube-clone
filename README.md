# YouTube Clone

A Kotlin + Jetpack Compose Android client inspired by YouTube, with an OLED-friendly dark theme and additional media-focused features.

## Features

- OLED dark mode
- Video and music uploads
- Background playback
- Comments and subscriptions
- Playlists
- Offline caching
- Creator analytics
- Interactive playable content
- Gemini AI integration

## Technology

- Kotlin
- Jetpack Compose / Material 3
- Android Gradle Plugin 9.1.1
- Room for local persistence
- Retrofit, OkHttp, and Moshi for networking
- Coil for image loading
- Firebase, including App Check and AI
- Kotlin Coroutines
- Robolectric and AndroidX test libraries

## Requirements

- Android Studio with an Android SDK supporting API 36
- JDK 11 or newer
- Android device or emulator running Android API 24 or newer
- A Gemini API key for AI features, when enabled
- Firebase configuration if Firebase-backed features are used

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/Aaron77047/YouTube-clone.git
   cd YouTube-clone
   ```

2. Open the project in Android Studio.

3. Copy the example environment file:

   ```bash
   cp .env.example .env
   ```

4. Optionally add `GEMINI_API_KEY` to `.env`. Do not commit real credentials.

5. Sync Gradle and run the `app` configuration on an API 24+ device or emulator.

## Running the App

From the terminal:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

On Windows:

```powershell
gradlew.bat assembleDebug
gradlew.bat installDebug
```

## Testing

Run the JVM test suite:

```bash
./gradlew test
```

Run instrumentation tests on a connected device or emulator:

```bash
./gradlew connectedAndroidTest
```

## Configuration

The Secrets Gradle Plugin reads `.env` and falls back to `.env.example`. Available configuration currently includes:

```dotenv
GEMINI_API_KEY=your_api_key
```

For release builds, signing variables can be supplied through the environment:

```dotenv
KEYSTORE_PATH=/absolute/path/to/upload.jks
STORE_PASSWORD=your_store_password
KEY_PASSWORD=your_key_password
```

Never commit `.env`, keystores, Firebase service credentials, or release passwords.

## Project Structure

```text
app/src/main/       Android application source and resources
app/src/test/       JVM unit tests
app/src/androidTest Instrumentation tests
gradle/             Gradle version catalog
```

## Contributing

Contributions are welcome. Before submitting a change:

1. Create a focused branch.
2. Follow the existing Kotlin and Compose conventions.
3. Add or update tests for behavioral changes.
4. Run `./gradlew test` and the relevant Android checks.
5. Open a pull request with a clear description.

## Security

Review [SECURITY.md](SECURITY.md) for supported versions and vulnerability reporting instructions.

## License

No license has been specified for this repository. By default, that means no permission is granted to reuse, modify, or distribute the project without explicit authorization from the owner.
