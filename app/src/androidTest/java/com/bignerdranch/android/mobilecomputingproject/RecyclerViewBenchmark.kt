package com.bignerdranch.android.mobilecomputingproject

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.fragment.app.Fragment
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TAG = "RecyclerViewBenchmark"

@LargeTest
@RunWith(AndroidJUnit4::class)
class RecyclerViewBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        activityRule.runOnUiThread {
            val activity = activityRule.activity

        }
    }

    @UiThreadTest
    @Test
    fun scroll() {
        val activity = activityRule.activity
        val currentFragment : TaskListFragment = activity.supportFragmentManager.findFragmentById(R.id.fragment_container) as TaskListFragment
        val recyclerView = currentFragment.provideRecyclerView()
        assertTrue("RecyclerView expected to have children", recyclerView.childCount > 0)

        // RecyclerView has children, its items are attached, bound, and have gone through layout.
        // Ready to benchmark!
        benchmarkRule.measureRepeated {
            // Scroll RecyclerView by one item
            // this will synchronously execute: attach / detach(old item) / bind / layout
            recyclerView.scrollBy(0, recyclerView.getLastChild().height)
        }
    }
}

private fun ViewGroup.getLastChild(): View = getChildAt(childCount - 1)