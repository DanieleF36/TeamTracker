package it.application.team_tracker.model.remoteDataSource.firebase.entities

import kotlin.reflect.full.declaredMemberProperties

interface Entity{
    var id: String
    fun toMap(): Map<String, Any>{
        val kClass = this.javaClass.kotlin
        val ret = emptyMap<String, Any>().toMutableMap()
        kClass.declaredMemberProperties.forEach {
            if(it.name != "id")
                ret[it.name] = it.get(this) as Any
        }
        return ret.toMap()
    }
}