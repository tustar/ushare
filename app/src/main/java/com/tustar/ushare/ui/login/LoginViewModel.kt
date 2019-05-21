package com.tustar.ushare.ui.login

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.tustar.ushare.Event
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication.Companion.context
import com.tustar.ushare.ViewModelFactory
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.ui.HomeActivity
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger
import com.tustar.ushare.util.Preference
import com.uber.autodispose.autoDisposable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class LoginViewModel(private val repo: UserRepository) : BaseViewModel() {

    private val _captchaGetEnable = MutableLiveData<Boolean>()
    val captchaGetEnable: LiveData<Boolean>
        get() = _captchaGetEnable

    private val _captchaGetText = MutableLiveData<String>()
    val captchaGetText: LiveData<String>
        get() = _captchaGetText

    private val _submitEnable = MutableLiveData<Boolean>()
    val submitEnable: LiveData<Boolean>
        get() = _submitEnable

    private val _toMainEvent = MutableLiveData<Event<Unit>>()
    val toMainEvent: LiveData<Event<Unit>>
        get() = _toMainEvent


    fun getCaptcha(mobile: String?) {
        _captchaGetEnable.value = false
        repo.captcha(mobile!!)
                .autoDisposable(this)
                .subscribe({
                    it.execute(
                            ok = { captcha->
                                context.toast(R.string.login_captcha_get_success)
                                showCaptcha(captcha.code)
                                startCaptchaTimer()
                            },
                            failure = { message ->
                                Message.handleFailure(message) {
                                    context.toast(R.string.login_captcha_get_err)
                                }
                                _captchaGetEnable.value = true
                            },
                            other = {
                                _captchaGetEnable.value = true
                            })
                }) {
                    _captchaGetEnable.value = true
                    StatusCode.handleException(it) {
                        R.string.login_captcha_get_err
                    }
                }
    }

    fun login(mobile: String, captcha: String) {
        _submitEnable.value = false
        repo.login(mobile!!, captcha!!)
                .doOnTerminate {
                    _submitEnable.value = true
                }
                .autoDisposable(this)
                .subscribe({
                    it.execute(
                            ok = { user ->
                                var token: String by Preference(context,
                                        CommonDefine.HEAD_ACCESS_TOKEN, "")
                                token = user.token
                                var mobile: String by Preference(context,
                                        CommonDefine.PREF_KEY_USER_MOBILE, "")
                                mobile = user.mobile
                                var nick: String by Preference(context,
                                        CommonDefine.PREF_KEY_USER_NICK, "")
                                nick = user.nick
                                _toMainEvent.value = Event(Unit)
                            },
                            failure = { message ->
                                Message.handleFailure(message) {
                                    context.toast(R.string.login_submit_err)
                                }
                            })
                }) {
                    StatusCode.handleException(it) {
                        R.string.login_submit_err
                    }
                }
    }

    @SuppressLint("CheckResult")
    private fun startCaptchaTimer() {
        Logger.d()
        val count = CommonDefine.CODE_TIMER
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .take(count)
                .map {
                    count - it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _captchaGetEnable.value = false
                }
                .doOnTerminate {
                    _captchaGetEnable.value = true
                }
                .autoDisposable(this)
                .subscribe(
                        { timer ->
                            _captchaGetText.value = context.getString(R.string.login_captcha_timer,
                                    timer)
                        },
                        {
                            Logger.d()
                            it.printStackTrace()
                        },
                        {
                            _captchaGetText.value = context.getString(R.string.login_captcha_get)
                        })

    }

    private fun showCaptcha(code: String) {
        saveCodeToClipboard(code)
        val intent = Intent(context, HomeActivity::class.java).apply {
            putExtra(CommonDefine.EXTRA_VCODE, code)
            addFlags(FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(context, 1, intent,
                FLAG_CANCEL_CURRENT)
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
            setContentIntent(pendingIntent)
            NotificationCompat.PRIORITY_DEFAULT
//            setFullScreenIntent(pendingIntent, true)
            setAutoCancel(true)
            setWhen(System.currentTimeMillis())
        }

        val notification = builder.build()
        val nm = NotificationManagerCompat.from(context)
        nm.notify((System.currentTimeMillis() / 1000L).toInt(), notification)
    }

    private fun saveCodeToClipboard(code: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.primaryClip = ClipData.newPlainText(CommonDefine.EXTRA_VCODE, code)
    }

    companion object {

        fun get(fragment: Fragment): LoginViewModel {
            val application = checkApplication(checkActivity(fragment))
            return ViewModelProviders.of(fragment, ViewModelFactory.getInstance(application))
                    .get(LoginViewModel::class.java)
        }
    }
}