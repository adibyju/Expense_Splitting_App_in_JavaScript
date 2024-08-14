package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.my_drawer_layout)
        navigationView = findViewById(R.id.navigation)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        replaceFragment(EventsFragment())

        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            drawerLayout.closeDrawer(GravityCompat.START)
            when (id) {
                R.id.nav_events -> {
                    replaceFragment(EventsFragment())
                    true
                }
                R.id.nav_payments -> {
                    replaceFragment(PaymentsFragment())
                    true
                }
                R.id.nav_logout -> {
                    logout()
                    startLoginActivity()
                    true
                }
                else -> {
                    false
                }

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun logout() {
        val uriObj = BaseUrl()
        val url = uriObj.url +  "/auth/logout"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, null,
            { response ->
                Toast.makeText(this, "Logout successful ${response.toString()}", Toast.LENGTH_SHORT).show()
                startLoginActivity()
            },
            { error ->
                Toast.makeText(this, "Logout failed ${error.toString()}", Toast.LENGTH_SHORT).show()
            }
        )

        val queue = VolleySingleton.getInstance(this).requestQueue
        queue.add(jsonObjectRequest)
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}