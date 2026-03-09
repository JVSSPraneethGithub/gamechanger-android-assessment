package com.gamechanger.assessment.repository

import androidx.room.withTransaction
import com.gamechanger.assessment.api.GameChangerAssessmentApi
import com.gamechanger.assessment.database.GameChangerAssessmentDatabase
import com.gamechanger.assessment.entities.GameChangerAssessmentLeagueEntity
import com.gamechanger.assessment.serializables.GameChangerLeagueResponse
import com.gamechanger.assessment.serializables.GameChangerLeagueSerializableEntity
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
import kotlin.time.Clock

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Leagues framework Repository Unit-Test.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GameChangerAssessmentLeaguesRepositoryTest {
    private lateinit var testRepo : IGameChangerAssessmentLeaguesRepository
    private val transactionLamba = slot<suspend () -> Any?>()
    private val testSuccessResponse = GameChangerLeagueResponse(
        List(5) { index ->
            GameChangerLeagueSerializableEntity(
                id = index.toString(),
                name = "League $index",
                sport = "Sport"
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
            testDatabase.leaguesDao().insertAll(any())
        } returns Unit

        coEvery {
            testDatabase.leaguesDao().getAll()
        } returns testSuccessResponse.leaguesList.map {
            GameChangerAssessmentLeagueEntity(
                id = it.id,
                name = it.name,
                sport = it.sport,
                createdAt = Clock.System.now()
            )
        }

        testRepo = GameChangerAssessmentLeaguesRepository(
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
    fun `test fetchAllLeagues success`() = runTest {
        coEvery {
            testDatabase.leaguesDao().getAll()
        } returns testSuccessResponse.leaguesList.map {
            GameChangerAssessmentLeagueEntity(
                id = it.id,
                name = it.name,
                sport = it.sport,
                createdAt = Clock.System.now()
            )
        }

        coEvery {
            testApi.getAllLeagues()
        } returns testSuccessResponse

        val result = testRepo.fetchAllLeagues()
        advanceUntilIdle()

        coVerify(exactly = 1) { testApi.getAllLeagues() }
        coVerify(exactly = 1) { testDatabase.withTransaction(any<suspend () -> Any>()) }
        coVerify(exactly = 2) { testDatabase.leaguesDao() }

        assert(result.isSuccess)
        assert(result.getOrNull()?.size == 5)
    }

    @Test
    fun `test fetchAllLeagues failure`() = runTest {
        coEvery {
            testDatabase.leaguesDao().getAll()
        } throws Exception("Test Exception")

        coEvery {
            testApi.getAllLeagues()
        } throws Exception("Test Exception")

        val result = testRepo.fetchAllLeagues()
        advanceUntilIdle()

        coVerify(exactly = 1) { testApi.getAllLeagues() }
        coVerify(exactly = 0) { testDatabase.withTransaction(any<suspend () -> Any>()) }
        coVerify(exactly = 1) { testDatabase.leaguesDao() }

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Test Exception")
    }
}