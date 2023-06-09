package com.example.todoapp.tasks

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.example.todoapp.R
import com.example.todoapp.ServiceLocator
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.FakeAndroidTestRepository
import com.example.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {

    private lateinit var repository: TasksRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @Test
    fun clickTask_navigateToDetailFragmentOne() = runBlockingTest {
        // GIVEN - On the home screen
        repository.saveTask(Task("TITLE1", "DESCRIPTION1", false, "id1"))
        repository.saveTask(Task("TITLE2", "DESCRIPTION2", true, "id2"))

        val bundle = bundleOf("userMessage" to "0")
        val scenario = launchFragmentInContainer<TasksFragment>(bundle, R.style.Base_AppTheme)

        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the first list item
        onView(withId(R.id.tasks_list))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("TITLE1")), click()))

        // THEN - Verify that we navigate to the first detail screen
        verify(navController)
            .navigate(TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment("id1"))
    }

    @Test
    fun clickAddTaskButton_navigateToAddEditFragment() = runBlockingTest {
        // GIVEN - On the home screen
        val bundle = bundleOf("userMessage" to "0")
        val scenario = launchFragmentInContainer<TasksFragment>(bundle, R.style.Base_AppTheme)

        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the first list item
        onView(withId(R.id.add_task_fab))
            .perform(click())

        // THEN - Verify that we navigate to the first detail screen
        verify(navController)
            .navigate(
                TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                    null,
                    getApplicationContext<Context>().getString(R.string.add_task)
                )
            )
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }
}
