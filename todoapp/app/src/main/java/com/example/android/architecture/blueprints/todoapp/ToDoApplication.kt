package com.example.android.architecture.blueprints.todoapp

import android.app.Application
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.tasksRepositoryModule
import com.github.salomonbrys.kodein.*


class ToDoApplication : Application(), KodeinAware {

    override val kodein: Kodein by Kodein.lazy {
        import(tasksRepositoryModule())
        import(applicationModule(this@ToDoApplication))
        bind<TasksRepository>() with singleton { TasksRepository(instance("Remote"), instance("Local")) }
    }
}
