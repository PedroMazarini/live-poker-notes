package com.livepokernotes.business.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.livepokernotes.business.domain.model.Session

class SessionDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceListOfSessions(): List<Session> {
        return Gson()
            .fromJson(
                getSessionsFromFile("session_list.json"),
                object : TypeToken<List<Session>>() {}.type
            )
    }

    fun produceHashMapOfSessions(sessionList: List<Session>): HashMap<String, Session> =
        HashMap(sessionList.associateBy({ it.id }, { it }))

    fun getSessionsFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }
}