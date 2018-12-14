package li.sau.projectwork.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_seppo.*
import li.sau.projectwork.R

class SeppoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seppo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation = AnimationUtils.loadAnimation(context, R.anim.seppo)
        animation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                findNavController().navigateUp()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        seppoSorsa.startAnimation(animation)
        seppoSorsa.visibility = View.VISIBLE
    }

}