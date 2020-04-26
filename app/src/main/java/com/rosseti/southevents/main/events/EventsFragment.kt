package com.rosseti.southevents.main.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rosseti.southevents.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EventsFragment : DaggerFragment() {

  @Inject
  lateinit var viewModel: EventsViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_events, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.eventsResponse.observe(viewLifecycleOwner, Observer {
      when(it) {
        is EventsViewState.ShowLoadingState -> {

        }
        is EventsViewState.ShowContentFeed -> {

        }
        is EventsViewState.ShowRequestError -> {
          Snackbar.make(view, it.message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        }
      }
    })
    viewModel.fetchEvents()

    view.findViewById<Button>(R.id.button_first).setOnClickListener {
      var bundle = bundleOf("data" to "Welcome to SouthEvents app.")
      findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }
  }
}
