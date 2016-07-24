package com.example.android.architecture.blueprints.todoapp.tasks

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance


fun tasksPresenterModule(view: TasksContract.View) = Kodein.Module {
    bind<TasksContract.View>() with instance(view)
}
