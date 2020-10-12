package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class MyBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun benchmarkSomeWork() = benchmarkRule.measureRepeated {
        doSomeWork()
    }

    fun doSomeWork() {

    }

    @Test
    fun simpleViewInflate() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inflater = LayoutInflater.from(context)
        val root = FrameLayout(context)
        inflater.inflate(R.layout.fragment_task_list, root, false)

        benchmarkRule.measureRepeated {
            
        }
    }
}