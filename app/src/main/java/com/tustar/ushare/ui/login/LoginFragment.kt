package com.tustar.ushare.ui.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tustar.action.RxBus
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.extension.clicks
import com.tustar.ushare.extension.textChanges
import com.tustar.ushare.rxbus.EventCode
import com.tustar.ushare.util.CodeUtils
import com.tustar.ushare.util.MobileUtils
import com.uber.autodispose.android.lifecycle.autoDisposable
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.toast


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

        initObservers()
    }

    private fun initViews() {
        login_mobile_editText.textChanges()
                .map(CharSequence::toString)
                .autoDisposable(this)
                .subscribe {
                    val visible = if (it.isNullOrEmpty()) {
                        View.INVISIBLE
                    } else {
                        View.VISIBLE
                    }
                    login_mobile_clear.visibility = visible
                }

        login_mobile_clear.clicks()
                .autoDisposable(this)
                .subscribe {
                    login_mobile_editText.setText("")
                }

        login_captcha_get.clicks()
                .autoDisposable(this)
                .subscribe {
                    getCaptcha()
                }


        login_submit.clicks()
                .autoDisposable(this)
                .subscribe {
                    login()
                }

        RxBus.get().toObservable(EventCode::class.java).autoDisposable(this)
                .subscribe {
                    login_captcha_editText.setText(it.code)
                }
    }

    private fun initObservers() {
        viewModel.captchaGetEnable.observe(this, Observer {
            login_captcha_get.isEnabled = it
        })
        viewModel.captchaGetText.observe(this, Observer {
            login_captcha_get.text = it
        })
        viewModel.submitEnable.observe(this, Observer {
            login_submit.isEnabled = it
        })
        viewModel.toMainEvent.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                activity?.finish()
            }
        })
    }

    private fun getCaptcha() {
        val mobile = login_mobile_editText.text.toString()
        if (!MobileUtils.isMobileOk(mobile)) {
            UShareApplication.context.toast(R.string.login_mobile_err)
            return
        }

        viewModel.getCaptcha(mobile)
    }

    private fun login() {
        val mobile = login_mobile_editText.text.toString()
        if (!MobileUtils.isMobileOk(mobile)) {
            UShareApplication.context.toast(R.string.login_mobile_err)
            return
        }

        val captcha = login_captcha_editText.text.toString()
        if (!CodeUtils.isCodeOk(captcha)) {
            UShareApplication.context.toast(R.string.login_captcha_err)
            return
        }

        viewModel.login(mobile, captcha)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
