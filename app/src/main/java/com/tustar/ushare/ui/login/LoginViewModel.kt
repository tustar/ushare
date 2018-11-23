package com.tustar.ushare.ui.login

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.view.View
import com.tustar.ushare.R
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.Injection
import com.tustar.ushare.data.entry.Message
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.exception.ExceptionHandler
import com.tustar.ushare.data.exception.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.util.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subscribers.DisposableSubscriber
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val repo: UserRepository
    private val context: Context

    init {
        context = application
        repo = Injection.provideUserRepository(application)
    }

    val mobile = ObservableField<String>()
    val mobileClear = ObservableInt(View.INVISIBLE)
    val captcha = ObservableField<String>()
    val captchaGetEnable = ObservableBoolean(true)
    val captchaGetText = ObservableField<String>()
    val submitEnable = ObservableBoolean(true)
    val toMainCommand = SingleLiveEvent<Void>()
    var onClickSendListener = object : NoFastClickListener() {
        override fun onNoFastClick(v: View) {
            send(mobile.get())
        }
    }
    var onClickLoginListener = object : NoFastClickListener() {
        override fun onNoFastClick(v: View) {
            login(mobile.get(), captcha.get())
        }
    }

    fun clearMobileText(v: View) {
        mobile.set("")
    }

    fun onMobileTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val visible = if (s == null || s.isEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        mobileClear.set(visible)
    }

    private fun send(mobile: String?) {
        if (!MobileUtils.isMobileOk(mobile)) {
            context.toast(R.string.login_mobile_err)
            return
        }

        addSubscription(disposable = repo.captcha(mobile!!)
                .doOnSubscribe {
                    captchaGetEnable.set(false)
                }
                .doOnComplete {
                    captchaGetEnable.set(true)
                }
                .subscribe({
                    when (it.code) {
                        Response.OK -> {
                            context.toast(R.string.login_captcha_get_success)
                            showCaptcha(it.data.code)
                            startCaptchaTimer()
                        }
                        Response.FAILURE -> {
                            when (it.message) {
                                Message.Unauthorized -> Logger.d("Sign Error")
                                else -> {
                                }
                            }
                        }
                        else -> {

                        }
                    }
                }) {
                    val code = ExceptionHandler.handleException(it)
                    when (code) {
                        StatusCode.SOCKET_TIMEOUT_ERROR -> context.toast(R.string.socket_timeout_error)
                        StatusCode.CONNECT_ERROR -> context.toast(R.string.connect_error)
                        StatusCode.UNKNOWN_HOST_ERROR -> context.toast(R.string.unkown_host_error)
                        StatusCode.SERVER_ERROR -> context.toast(R.string.server_err)
                        else -> context.toast(R.string.login_captcha_get_err)
                    }
                })
    }

    fun login(mobile: String?, captcha: String?) {
        Logger.d("mobile = $mobile, captcha = $captcha")
        if (!MobileUtils.isMobileOk(mobile)) {
            context.toast(R.string.login_mobile_err)
            return
        }

        if (!CodeUtils.isCodeOk(captcha)) {
            context.toast(R.string.login_captcha_err)
            return
        }

        addSubscription(disposable = repo.login(mobile!!, captcha!!)
                .doOnSubscribe {
                    submitEnable.set(false)
                }
                .doOnComplete {
                    submitEnable.set(true)
                }
                .subscribe({
                    when (it.code) {
                        Response.OK -> {
                            var token: String by Preference(context,
                                    CommonDefine.HEAD_ACCESS_TOKEN, "")
                            token = it.data.token
                            var mobile: String by Preference(context,
                                    CommonDefine.PREF_KEY_USER_MOBILE, "")
                            mobile = it.data.mobile
                            var nick: String by Preference(context,
                                    CommonDefine.PREF_KEY_USER_NICK, "")
                            nick = it.data.nick
                            toMainCommand.call()
                        }
                        Response.FAILURE -> {
                            when (it.message) {
                                Message.Unauthorized -> Logger.d("Sign Error")
                                else -> context.toast(R.string.login_submit_err)
                            }
                        }
                        else -> {
                            // 更多情况
                        }
                    }
                }) {
                    //
                    val code = ExceptionHandler.handleException(it)
                    when (code) {
                        StatusCode.SOCKET_TIMEOUT_ERROR -> context.toast(R.string.socket_timeout_error)
                        StatusCode.CONNECT_ERROR -> context.toast(R.string.connect_error)
                        StatusCode.UNKNOWN_HOST_ERROR -> context.toast(R.string.unkown_host_error)
                        StatusCode.SERVER_ERROR -> context.toast(R.string.server_err)
                        else -> context.toast(R.string.login_submit_err)
                    }
                })
    }

    private fun startCaptchaTimer() {
        Logger.d()
        val count = CommonDefine.CODE_TIMER
        addSubscription(disposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .take(count)
                .map {
                    count - it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    captchaGetEnable.set(false)
                }
                .doOnTerminate {
                    captchaGetEnable.set(true)
                }
                .subscribeWith(object : DisposableSubscriber<Long>() {

                    override fun onNext(timer: Long?) {
                        val text = context.getString(R.string.login_captcha_timer, timer)
                        captchaGetText.set(text)
                    }

                    override fun onComplete() {
                        Logger.d()
                        val text = context.getString(R.string.login_captcha_get)
                        captchaGetText.set(text)
                    }

                    override fun onError(t: Throwable?) {
                        Logger.d()
                        t?.printStackTrace()
                    }
                }))

    }

    private fun showCaptcha(code: String) {
        captcha.set(code)
//        val intent = Intent(this, HomeActivity::class.java).apply {
//            putExtra(CommonDefine.EXTRA_VCODE, captcha)
//        }
//        val pendingIntent = PendingIntent.getActivity(this, 1, intent, 0)
        val channelName = context.getString(R.string.login_channel_name)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CommonDefine.CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_HIGH)
            channel.description = CommonDefine.CHANNEL_DESCRIPTION
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setShowBadge(false)
            val nm = context.getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context,
                CommonDefine.CHANNEL_ID).apply {
            setContentTitle(context.getString(R.string.login_captcha_notify_title))
            setContentText(context.getString(R.string.login_captcha_notify_text, code))
            setDefaults(NotificationCompat.DEFAULT_SOUND)
            setSmallIcon(R.mipmap.ic_launcher)
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
//            setContentIntent(pendingIntent)
            NotificationCompat.PRIORITY_DEFAULT
//            setFullScreenIntent(pendingIntent, true)
            setAutoCancel(true)
            setWhen(System.currentTimeMillis())
        }

        val notification = builder.build()
        val nm = NotificationManagerCompat.from(context)
        nm.notify((System.currentTimeMillis() / 1000L).toInt(), notification)
    }
}