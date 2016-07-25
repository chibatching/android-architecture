package com.example.android.architecture.blueprints.todoapp.addedittask

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance


fun addEditTaskPresenterModule(view: AddEditTaskContract.View, taskId: String?) = Kodein.Module {
    bind<AddEditTaskContract.View>() with instance(view)
    taskId?.let {
        bind<String>("TaskId") with instance(it)
    }
}
