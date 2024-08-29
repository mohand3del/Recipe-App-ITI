import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeappiti.R
import com.example.recipeappiti.core.model.remote.Meal

class FavouriteRecyclerAdapter(
    private val changeFav: (id: String, isChange: Boolean, onComplete: (Boolean) -> Unit) -> Unit,
    private val goToDetails: (id: String) -> Unit
) : RecyclerView.Adapter<FavouriteRecyclerAdapter.MealViewHolder>() {
    private var meals: MutableList<Meal> = mutableListOf()

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealImageView: ImageView = itemView.findViewById(R.id.item_image)
        val favouriteButton: ImageView = itemView.findViewById(R.id.btn_loveH)
        val mealNameTextView: TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]

        // Initial check for the favorite state
        changeFav(meal.idMeal, false) { isFav ->
            if (isFav) {
                holder.favouriteButton.setImageResource(R.drawable.loved_icon)
            } else {
                holder.favouriteButton.setImageResource(R.drawable.love_icon_light)
            }
        }

        holder.mealNameTextView.text = meal.strMeal

        holder.itemView.setOnClickListener {

            goToDetails(meal.idMeal)

        }

        Glide.with(holder.itemView.context)
            .load(meal.strMealThumb)
            .into(holder.mealImageView)

        holder.favouriteButton.setOnClickListener {
            // Change the favorite state when the button is clicked
            changeFav(meal.idMeal, true) { isFav ->
                if (isFav) {
                    holder.favouriteButton.setImageResource(R.drawable.loved_icon)
                } else {
                    holder.favouriteButton.setImageResource(R.drawable.love_icon_light)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun updateData(meals: MutableList<Meal>) {
        this.meals = meals
        notifyDataSetChanged()
    }
}

