import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.home.repository.MealRepository

class FavoriteViewModelFactory(private val userRepository: UserRepository,private  val mealRepository: MealRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(userRepository,mealRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
