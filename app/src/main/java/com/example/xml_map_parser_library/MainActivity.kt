package com.example.xml_map_parser_library

import CinemaChooseFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.xml_map_parser_library.databinding.ActivityMainBinding
import com.example.xml_map_parser_library.russiamap.presentation.RussiaMapFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            loadFragment(CinemaChooseFragment.newInstance(), CinemaChooseFragment.TAG)
        }

        binding.bnView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.cinema->{
                    loadFragment(CinemaChooseFragment.newInstance(), CinemaChooseFragment.TAG)
                    true
                }
                R.id.map->{
                    loadFragment(RussiaMapFragment.newInstance(), RussiaMapFragment.TAG)
                    true
                }

                else -> false
            }

        }
    }

    private  fun loadFragment(fragment: Fragment, tag: String){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.mainContainer.id,fragment, tag)
        transaction.commit()
    }
}