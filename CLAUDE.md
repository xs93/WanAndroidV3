# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Build commands

**Before any Gradle command, set JAVA_HOME to JDK 17:**

```bash
export JAVA_HOME="D:/Program Files/Java/jdk-17.0.9"
```

```bash
# Build the main app
./gradlew :app:assembleDebug

# Build the demo app (sandbox for testing components)
./gradlew :app-demo:assembleDebug

# Run all unit tests
./gradlew test

# Run unit tests for a specific module
./gradlew :feature:lib_data:test

# Run a single test class
./gradlew :feature:lib_data:test --tests "com.github.xs93.wan.data.MyTest"

# Run instrumentation tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## Architecture

This is a **multi-module Android project** (Kotlin 2.4.10, minSdk 26, targetSdk 37, AGP 9.2.1) for a
wanandroid.com client. It uses **Clean Architecture + MVI**, with all UI built on Views (XML +
ViewBinding, no Compose in production code).

> AGP 9 uses the new `compileSdk { version = release(libs.versions.targetSdk.get().toInt()) }` DSL
> in
> module `build.gradle.kts` files. Versions are centralized in `gradle/libs.versions.toml`.

### Module layers (bottom-up)

| Layer         | Modules              | Purpose                                                                                                          |
|---------------|----------------------|------------------------------------------------------------------------------------------------------------------|
| **Core**      | `core/lib_*`         | Infrastructure: base classes, network (Retrofit/OkHttp), KV storage (MMKV), MVI framework, Coil wrapper, CameraX |
| **Model**     | `feature/lib_model`  | Data entities with Moshi `@JsonClass` annotations                                                                |
| **Data**      | `feature/lib_data`   | Retrofit API interfaces, Repository classes, AccountDataManager, Hilt DI modules                                 |
| **Domain**    | `feature/lib_domain` | Use cases (e.g. `CollectOrNotArticleUseCase`)                                                                    |
| **Feature**   | `feature/module_*`   | Screens with MVI (ViewModel + Activity/Fragment), each module self-contained                                     |
| **Router**    | `feature/lib_router` | Therouter path constants, RouterHelper                                                                           |
| **Resources** | `feature/lib_res`    | Shared drawables, strings, themes                                                                                |
| **Common**    | `feature/lib_common` | Cross-feature config, adapters, widgets, services                                                                |

### Dependency injection

**Hilt** throughout (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`). Annotation
processing uses **KSP** (never kapt). DI modules live in `feature/lib_data/di/` (SingletonComponent)
and `feature/module_main/di/` (ActivityRetainedComponent for `MainServiceImpl`).

> Koin appears in `libs.versions.toml` but is **not** used — do not introduce it. Hilt is the only
> DI
> framework in the codebase.

### Navigation

Uses **Therouter** (`cn.therouter`). Route paths are string constants in `PageRouterPath`.
Activities/Fragments annotated with `@Route(path = "...")`. Cross-module service location uses
`TheRouter.get(IMusicService::class.java)`.

### MVI pattern (`core/lib_ui`)

- `BaseViewModel` provides `mviStates()`, `mviActions()`, `mviEvents()` delegates
- `uiState` is a `MutableStateFlow` updated via `uiState.update { copy(...) }`
- `uiAction` is a `Channel<ACTION>` — collected automatically and routed to `onAction()`
- `uiEvent` is a `Channel<EVENT>` for one-shot events
- Activities/Fragments observe via `observerState()` and `observerEvent()` extensions

### Network layer (`core/lib_network`)

`EasyRetrofit` singleton manages Retrofit instances. Key interceptors in order:

- `DynamicBaseUrlInterceptor` (per-request base URL override via `@DynamicBaseUrl`)
- `DynamicTimeoutInterceptor` (per-request timeout via `@Timeout`)
- `NetworkInterceptor` (connectivity check)
- `HandleErrorInterceptor` (HTTP errors → typed exceptions)
- `CacheInterceptor` (AES-encrypted cache, TTL via `@Cache`)

`HandleErrorConverterFactory` catches parse errors and checks API error codes. `WanErrorHandler`
shows toasts for network/server errors and redirects to login on auth failure.

Error codes: the wanandroid API uses `errorCode` in a `WanResponse<T>` wrapper. `0` = success,
non-zero = application error.

### Data persistence

**No Room database**. Local state uses MMKV (`core/lib_kv`) via `MMKVOwner` delegated properties (
`by bool`, `by string`, `by stateFlow`, etc.). `core/lib_kv` also provides a DataStore wrapper
(`DataStoreOwner`, `DataStorePreferences`) for cases needing DataStore semantics. User session
managed by `AccountDataManager` with a `StateFlow<AccountState>` that validates cookies at init.

### Key conventions

- API calls return `Result<WanResponse<T>>`. The `Result` wrapping happens in the **Retrofit API
  layer** (via a call adapter) — API interface methods are declared as
  `suspend fun ...(): Result<WanResponse<T>>`, and Repository methods simply pass the call through
  without their own `runSuspendCatching`. Do not re-wrap in repositories.
- `AppConstant.baseUrl = "https://www.wanandroid.com/"`
- Data classes use Moshi `@JsonClass(generateAdapter = false)` with KSP-generated adapters
- Preference delegates in `MMKVOwner` use underscore-prefixed keys (e.g.,
  `var isNightMode by bool("_night_mode")`)