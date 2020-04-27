package com.rosseti.southevents.main.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rosseti.southevents.R
import com.rosseti.southevents.dialog.ProgressDialog
import com.rosseti.southevents.main.model.Event
import com.rosseti.southevents.main.model.NoNetworkException
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_events.*
import javax.inject.Inject

class EventsFragment : DaggerFragment() {

  @Inject
  lateinit var progressDialog: ProgressDialog

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
    setLayoutManager()
    observeViewModel(view)
    viewModel.fetchEvents()
  }

  private fun observeViewModel(view: View) {
    var snackbar: Snackbar = Snackbar.make(view, R.string.error_request, Snackbar.LENGTH_LONG)
    viewModel.response.observe(viewLifecycleOwner, Observer {
      when (it) {
        is EventsViewState.ShowLoadingState -> {
          progressDialog.show(requireContext())
        }
        is EventsViewState.ShowContentFeed -> {
          snackbar.dismiss()
          progressDialog.hide()
          updateAdapter(it.events)
        }
        is EventsViewState.ShowRequestError -> {
          progressDialog.hide()
          snackbar = Snackbar.make(
            view,
            it.message,
            if (it.networkException is NoNetworkException) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_INDEFINITE)
          snackbar.setAction("Action", null).show()
        }
      }
      })
  }

  private fun setLayoutManager() {
    recyclerView.layoutManager = LinearLayoutManager(context)
  }

  private fun updateAdapter(events: List<Event>) {
    val mainAdapter = EventsAdapter(events)
    recyclerView.adapter = mainAdapter
    mainAdapter?.onItemClick = { it -> run {
      var bundle = bundleOf(
        "title" to it.title,
        "description" to it.description,
        "image" to it.image,
        "price" to it.price,
        "latitude" to it.latitude,
        "longitude" to it.longitude,
        "date" to it.date
      )
      findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }
    }
  }
}
