package it.application.team_tracker.model.remoteDataSource.entities

data class User(
    val id: String,
    val nickname: String,
    val fullName: String,
    val email: String,
    val location: String,
    val phone: String,
    val description: String,
    val photo: String?,
    /** Map<idTeam, role> */
    val teamMembers: List<Map<String, String>>,
    /**
       Map<comment || evaluation || teamId, String>
       this map will have always 3 element: comment, evaluation, teamId
     */
    val feedbacks: List<Map<String, String>>,
    /** Map<idUser, role> */
    val taskMembers: List<Map<String, String>>
)
