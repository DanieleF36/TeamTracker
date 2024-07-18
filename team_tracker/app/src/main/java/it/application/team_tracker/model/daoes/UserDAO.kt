package it.application.team_tracker.model.daoes


interface UserDAO {
    fun getUser(id: String): User
    fun getByNickname(nickname: String): User
    fun getLikeNickname(nickname: String): User
    fun addUser(user: User)
    fun addUsers(vararg user: User)
    fun updateUser(user: User)
    fun deleteUser(id: String)
}