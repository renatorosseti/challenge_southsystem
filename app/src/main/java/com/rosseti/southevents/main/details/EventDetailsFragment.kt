package com.rosseti.southevents.main.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.rosseti.southevents.R
import kotlinx.android.synthetic.main.fragment_second.*

class EventDetailsFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_second, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)
    textview_second.text = arguments?.getString("data")

    view.findViewById<Button>(R.id.button_second).setOnClickListener {
      findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        return true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}
