package com.example.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
// will use some code from androidX library
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {

    // Executes each task synchronously using Architecture Components.
    // since you are testing Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the process is killed.
        database = Room
            .inMemoryDatabaseBuilder(getApplicationContext(), ToDoDatabase::class.java) // only for testing
            .build()
    }

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // GIVEN - Insert a task.
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // WHEN - Get the task by id from the database.
        val loaded = database.taskDao().getTaskById(task.id)

        // THEN - The loaded data contains the expected values.
        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
        assertThat(loaded.description, `is`(task.description))
        assertThat(loaded.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        // 1. Insert a task into the DAO.
        // GIVEN - Insert a task.
        val originTask = Task("title", "description")
        database.taskDao().insertTask(originTask)

        // 2. Update the task by creating a new task with the same ID but different attributes.
        // WHEN - Update the task by id on the database.
        val updatedTask = Task("new title", "new description", true, originTask.id)
        database.taskDao().updateTask(updatedTask)

        // 3. Check that when you get the task by its ID, it has the updated values.
        // THEN - Get the task by id from the database & loaded data contains the expected values.
        val loaded = database.taskDao().getTaskById(originTask.id)
        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(originTask.id))
        assertThat(loaded.title, `is`(updatedTask.title))
        assertThat(loaded.description, `is`(updatedTask.description))
        assertThat(loaded.isCompleted, `is`(updatedTask.isCompleted))
    }

    @After
    fun closeDb() = database.close()
}
