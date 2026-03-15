package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_sameTime_CorrectIdSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 20.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboardForRank_rank1() {
        val results = (1..9).map { GameResult(it.toLong(), "p$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(1)

        verify(mockedService).getGameResults()
        assertEquals(4, res.size)
        assertEquals(listOf(1,2,3,4), res.map { it.id.toInt() })
    }

    @Test
    fun test_getLeaderboardForRank_rank3() {
        val results = (1..9).map { GameResult(it.toLong(), "p$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(3)

        assertEquals(6, res.size)
        assertEquals(listOf(1,2,3,4,5,6), res.map { it.id.toInt() })
    }

    @Test
    fun test_getLeaderboardForRank_rank5() {
        val results = (1..9).map { GameResult(it.toLong(), "p$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(5)

        assertEquals(7, res.size)
        assertEquals(listOf(2,3,4,5,6,7,8), res.map { it.id.toInt() })
    }

    @Test
    fun test_getLeaderboardForRank_rank7() {
        val results = (1..9).map { GameResult(it.toLong(), "p$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(7)

        assertEquals(6, res.size)
        assertEquals(listOf(4,5,6,7,8,9), res.map { it.id.toInt() })
    }

    @Test
    fun test_getLeaderboardForRank_rank9() {
        val results = (1..9).map { GameResult(it.toLong(), "p$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(9)

        assertEquals(4, res.size)
        assertEquals(listOf(6,7,8,9), res.map { it.id.toInt() })
    }

    @Test
    fun test_getLeaderboardForRank_rank10_throwsException() {
        val results = (1..9).map { GameResult(it.toLong(), "p$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(results)

        assertThrows<ResponseStatusException> {
            controller.getLeaderboard(10)
        }
    }

}