package com.example.recipeappiti.core.model.remote.source

import com.example.recipeappiti.core.model.remote.source.GsonServiceHelper.retrofit

object GsonApi {
    val service: RemoteMealDataSource by lazy {
        retrofit.create(RemoteMealDataSource::class.java)
    }
}
