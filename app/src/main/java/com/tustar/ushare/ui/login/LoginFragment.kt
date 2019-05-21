package com.tustar.ushare.ui.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tustar.ushare.R
import com.uber.autodispose.android.lifecycle.autoDisposable
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LoginViewModel.get(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {

         RxTextView.textChangeEvents(login_captcha_editText)
                 .autoDisposable(this)
                 .subscribe {
                     val visible = if (it.view().toString().isNullOrEmpty()) {
                         View.INVISIBLE
                     } else {
                         View.VISIBLE
                     }
                     login_mobile_clear.visibility = visible
         }


        login_submit.setOnClickListener {
            Navigation.createNavigateOnClickListener(R.id.activity_main)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
