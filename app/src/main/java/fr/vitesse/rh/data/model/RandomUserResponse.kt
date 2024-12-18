package fr.vitesse.rh.data.model

data class RandomUserResponse(
    val results: List<RandomUser>
)

data class RandomUser(
    val name: Name,
    val email: String,
    val picture: Picture
)

data class Name(
    val first: String,
    val last: String
)

data class Picture(
    val large: String
)