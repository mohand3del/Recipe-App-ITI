import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeappiti.R
import com.example.recipeappiti.home.model.Meal

class FavouriteRecyclerAdapter(private val action: (id:String) -> Unit,private val checkFavouirt: (id:String) -> Boolean) : RecyclerView.Adapter<FavouriteRecyclerAdapter.MealViewHolder>() {
    private var meals: List<Meal> = emptyList()
    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealImageView: ImageView = itemView.findViewById(R.id.item_image)
        val favouriteButton: ImageButton = itemView.findViewById(R.id.btn_loveH)
        val mealNameTextView: TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]

        if (checkFavouirt(meal.idMeal)) {
            holder.favouriteButton.setImageResource(R.drawable.loved_icon)
        }else{
            holder.favouriteButton.setImageResource(R.drawable.love_icon_light)
        }


        holder.mealNameTextView.text = meal.strMeal


        Glide.with(holder.itemView.context)
            .load(meal.strMealThumb)
            .into(holder.mealImageView)


        holder.favouriteButton.setOnClickListener {
          action(meal.idMeal)
        }
    }

    override fun getItemCount(): Int {
        return meals.size
    }
    fun updateData(meals: List<Meal>) {
        this.meals = meals
        notifyDataSetChanged()
    }
}
