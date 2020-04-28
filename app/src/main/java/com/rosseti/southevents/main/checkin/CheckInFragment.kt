package com.rosseti.southevents.main.checkin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rosseti.southevents.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_check_in.*

class CheckInFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        submitCheckIn.setOnClickListener {
            if (nameEditText.text.isNotEmpty() && emailEditText.text.isNotEmpty()) {
                var bundle: Bundle = bundleOf("checkIn" to true,
                    "name" to nameEditText.text.toString(),
                    "email" to emailEditText.text.toString())
                findNavController().navigate(R.id.action_CheckInFragment_to_DetailsFragment, bundle)
            } else {
                var snackbar = Snackbar.make(
                    view,
                    getString(R.string.error_check_in),
                    Snackbar.LENGTH_LONG)
                snackbar.setAction("Action", null).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                var bundle: Bundle = bundleOf("checkIn" to false)
                findNavController().navigate(R.id.action_CheckInFragment_to_DetailsFragment, bundle)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}