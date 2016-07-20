package com.example.android.architecture.blueprints.todoapp.tasks

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider


fun tasksPresenterModule(view: TasksContract.View) = Kodein.Module {
    bind<TasksContract.Presenter>() with provider { TasksPresenter(instance(), view) }
}
