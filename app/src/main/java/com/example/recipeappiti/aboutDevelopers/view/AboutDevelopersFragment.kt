package com.example.recipeappiti.aboutDevelopers.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.recipeappiti.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AboutDevelopersFragment : Fragment() {

    private lateinit var userEmailIbrahim: TextView
    private lateinit var userLinkedInIbrahim: TextView
    private lateinit var userGitHubIbrahim: TextView


    private lateinit var userEmailHossam: TextView
    private lateinit var userLinkedInHossam: TextView
    private lateinit var userGitHubHossam: TextView

    private lateinit var userEmailMohaned: TextView
    private lateinit var userLinkedInMohaned: TextView
    private lateinit var userGitHubMohaned: TextView

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_developers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE

        userEmailIbrahim = view.findViewById(R.id.userEmailIbrahim)
        userLinkedInIbrahim = view.findViewById(R.id.userLinkedInIbrahim)
        userGitHubIbrahim = view.findViewById(R.id.userGitHubIbrahim)

        userEmailIbrahim.setOnClickListener {
            sendEmail("ibrahim.mohamed.ibrahim.t@gmail.com")
        }

        userLinkedInIbrahim.setOnClickListener {
            openLink("https://www.linkedin.com/in/eibrahim67")
        }

        userGitHubIbrahim.setOnClickListener {
            openLink("https://github.com/eIbrahim67")
        }

        userEmailHossam = view.findViewById(R.id.userEmailHossam)
        userLinkedInHossam = view.findViewById(R.id.userLinkedInHossam)
        userGitHubHossam = view.findViewById(R.id.userGitHubHossam)

        userEmailHossam.setOnClickListener {
            sendEmail("hossamwalidgv@gmail.com")
        }
        userLinkedInHossam.setOnClickListener {
            openLink("https://www.linkedin.com/in/gv-hossamwalid")
        }
        userGitHubHossam.setOnClickListener {
            openLink("https://github.com/GreenVenom77")
        }

        userEmailMohaned = view.findViewById(R.id.userEmailMohand)
        userLinkedInMohaned = view.findViewById(R.id.userLinkedInMohand)
        userGitHubMohaned = view.findViewById(R.id.userGitHubMohand)

        userEmailMohaned.setOnClickListener {
            sendEmail("mohandadel2299@gmail.com")
        }
        userLinkedInMohaned.setOnClickListener {
            openLink("https://www.linkedin.com/in/mohand-adel-034013189/")
        }
        userGitHubMohaned.setOnClickListener {
            openLink("https://github.com/mohand3del")
        }

    }

    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        startActivity(intent)
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView.visibility = View.VISIBLE
    }

}