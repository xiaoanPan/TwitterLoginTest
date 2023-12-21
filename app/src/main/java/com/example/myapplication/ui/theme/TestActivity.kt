package com.example.myapplication.ui.theme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.api.AGConnectApi
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.TwitterAuthParam
import com.huawei.agconnect.auth.TwitterAuthProvider
import com.mildom.android.R
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class TestActivity : AppCompatActivity() {
    lateinit var loginButton: TwitterLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        huaweiAuth()
    }

    //统一登录接入
    private fun huaweiAuth() {
        val agConnectOptionsBuilder = AGConnectOptionsBuilder()
        AGConnectInstance.initialize(this, agConnectOptionsBuilder)
        AGConnectApi.getInstance().options.setOption("/twitter/client_id", "RTFuWXE5RGFiMXB2UTlvUmtzZmY6MTpjaQ")
        AGConnectApi.getInstance().options.setOption("/twitter/redirect_url", "twittersdk://mildom.com/test")

        val config = TwitterAuthConfig("x8QrFZfAxvs88Roa8GcSbXlVg", "rwRZXwlPNWRtraSMsBcAvq20WhrDhX1gcTvkkKoHZUm7hnl7Xo")
        val config2 = TwitterConfig.Builder(this).twitterAuthConfig(config).build()
        Twitter.initialize(config2)

        findViewById<View>(R.id.test_view).setOnClickListener {

            AGConnectAuth.getInstance().signIn(this@TestActivity, AGConnectAuthCredential.Twitter_Provider)
                .addOnSuccessListener {
                    Log.e("xxx", "${it.user.displayName}, ${it.user.providerInfo}")
                    it.user.getToken(true).addOnSuccessListener {
                        Log.e("xxx", "token=${it.token}")
                    }.addOnFailureListener {
                        Log.e("xxx", "getToken fail")
                    }
                }.addOnFailureListener {
                    Log.e("xxx", "sign in fail")
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginButton.onActivityResult(requestCode, resultCode, data)
    }
}