package com.alcntml.codecase

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.alcntml.codecase.util.GithubTestRunner].
 */
class TestApp : Application()
