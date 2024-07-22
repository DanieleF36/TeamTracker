package it.application.team_tracker.model.remoteDataSource.entities

import com.google.firebase.firestore.PropertyName

data class User(
    override var id: String = "",
    var nickname: String = "",
    @set:PropertyName("fullname")
    @get:PropertyName("fullname")
    var fullName: String = "",
    var email: String = "",
    var location: String = "",
    var phone: String = "",
    var description: String = "",
    var photo: String? = "",
    /** Map<idTeam, role> */
    @set:PropertyName("team_members")
    @get:PropertyName("team_members")
    var teamMembers: Map<String, String> = emptyMap(),
    /**
       Map<comment || evaluation || teamId, String>
       this map will have always 3 element: comment, evaluation, teamId
     */
    var feedbacks: Map<String, String> = emptyMap(),
    /** Map<idUser, role> */
    @set:PropertyName("task_members")
    @get:PropertyName("task_members")
    var taskMembers: Map<String, String> = emptyMap()
):Entity
