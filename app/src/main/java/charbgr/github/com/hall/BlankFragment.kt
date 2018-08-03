package charbgr.github.com.hall

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import charbgr.github.com.hall.fragment_hall.BaseFragment

class BlankFragment : BaseFragment() {

  var count: Int = 0
  var title: String = ""

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
      inflater.inflate(R.layout.fragment_blank, container, false)

  @SuppressLint("SetTextI18n")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<TextView>(R.id.text).apply {
      text = screenTag()
      setOnClickListener {
        hall.push(newInstance(title, count + 1))
      }
    }
  }


  override fun screenTag(): String = "$title : $count"
  override fun handleBack(): Boolean = false

  override fun toString(): String {
    return "BlankFragment(count=$count, title='$title')"
  }

  companion object {
    @JvmStatic
    fun newInstance(title: String, count: Int) = BlankFragment().apply {
      this.title = title
      this.count = count
    }
  }
}
