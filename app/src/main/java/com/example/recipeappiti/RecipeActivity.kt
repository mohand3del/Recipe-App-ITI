package com.example.recipeappiti

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipeappiti.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class RecipeActivity : AppCompatActivity(), OnNavigationItemSelectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_recipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        drawer = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(
            this, drawer,
            R.string.drawer_opened, R.string.drawer_closed
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()

        bottomNavItemChangeListener(bottomNavigationView)

        navController?.let {
            bottomNavigationView.setupWithNavController(it)
        }
    }

    private fun bottomNavItemChangeListener(navView: BottomNavigationView) {
        navView.setOnItemSelectedListener { item ->
            if (item.itemId != navView.selectedItemId) {
                navController?.popBackStack(item.itemId, inclusive = true, saveState = false)
                navController?.navigate(item.itemId)
            }
            true
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawer.closeDrawer(GravityCompat.START)

        when (item.itemId) {


        }

        return false
    }
}