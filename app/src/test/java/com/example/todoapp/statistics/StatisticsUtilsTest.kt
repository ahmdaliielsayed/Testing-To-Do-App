package com.example.todoapp.statistics

import com.example.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest {

    // naming follow this pattern
    // subjectUnderTest_actionOrInput_resultState
    // subjectUnderTest --> method & class & feature

    // GIVEN - WHEN - THEN

    // Arrange - Act - Assert (AAA)

    @Test
    fun getActiveAndCompletedStats_allActiveNoCompleted_returnsHundredZero() {
        // GIVEN a list of tasks with a single, active, task
        // Create an active tasks (the false makes this active)
        val tasks = listOf(
            Task("title", "desc", isCompleted = false)
        )

        // WHEN you call getActiveAndCompletedStats
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // THEN there are 100% active tasks and 0% completed tasks
        // Check the result
        assertEquals(result.activeTasksPercent, 100f)
        assertEquals(result.completedTasksPercent, 0f)
    }

    @Test
    fun getActiveAndCompletedStats_sixtyActiveFortyCompleted_returnsSixtyForty() {
        // GIVEN
        // Create an active tasks (the false makes this active)
        val tasks = listOf(
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false)
        )

        // WHEN
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // THEN
        // Check the result
        assertEquals(result.activeTasksPercent, 60f)
        assertEquals(result.completedTasksPercent, 40f)
    }

    @Test
    fun getActiveAndCompletedStats_emptyList_returnsZeroZero() {
        // GIVEN
        // Create an empty list
        val tasks = emptyList<Task>()

        // WHEN
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // THEN
        // Check the result
        assertEquals(result.activeTasksPercent, 0f)
        assertEquals(result.completedTasksPercent, 0f)
    }

    @Test
    fun getActiveAndCompletedStats_null_returnsZeroZero() {
        // GIVEN
        // Create null list
        val tasks = null

        // WHEN
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // THEN
        // Check the result
        assertEquals(result.activeTasksPercent, 0f)
        assertEquals(result.completedTasksPercent, 0f)
    }
}
