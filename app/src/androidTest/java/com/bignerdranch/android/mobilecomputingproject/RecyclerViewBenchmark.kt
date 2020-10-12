package com.bignerdranch.android.mobilecomputingproject

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.recyclerview.widget.RecyclerView
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


@LargeTest
@RunWith(AndroidJUnit4::class)
class RecyclerViewBenchmark {
    class LazyComputedList<T>(
        override val size: Int = Int.MAX_VALUE,
        private inline val compute: (Int) -> T
    ) : AbstractList<T>() {
        override fun get(index: Int): T = compute(index)
    }

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        activityRule.runOnUiThread {
            val activity = activityRule.activity

            // Set the RecyclerView to have a height of 1 pixel.
            // This ensures that only one item can be displayed at once.
            activity.recyclerView.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, 1)

            // Initialize the Adapter with fake data.
            // (Submit null first so both are synchronous for simplicity)
            // 1st ViewHolder will be inflated and displayed by the next onActivity callback

            //we need to implement buildRandomParagraph() 
            activity.adapter.submitList(null)
            activity.adapter.submitList(LazyComputedList { buildRandomParagraph() })
        }
    }

    @Test
    fun buildParagraph() {
        benchmarkRule.measureRepeated {
            // measure cost of generating paragraph - this is overhead in the primary scroll()
            // benchmark, but is a very small fraction of the amount of work there.
            buildRandomParagraph()
        }
    }

    @UiThreadTest
    @Test
    fun scroll() {
        val recyclerView = activityRule.activity.recyclerView
        assertTrue("RecyclerView expected to have children", recyclerView.childCount > 0)
        assertEquals("RecyclerView must have height = 1", 1, recyclerView.height)

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