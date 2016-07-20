package com.example.android.architecture.blueprints.todoapp.addedittask

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider


fun addEditTaskPresenterModule(view: AddEditTaskContract.View, taskId: String?) = Kodein.Module {
    bind<AddEditTaskContract.Presenter>() with provider { AddEditTaskPresenter(taskId, instance(), view) }
}
