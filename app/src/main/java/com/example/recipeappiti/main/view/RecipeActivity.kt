package com.example.recipeappiti.main.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
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
import com.example.recipeappiti.auth.AuthActivity
import com.example.recipeappiti.core.model.local.repository.UserRepositoryImpl
import com.example.recipeappiti.core.model.local.source.LocalDataSourceImpl
import com.example.recipeappiti.core.model.local.source.UserDatabase
import com.example.recipeappiti.core.model.remote.Response
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder.createFailureResponse
import com.example.recipeappiti.core.util.CreateMaterialAlertDialogBuilder.createMaterialAlertDialogBuilderOkCancel
import com.example.recipeappiti.main.viewModel.RecipeActivityViewModel
import com.example.recipeappiti.main.viewModel.RecipeActivityViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class RecipeActivity : AppCompatActivity(), OnNavigationItemSelectedListener {

    private val viewModel: RecipeActivityViewModel by viewModels {


        val database = UserDatabase.getDatabaseInstance(this)
        val userDao = database.userDao()
        val localDataSource = LocalDataSourceImpl(userDao)

        val userRepository = UserRepositoryImpl(localDataSource)
        RecipeActivityViewModelFactory(userRepository)
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private var headerView: View? = null
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView

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

        initUi()

        viewModel.navigateToFragment.observe(this) { fragmentId ->
            fragmentId?.let {
                bottomNavigationView.selectedItemId = fragmentId
            }
        }

        viewModel.getUserName()

        viewModel.userName.observe(this) { response ->

            when (response) {
                is Response.Loading -> {}

                is Response.Success -> {
                    userName.text = response.data
                }

                is Response.Failure -> {
                    createFailureResponse(response, this)
                }
            }
        }

        viewModel.getUserEmail()

        viewModel.userEmail.observe(this) { response ->

            when (response) {
                is Response.Loading -> {}

                is Response.Success -> {
                    userEmail.text = response.data
                }

                is Response.Failure -> {
                    createFailureResponse(response, this)
                }
            }
        }
    }

    private fun initUi() {

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        drawer = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigation_view)

        headerView = navigationView.getHeaderView(0)

        headerView?.let {
            userName = it.findViewById(R.id.userNameDrawer)
            userEmail = it.findViewById(R.id.userEmailDrawer)
        }

        toggle = ActionBarDrawerToggle(
            this, drawer,
            R.string.drawer_opened, R.string.drawer_closed
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()

        //navigate by its own
        navController?.let { bottomNavigationView.setupWithNavController(it) }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawer.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.action_profile -> {
                Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.action_settings -> {
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.action_dark_mode -> {
                Toast.makeText(this, "Dark Mode selected", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.action_security_policies -> {
                Toast.makeText(this, "Security Policies selected", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.action_help_feedback -> {
                Toast.makeText(this, "Help & Feedback selected", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.action_about -> {
                Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.action_log_out -> {
                createMaterialAlertDialogBuilderOkCancel(
                    context = this,
                    title = "Log Out",
                    message = "Are you sure you want to log out?",
                    positiveBtnMsg = "Log Out",
                    negativeBtnMsg = "Cancel",
                    positiveBtnFun = {
                        viewModel.logOut()

                        viewModel.loggedOut.observe(this) { response ->
                            when (response) {
                                is Response.Loading -> {}

                                is Response.Success -> {
                                    startActivity(Intent(this, AuthActivity::class.java))
                                    finish()
                                }

                                is Response.Failure -> {
                                    createFailureResponse(response, this)
                                }
                            }
                        }
                    }
                )

                return true
            }

            R.id.action_delete_account -> {
                createMaterialAlertDialogBuilderOkCancel(
                    context = this,
                    title = "Delete Account",
                    message = "This action will permanently delete your account. Are you sure you want to continue?",
                    positiveBtnMsg = "Delete",
                    negativeBtnMsg = "Cancel",
                    positiveBtnFun = {
                        viewModel.deleteAccount()

                        viewModel.deletedAccount.observe(this) { response ->
                            when (response) {
                                is Response.Loading -> {}

                                is Response.Success -> {
                                    startActivity(Intent(this, AuthActivity::class.java))
                                    finish()
                                }

                                is Response.Failure -> {
                                    createFailureResponse(response, this)
                                }
                            }
                        }
                    }
                )

                return true
            }

            else -> {
                return false
            }
        }
    }
}