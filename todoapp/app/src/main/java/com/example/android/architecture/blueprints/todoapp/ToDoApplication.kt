package com.example.android.architecture.blueprints.todoapp

import android.app.Application
import android.content.Context
import com.example.android.architecture.blueprints.todoapp.data.source.tasksRepositoryModule
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.provider


class ToDoApplication : Application(), KodeinAware {

    override val kodein: Kodein by LazyKodein {
        Kodein {
            import(tasksRepositoryModule)
            bind<Context>() with provider { this@ToDoApplication }
        }
    }
}
