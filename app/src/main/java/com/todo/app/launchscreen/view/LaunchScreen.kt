package com.todo.app.launchscreen.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.todo.app.todo.view.TodoActivity
import kotlinx.android.synthetic.main.activity_launch.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class LaunchScreen : AppCompatActivity(), AnkoLogger {

    /**
     * Parameters for the facebook login to define the return status
     */
    private val email = "email"

    private val callbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.todo.app.R.layout.activity_launch)

        login_button.setReadPermissions(listOf(email))
        login_button.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    startActivity<TodoActivity>()
                }

                override fun onCancel() {
                    toast("Login Cancelled")
                }

                override fun onError(error: FacebookException?) {
                    toast("Login Failed")
                    error(error?.message)
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
