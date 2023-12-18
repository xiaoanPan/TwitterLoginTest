package com.example.myapplication.ui.theme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
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
    lateinit var twitterAuthClient: TwitterAuthClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val config = TwitterAuthConfig("x8QrFZfAxvs88Roa8GcSbXlVg", "rwRZXwlPNWRtraSMsBcAvq20WhrDhX1gcTvkkKoHZUm7hnl7Xo")
        val config2 = TwitterConfig.Builder(this).twitterAuthConfig(config).build()
        Twitter.initialize(config2)
        val client = TwitterAuthClient()
        //RTFuWXE5RGFiMXB2UTlvUmtzZmY6MTpjaQ
        AGConnectApi.getInstance().options.setOption("/twitter/client_id", "RTFuWXE5RGFiMXB2UTlvUmtzZmY6MTpjaQ")
        AGConnectApi.getInstance().options.setOption("/twitter/redirect_url", "twittersdk://mildom.com/test")

        val child = TestChild()
        val viewStub: ViewStub? = findViewById(R.id.test_view_stub)
        child.onCreate(viewStub)

        loginButton = findViewById<TwitterLoginButton>(R.id.buttonTwitterLogin)

            loginButton.callback = object : Callback<TwitterSession>() {
                override fun success(p0: Result<TwitterSession>?) {
                    Log.e("xxx", "token=${p0?.data?.authToken}, secret=${p0?.data?.authToken?.secret}")
                    val credential = TwitterAuthProvider.credentialWithToken(p0?.data?.authToken?.token, p0?.data?.authToken?.secret)
                    AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener {
                        // onSuccess
                        val user = it.user
                        Log.e("xxx", user.toString())
                    }.addOnFailureListener {
                        // onFail
                        Log.e("xxx", it.message ?:"")
                    }
                }

                override fun failure(p0: TwitterException?) {
                    Log.e("xxx", p0?.message?:"")
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginButton.onActivityResult(requestCode, resultCode, data)
    }
}

open class Parent {
    open fun onCreate(view: View) {
        Log.e("xxx", "onCreate")
    }

    open fun onCreate(viewStub: ViewStub) {
        Log.e("xxx", "onCreateViewStub")
    }
}

class Child : Parent() {
    override fun onCreate(viewStub: ViewStub) {
        super.onCreate(viewStub)
    }
}