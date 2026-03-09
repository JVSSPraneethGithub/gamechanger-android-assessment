@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GameChangerAssessment"
include(":app")

include(":network")
project(":network").projectDir = File(
    rootProject.projectDir,
    "foundation/network"
)

include(":persistence")
project(":persistence").projectDir = File(
    rootProject.projectDir,
    "foundation/persistence"
)

include(":coreUi")
project(":coreUi").projectDir = File(
    rootProject.projectDir,
    "foundation/coreUi"
)

include(":testing")
project(":testing").projectDir = File(
    rootProject.projectDir,
    "foundation/testing"
)

include(":framework-leagues")
project(":framework-leagues").projectDir = File(
    rootProject.projectDir,
    "features/leagues/domain"
)

include(":framework-teams")
project(":framework-teams").projectDir = File(
    rootProject.projectDir,
    "features/teams/domain"
)

include(":feature-leagues")
project(":feature-leagues").projectDir = File(
    rootProject.projectDir,
    "features/leagues/display")

include(":feature-teams")
project(":feature-teams").projectDir = File(
    rootProject.projectDir,
    "features/teams/display")