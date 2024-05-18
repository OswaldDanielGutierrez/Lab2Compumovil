import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Snack(
    val id: Long,
    val name: String,
    @SerialName(value = "imageUrl") val imageUrl: String,
    val price: Long,
    val tagline: String = "",
    val tags: Set<String> = emptySet()
)
