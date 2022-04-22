package com.livepokernotes.framework.datasource.network.implementation

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.framework.datasource.network.abstraction.SessionFirestoreService
import com.livepokernotes.framework.datasource.network.mappers.NetworkMapper.mapToDomainList
import com.livepokernotes.framework.datasource.network.mappers.NetworkMapper.mapToDomainModel
import com.livepokernotes.framework.datasource.network.mappers.NetworkMapper.mapToEntity
import com.livepokernotes.framework.datasource.network.model.SessionNetworkEntity
import com.livepokernotes.util.UtilExtensionFunctions.updatedNow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionFirestoreServiceImpl
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): SessionFirestoreService {

    override suspend fun insertOrUpdateSession(session: Session) {
        firestore
            .collection(SESSIONS_COLLECTION)
            .document(USER_ID)
            .collection(SESSIONS_COLLECTION)
            .document(session.id)
            .set(session.mapToEntity().updatedNow())
            .await()
    }

    override suspend fun insertOrUpdateSessions(sessions: List<Session>) {
        val collectionRef = firestore
            .collection(SESSIONS_COLLECTION)
            .document(USER_ID)
            .collection(SESSIONS_COLLECTION)

        firestore.runBatch { batch ->
            sessions.forEach { session ->  
                val documentRef = collectionRef.document(session.id)
                batch.set(documentRef, session.mapToEntity().updatedNow())
            }
        }
    }

    override suspend fun deleteSession(primaryKey: String) {
        firestore
            .collection(SESSIONS_COLLECTION)
            .document(USER_ID)
            .collection(SESSIONS_COLLECTION)
            .document(primaryKey)
            .delete()
            .await()
    }

    override suspend fun searchSessionById(primaryKey: String): Session? =
        firestore
            .collection(SESSIONS_COLLECTION)
            .document(USER_ID)
            .collection(SESSIONS_COLLECTION)
            .document(primaryKey)
            .get()
            .await()
            .toObject(SessionNetworkEntity::class.java)?.mapToDomainModel()

    override suspend fun deleteSessions(sessions: List<Session>) {
        val collectionRef = firestore
            .collection(SESSIONS_COLLECTION)
            .document(USER_ID)
            .collection(SESSIONS_COLLECTION)

        firestore.runBatch { batch ->
            sessions.forEach { session ->
                val documentRef = collectionRef.document(session.id)
                batch.delete(documentRef)
            }
        }
    }

    override suspend fun getAllSessions(): List<Session> =
        firestore
            .collection(SESSIONS_COLLECTION)
            .document(USER_ID)
            .collection(SESSIONS_COLLECTION)
            .get()
            .await()
            .toObjects(SessionNetworkEntity::class.java).mapToDomainList()

    companion object {
        const val SESSIONS_COLLECTION = "sessions"
        const val USERS_COLLECTION = "users"
        const val USER_ID = "ieVElHxFkaeRZEnFtv3BhLpF2nz2"
        const val USER_EMAIL = "pedromazarini@gmail.com"
    }

}