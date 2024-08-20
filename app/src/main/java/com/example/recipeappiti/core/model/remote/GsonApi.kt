package com.example.recipeappiti.core.model.remote

import com.example.recipeappiti.core.model.remote.GsonServiceHelper.retrofit

object GsonApi {
    val service: RemoteMealDataSource by lazy {
        retrofit.create(RemoteMealDataSource::class.java)
    }
}
