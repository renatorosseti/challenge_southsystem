package com.rosseti.southevents.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.rosseti.southevents.R
import com.rosseti.southevents.animation.ViewAnimation
import com.rosseti.southevents.dialog.ProgressDialog
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_event_details.*
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.rosseti.southevents.data.Cache.contendDetail
import com.rosseti.southevents.dialog.MessageDialog
import com.rosseti.southevents.main.model.NoNetworkException
import java.text.SimpleDateFormat

class EventDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var viewModel: EventDetailsViewModel

    @Inject
    lateinit var dialog: MessageDialog

    private var isRotate = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleBundleData(arguments)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        observeViewModel(view)
        setFabOptionsListener()
        initializeFabAnimation()
    }

    private fun initializeFabAnimation() {
        ViewAnimation.init(fabCheckIn)
        ViewAnimation.init(fabShare)
        ViewAnimation.init(fabLocation)
    }

    private fun setUiView() {
        Glide.with(this)
            .load(contendDetail.image)
            .apply(RequestOptions().placeholder(R.mipmap.ic_event_foreground))
            .into(detailsImageView)

        titleTextView.text = contendDetail.title
        descriptionTextView.text = contendDetail.description
        priceTextView.text = "R$ ${contendDetail.price}"
        val date = Date(contendDetail.date!!.toLong())
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy' - 'HH:mm:ss")
        dateTextView.text = "${simpleDateFormat.format(date)}"
    }

    private fun setFabOptionsListener() {
        fab.setOnClickListener {
            isRotate = ViewAnimation.rotateFab(it, !isRotate)
            if (isRotate) {
                ViewAnimation.showIn(fabCheckIn)
                ViewAnimation.showIn(fabLocation)
                ViewAnimation.showIn(fabShare)
            } else {
                ViewAnimation.showOut(fabCheckIn)
                ViewAnimation.showOut(fabLocation)
                ViewAnimation.showOut(fabShare)
            }
        }
        fabCheckIn.setOnClickListener {
            findNavController().navigate(R.id.action_DetailsFragment_to_CheckInFragment)
        }
        fabLocation.setOnClickListener {
            viewModel.navigateToLocationEvent(context = requireContext(), event = contendDetail)
        }
        fabShare.setOnClickListener {
            viewModel.navigateToSharingEvent(context = requireContext(), event = contendDetail)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_DetailsFragment_to_EventsFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeViewModel(view: View) {
        var snackbar: Snackbar = Snackbar.make(view, R.string.error_request, Snackbar.LENGTH_LONG)
        viewModel.response.observe(viewLifecycleOwner, Observer {
            when (it) {
                is EventDetailsViewState.RefreshEventDetails -> {
                    progressDialog.hide()
                    snackbar.dismiss()
                    setUiView()
                }
                is EventDetailsViewState.ShowLoadingState -> {
                    progressDialog.show(requireContext())
                }
                is EventDetailsViewState.ShowCheckInSucceed -> {
                    dialog.show(context = requireContext(), message = getString(R.string.succeed_check_in))
                }
                is EventDetailsViewState.ShowNetworkError -> {
                    progressDialog.hide()
                    snackbar = Snackbar.make(
                        view,
                        it.message,
                        if (it.networkException is NoNetworkException) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("Action", null).show()
                }
            }
        })
    }
}
