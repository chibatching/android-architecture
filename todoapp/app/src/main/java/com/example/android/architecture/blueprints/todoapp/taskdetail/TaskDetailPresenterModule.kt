package com.example.android.architecture.blueprints.todoapp.taskdetail

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * [TaskDetailPresenter].
 */
fun taskDetailPresenterModule(view: TaskDetailContract.View, taskId: String) = Kodein.Module {
    bind<TaskDetailContract.View>() with instance(view)
    bind<String>("TaskId") with instance(taskId)
}
