package com.example.GestionDesCours

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class TabNavigation : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_navigation)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.viewpager)
        tabLayout.setupWithViewPager(viewPager)

        val tabNavAdapter = TabNavAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )

        //tabNavAdapter.addFragment(FragmentPresence(), "Présence")
        //tabNavAdapter.addFragment(FragmentReclamation(), "Reclamation")
        tabNavAdapter.addFragment(FragmentListeEtudiant(), "Liste Etudiant")

        viewPager.adapter = tabNavAdapter
    }
}
