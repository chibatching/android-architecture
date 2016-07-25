/*
 * Copyright 2016, The Android Open Source Project
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

package com.example.android.architecture.blueprints.todoapp.addedittask

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.util.ActivityUtils
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.appKodein

/**
 * Displays an add or edit task screen.
 */
class AddEditTaskActivity : AppCompatActivity() {

    private val injector = KodeinInjector()

    private val addEditTaskPresenter: AddEditTaskContract.Presenter by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_act)

        // Set up the toolbar.
        (findViewById(R.id.toolbar) as? Toolbar)?.let {
            setSupportActionBar(it)
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        var taskId: String? = null

        val addEditTaskFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as? AddEditTaskFragment ?: run {
            AddEditTaskFragment.newInstance().apply {
                if (intent.hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                    taskId = intent.getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)
                    supportActionBar?.setTitle(R.string.edit_task)
                    this.arguments = Bundle().apply {
                        putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
                    }
                } else {
                    supportActionBar?.setTitle(R.string.add_task)
                }

                ActivityUtils.addFragmentToActivity(supportFragmentManager, this, R.id.contentFrame)
            }
        }

        // Create the presenter
        injector.inject(Kodein {
            extend(appKodein())
            import(addEditTaskPresenterModule(addEditTaskFragment, taskId))
            bind<AddEditTaskContract.Presenter>() with provider { AddEditTaskPresenter(instanceOrNull("TaskId"), instance(), instance()) }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.getIdlingResource()

    companion object {

        val REQUEST_ADD_TASK = 1
    }
}
