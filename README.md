# GameChanger Take-home Assessment Android mobile-app

[Detailed Requirements can be viewed here](Assessment.md)

### System Requirements:

* Android Studio Panda 2 | 2025.3.2
* Android SDK - Compile: 36.1 | Min: 24

### Configurations:

* Gradle: 9.3.1
* Android Gradle Plugin: 9.1.0
* Kotlin: 2.3.10
* Android Studio JBR 21

### Implementation Details:

* Leagues list fetched from `all_leagues.php` end-point.
* Teams list fetched from `search_all_teams.php` end-point.
* Team details fetched from `searchteams.php` end-point.

### Architecture:

* *MVVM* architecture-pattern using `Jetpack ViewModels`.
* Separation of Concerns across `Jetpack Compose` UI, ViewModels, UseCase and Repository classes.
* Dependency-Management using `Hilt`.
* UI State-management across Loading, Success and Error states using `StateFlow`.
* Offline Persistence using `Room`.
* Feature Modularization following `SOLID design-principles`.
* `Unit-tests` included for ViewModels and Repositories.
* `Release` build-type doesn't have Signing-Configuration key-store.

### Testing:

* On MacOS, ensure JAVA_HOME is `<Absolute Path to Android Studio>/Contents/jbr/Contents/Home`
* `./gradlew test` to execute Unit-tests.
* `./gradlew connectedCheck` to execute UI-tests.
* Clear all Gradle caches as necessary in case of build and test failures in order to prevent corrupt Gradle configuration meddling with Gradle execution.

## Evaluation Criteria

### Must Have (Pass/Fail)
- [ Y ] App builds and runs without crashes
- [ Y ] Displays data from TheSportsDB API
- [ Y ] Clear MVVM architecture
- [ Y ] Feature module separation maintained
- [ Y ] At least some unit tests present
- [ Y ] README with setup instructions

### Eligibility for Bonus Points

***Implemented***

* Offline Caching with `Room` Persistence.
* Dark-mode and Light-mode themes.
* Loss-less orientation configuration changes.
* Fade-Transition Animations are Jetpack Compose default.
* Error-states alongside Loading and Success states.

***Not Implemented***
* No Paging details available in [original requirements](Assessment.md)
* Keyword Search, presumed not-mandatory according to the [original requirements](Assessment.md)