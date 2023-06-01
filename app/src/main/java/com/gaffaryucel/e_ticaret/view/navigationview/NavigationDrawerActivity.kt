package com.gaffaryucel.e_ticaret.view.navigationview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.ActivityNavigationDrawerBinding
import com.gaffaryucel.e_ticaret.databinding.NavHeaderNavigationDrawerBinding
import com.gaffaryucel.e_ticaret.model.User
import com.gaffaryucel.e_ticaret.view.navigationview.ui.home.HomeViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging


class NavigationDrawerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding
    private lateinit var viewModel : HomeViewModel
    private var userInfos = User()
    private lateinit var headerBinding: NavHeaderNavigationDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        headerBinding = NavHeaderNavigationDrawerBinding.bind(binding.navView.getHeaderView(0))
        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navBottomView : BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navView: NavigationView = binding.navView
        navView.menu.findItem(R.id.nav_basket).isVisible = false
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,R.id.nav_basket,
                R.id.nav_gallery,R.id.nav_profile,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navBottomView.menu.findItem(R.id.nav_profile).isVisible = false
        navBottomView.setupWithNavController(navController)
        getUserInfos(FirebaseAuth.getInstance().currentUser!!.uid)

        var coming = intent.getStringExtra("from")
        println("mm "+coming.toString())

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun getUserInfos(id : String){
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(id)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userInfos  = snapshot.getValue(User::class.java) ?: User()
                headerBinding.usernametext.text = userInfos.userName
                headerBinding.useremailtextview.text = userInfos.eMail
                if(userInfos.photo != null){
                    Glide.with(this@NavigationDrawerActivity).load(userInfos.photo).into(headerBinding.userimageview)
                }else{
                    headerBinding.userimageview.setImageResource(R.drawable.profile)
                }
            }
            override fun onCancelled(myerror: DatabaseError) {

            }
        })
    }
}