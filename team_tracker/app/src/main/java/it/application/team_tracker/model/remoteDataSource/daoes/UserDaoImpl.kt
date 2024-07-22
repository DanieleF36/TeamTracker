package it.application.team_tracker.model.remoteDataSource.daoes

import android.net.Uri
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.daoes.remote.UserDAO
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class UserDaoImpl: FirebaseDAO(), UserDAO {
    override fun getUser(userId: String): Flow<User?> {
        return getDocument<it.application.team_tracker.model.remoteDataSource.entities.User>("users/$userId").map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getUserWithUpdate(userId: String): Flow<Pair<ChangeType, User>?>{
        return getDocumentWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.User>("users/$userId").map {
            if(it != null)
                Pair(it.first,fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun getUserLikeNickname(nickname: String): Flow<User?> {
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.User>(db.collection("users").whereGreaterThanOrEqualTo("nickname", nickname)).filter {
            it?.nickname?.contains(nickname) ?: false
        }.map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getUserLikeNicknameWithUpdate(nickname: String): Flow<Pair<ChangeType, User>?> {
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.User>(db.collection("users").whereGreaterThanOrEqualTo("nickname", nickname)).filter {
            it?.second?.nickname?.contains(nickname) ?: false
        }.map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun updateUser(oldUser: User, newUser: User): Flow<Boolean> {
        return updateDocument("/users/${newUser.id}", oldUser, newUser)
    }

    private fun fromRemoteToNeutral(u: it.application.team_tracker.model.remoteDataSource.entities.User): User{
        return User(
            u.id,
            u.nickname,
            u.fullName,
            u.email,
            u.location,
            u.phone,
            u.description,
            if(u.photo!=null) Uri.parse(u.photo) else null,
            u.teamMembers.map { it.key },
            u.taskMembers.map { it.key },
            null
        )
    }

}