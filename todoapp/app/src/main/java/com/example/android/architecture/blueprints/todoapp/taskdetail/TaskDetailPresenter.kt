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

package com.example.android.architecture.blueprints.todoapp.taskdetail

import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository

/**
 * Listens to user actions from the UI ([TaskDetailFragment]), retrieves the data and updates
 * the UI as required.
 *
 *
 * By marking the constructor with `@Inject`, Dagger injects the dependencies required to
 * create an instance of the TaskDetailPresenter (if it fails, it emits a compiler error). It uses
 * [TaskDetailPresenterModule] to do so.
 *
 *
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 */
class TaskDetailPresenter(
        var taskId: String?,
        private val tasksRepository: TasksRepository,
        private val taskDetailView: TaskDetailContract.View) : TaskDetailContract.Presenter {

    init {
        taskDetailView.setPresenter(this)
    }

    override fun start() {
        openTask()
    }

    private fun openTask() {
        if (null == taskId || taskId!!.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }

        taskDetailView.setLoadingIndicator(true)
        tasksRepository.getTask(taskId!!, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task?) {
                // The view may not be able to handle UI updates anymore
                if (!taskDetailView.isActive) {
                    return
                }
                taskDetailView.setLoadingIndicator(false)
                if (null == task) {
                    taskDetailView.showMissingTask()
                } else {
                    showTask(task)
                }
            }

            override fun onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!taskDetailView.isActive) {
                    return
                }
                taskDetailView.showMissingTask()
            }
        })
    }

    override fun editTask() {
        if (null == taskId || taskId!!.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }
        taskDetailView.showEditTask(taskId)
    }

    override fun deleteTask() {
        tasksRepository.deleteTask(taskId!!)
        taskDetailView.showTaskDeleted()
    }

    override fun completeTask() {
        if (null == taskId || taskId!!.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }
        tasksRepository.completeTask(taskId!!)
        taskDetailView.showTaskMarkedComplete()
    }

    override fun activateTask() {
        if (null == taskId || taskId!!.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }
        tasksRepository.activateTask(taskId!!)
        taskDetailView.showTaskMarkedActive()
    }

    private fun showTask(task: Task) {
        val title = task.title
        val description = task.description

        if (title != null && title.isEmpty()) {
            taskDetailView.hideTitle()
        } else {
            taskDetailView.showTitle(title)
        }

        if (description != null && description.isEmpty()) {
            taskDetailView.hideDescription()
        } else {
            taskDetailView.showDescription(description)
        }
        taskDetailView.showCompletionStatus(task.isCompleted)
    }
}
