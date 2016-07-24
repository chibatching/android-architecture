/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.tasks

import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.NavigationView
import android.support.test.espresso.IdlingResource
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.statistics.StatisticsActivity
import com.example.android.architecture.blueprints.todoapp.util.ActivityUtils
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider

class TasksActivity : AppCompatActivity() {

    private val drawerLayout: DrawerLayout by lazy { findViewById(R.id.drawer_layout) as DrawerLayout }

    private val injector = KodeinInjector()

    private val tasksPresenter: TasksContract.Presenter by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_act)


        // Set up the toolbar.
        (findViewById(R.id.toolbar) as Toolbar).let {
            setSupportActionBar(it)
        }
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        // Set up the navigation drawer.
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
        setupDrawerContent(findViewById(R.id.nav_view) as NavigationView)

        val tasksFragment = (supportFragmentManager.findFragmentById(R.id.contentFrame) as? TasksFragment) ?: run {
            // Create the fragment
            TasksFragment.newInstance().apply {
                ActivityUtils.addFragmentToActivity(supportFragmentManager, this, R.id.contentFrame)
            }
        }

        // Create the presenter
        injector.inject(Kodein {
            extend(appKodein())
            import(tasksPresenterModule(tasksFragment))
            bind<TasksContract.Presenter>() with provider { TasksPresenter(instance(), instance()) }
        })


        // Load previously saved state, if available.
        savedInstanceState?.let {
            tasksPresenter.filtering = it.getSerializable(CURRENT_FILTERING_KEY) as TasksFilterType
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(CURRENT_FILTERING_KEY, tasksPresenter.filtering)

        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.list_navigation_menu_item -> {
                }
                R.id.statistics_navigation_menu_item -> {
                    val intent = Intent(this@TasksActivity, StatisticsActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(intent)
                }
                else -> {
                }
            }// Do nothing, we're already on that screen
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }

    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.getIdlingResource()

    companion object {

        private val CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"
    }
}
