import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappiti.auth.repository.UserRepository
import com.example.recipeappiti.home.model.Meal
import com.example.recipeappiti.home.repository.MealRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val userRepository: UserRepository,
    private val mealRepository: MealRepository
) : ViewModel() {


    private val _favourites = MutableLiveData<MutableList<String>>()

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> get() = _isFavourite

    private val _recipes = MutableLiveData<MutableList<Meal>>()
    val recipes: LiveData<MutableList<Meal>> get() = _recipes


    init {
        loadFavoriteItems()
    }


    private fun loadFavoriteItems() {
        viewModelScope.launch {
            _favourites.value =
                userRepository.getLoggedInUser()?.favourites?.toMutableList() ?: mutableListOf()
        }.invokeOnCompletion {
            getMeals()
        }
    }

    private fun getMeals() {
        viewModelScope.launch {
            val updatedMeals = _recipes.value ?: mutableListOf()
            _favourites.value?.forEach { mealId ->
                val meal = mealRepository.getMealById(mealId)
                updatedMeals.add(meal)
            }
            _recipes.value = updatedMeals
        }
    }


    fun changeFavouriteState(recipeId: String) {
        viewModelScope.launch {
            val currentFavouriteState = _favourites.value?.contains(recipeId)
            if (currentFavouriteState == true) {
                _favourites.value?.remove(recipeId)
            } else {
                _favourites.value?.add(recipeId)

            }

            userRepository.updateFavourites(_favourites.value?.toMutableList() ?: mutableListOf())
            _isFavourite.value = when (currentFavouriteState) {
                true -> false
                false -> true
                null -> true
            }
        }
    }

    fun checkFavouriteState(recipeId: String) {
        _isFavourite.value = _favourites.value?.contains(recipeId)
    }


//    fun removeFavorite(item: FavoriteItem) {
//        viewModelScope.launch {
//            repository.removeFavorite(item)
//            loadFavoriteItems()
//        }
//    }

}
