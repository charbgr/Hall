package charbgr.github.com.hall.fragment_hall

import android.content.Context
import android.support.v4.app.Fragment
import charbgr.github.com.hall.MainActivity
import charbgr.github.com.hall.hall.Screen

abstract class BaseFragment : Fragment(), Screen {

  lateinit var hall: FragmentHall

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is MainActivity) {
      this.hall = context.hall
    }
  }

  override fun handleBack(): Boolean = false
}
