package charbgr.github.com.hall.conductor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller

object DummyController : Controller() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View = container
}
