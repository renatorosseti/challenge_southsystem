package com.rosseti.southevents.main.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rosseti.southevents.R
import com.rosseti.southevents.animation.ViewAnimation
import com.rosseti.southevents.main.model.Event
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_event_details.*
import java.util.*

class EventDetailsFragment : DaggerFragment() {

  lateinit var event: Event

  var isRotate = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_event_details, container, false)
  }

  override fun onResume() {
    super.onResume()
    fetchBundle()
    setUiView()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)
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
      .load(event.image)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
      .into(detailsImageView)
    titleTextView.text = event.title
    descriptionTextView.text = event.description
    priceTextView.text = "R$ ${event.price}"

    val date = Date(event.date!!.toLong())
    dateTextView.text = "$date"
  }

  private fun fetchBundle() {
    fun arg(key: String) = arguments?.getString(key)
    fun argDouble(key: String) = arguments?.getDouble(key)
    event = Event(
      title = arg("title"),
      description = arg("description"),
      price = arg("price")?.toDouble(),
      latitude = argDouble("latitude"),
      longitude = argDouble("longitude"),
      image = arg("image"),
      date = argDouble("date")
      )
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
    fabCheckIn.setOnClickListener {  }
    fabLocation.setOnClickListener {
      val gmmIntentUri: Uri = Uri.parse("geo:${event.latitude},${event.longitude}")
      val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
      mapIntent.setPackage("com.google.android.apps.maps")
      if (mapIntent.resolveActivity(activity?.getPackageManager()) != null) {
        startActivity(mapIntent)
      }
    }
    fabShare.setOnClickListener {
      val shareIntent = Intent(Intent.ACTION_SEND)
      val uri: Uri = Uri.parse("${context?.filesDir}/image.png")
      detailsImageView.setImageURI(uri)
      val message = "${event.title}\n\n${event.description}\n\nR$${event.price}\n\n${event.image}"
      shareIntent.type = "text/plain"
      shareIntent.putExtra(Intent.EXTRA_TEXT, message)
      startActivity(Intent.createChooser(shareIntent, "Share event using"))
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
