package com.example.bestplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import com.example.android.navigationadvancedsample.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private val lastId = "last"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigation()
        ComplementaryApp.instance.setScope(lifecycle.coroutineScope)

        if (savedInstanceState != null) {
            bottomNavigationView.selectedItemId = savedInstanceState.getInt(lastId)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val navGraphIds = listOf(
            R.navigation.calendar_navigation,
            R.navigation.list_navigation,
            R.navigation.projects_navigation
        )
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.my_nav_host_fragment,
            intent = intent
        )
        currentNavController = controller
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(lastId, bottomNavigationView.selectedItemId)
        println(bottomNavigationView.selectedItemId)
        super.onSaveInstanceState(outState)
    }
}