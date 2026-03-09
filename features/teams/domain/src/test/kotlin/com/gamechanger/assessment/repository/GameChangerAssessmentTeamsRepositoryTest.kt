package com.gamechanger.assessment.repository

import androidx.room.withTransaction
import com.gamechanger.assessment.api.GameChangerAssessmentApi
import com.gamechanger.assessment.database.GameChangerAssessmentDatabase
import com.gamechanger.assessment.entities.GameChangerAssessmentTeamEntity
import com.gamechanger.assessment.serializables.GameChangerTeamSerializableEntity
import com.gamechanger.assessment.serializables.GameChangerTeamsResponse
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.time.Clock

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Teams framework Repository Unit-Test.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GameChangerAssessmentTeamsRepositoryTest {
    private lateinit var testRepo: IGameChangerAssessmentTeamsRepository
    private var randomIndex by Delegates.notNull<Int>()
    private val transactionLamba = slot<suspend () -> Any?>()

    private val testSuccessResponse = GameChangerTeamsResponse(
        List(5){ index ->
            GameChangerTeamSerializableEntity(
                id = index.toString(),
                name = "Team $index",
                sport = "Sport",
                gender = "Gender",
                league = "League",
                stadium = "Stadium-$index",
                location = "Location_$index",
                country = "Country $index",
                badge = "Badge",
                banner = "Banner",
                description = "Description: $index",
                website = "website.com/$index",
                facebook = "www.facebook.com/$index/fb",
                twitter = "twitter.com/tw_$index",
                instagram = "instagram.com/inst_$index",
                youtube = "youtube.com/yt_$index",
                locked = "Locked"
            )
        }
    )

    @MockK
    private lateinit var testApi: GameChangerAssessmentApi

    @MockK
    private lateinit var testDatabase: GameChangerAssessmentDatabase

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this)

        mockkStatic("androidx.room.RoomDatabaseKt")

        coEvery {
            testDatabase.withTransaction(capture(transactionLamba))
        } coAnswers {
            transactionLamba.captured.invoke()
        }

        coEvery {
            testDatabase.teamsDao().insertAll(any())
        } returns Unit

        coEvery {
            testDatabase.teamsDao().insertTeam(any())
        } returns Unit

        testRepo = GameChangerAssessmentTeamsRepository(
            testApi,
            testDatabase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `test fetchAllTeamsForLeague success`() = runTest {
        coEvery {
            testDatabase.teamsDao().fetchAllTeamsForLeague(any())
        } returns testSuccessResponse.teamsList.map {
            GameChangerAssessmentTeamEntity(
                id = it.id,
                name = it.name,
                sport = it.sport,
                gender = it.gender,
                league = it.league,
                stadium = it.stadium,
                location = it.location,
                country = it.country,
                badge = it.badge,
                banner = it.banner,
                description = it.description,
                website = it.website,
                facebook = it.facebook,
                twitter = it.twitter,
                instagram = it.instagram,
                youtube = it.youtube,
                locked = it.locked,
                createdAt = Clock.System.now()
            )
        }

        coEvery {
            testApi.searchAllTeams(any())
        } returns testSuccessResponse

        val result = testRepo.fetchAllTeamsForLeague("League Name")
        advanceUntilIdle()

        coVerify(exactly = 1) { testApi.searchAllTeams(any()) }
        coVerify(exactly = 1) { testDatabase.withTransaction(any<suspend () -> Any>()) }
        coVerify(exactly = 2) { testDatabase.teamsDao() }

        assert(result.isSuccess)
        assert(result.getOrNull()?.size == 5)
    }

    @Test
    fun `test fetchAllTeamsForLeague failure`() = runTest {
        coEvery {
            testDatabase.teamsDao().fetchAllTeamsForLeague(any())
        } throws Exception("Test Exception")

        coEvery {
            testApi.searchAllTeams(any())
        } throws Exception("Test Exception")

        val result = testRepo.fetchAllTeamsForLeague("League Name")
        advanceUntilIdle()

        coVerify(exactly = 1) { testApi.searchAllTeams(any()) }
        coVerify(exactly = 0) { testDatabase.withTransaction(any<suspend () -> Any>()) }
        coVerify(exactly = 1) { testDatabase.teamsDao() }

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Test Exception")
    }

    @Test
    fun `test fetchTeam success`() = runTest {
        coEvery {
            testDatabase.teamsDao().fetchTeam(any(), any(), any())
        } returns testSuccessResponse.teamsList
            .let { list ->
                randomIndex = Random.nextInt(0, list.size)
                list[randomIndex].let {
                    GameChangerAssessmentTeamEntity(
                        id = it.id,
                        name = it.name,
                        sport = it.sport,
                        gender = it.gender,
                        league = it.league,
                        stadium = it.stadium,
                        location = it.location,
                        country = it.country,
                        badge = it.badge,
                        banner = it.banner,
                        description = it.description,
                        website = it.website,
                        facebook = it.facebook,
                        twitter = it.twitter,
                        instagram = it.instagram,
                        youtube = it.youtube,
                        locked = it.locked,
                        createdAt = Clock.System.now()
                    )
                }
            }

        coEvery {
            testApi.searchTeamByName(any())
        } returns testSuccessResponse

        val result = testRepo.fetchTeam(
            randomIndex.toString(),
            "Team $randomIndex",
            "League"
        )
        advanceUntilIdle()

        coVerify(exactly = 1) { testApi.searchTeamByName(any()) }
        coVerify(exactly = 1) { testDatabase.withTransaction(any<suspend () -> Any>()) }
        coVerify(exactly = 2) { testDatabase.teamsDao() }

        assert(result.isSuccess)
        assert(result.getOrNull()?.name == "Team $randomIndex")
    }

    @Test
    fun `test fetchTeam failure`() = runTest {
        coEvery {
            testDatabase.teamsDao().fetchTeam(any(), any(), any())
        } throws Exception("Test Exception")

        coEvery {
            testApi.searchTeamByName(any())
        } throws Exception("Test Exception")

        val result = testRepo.fetchTeam("1", "Team 1", "League")
        advanceUntilIdle()

        coVerify(exactly = 1) { testApi.searchTeamByName(any()) }
        coVerify(exactly = 0) { testDatabase.withTransaction(any<suspend () -> Any>()) }
        coVerify(exactly = 1) { testDatabase.teamsDao() }

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Test Exception")
    }
}