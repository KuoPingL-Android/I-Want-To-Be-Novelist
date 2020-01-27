package studio.saladjam.iwanttobenovelist.repository

import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import studio.saladjam.iwanttobenovelist.repository.dataclass.User

/**
 * This is a combination of Local and Remote Data Sources
 * */

class DataSource(private val localDataSource: Repository,
                 private val remoteDataSource: Repository): Repository {
    override suspend fun loginUser(user: User): Result<Boolean> {
        return remoteDataSource.loginUser(user)
    }

    override suspend fun getCategory(): Result<List<String>> {
        return remoteDataSource.getCategory()
    }

    override suspend fun loginViaFacebook(callbackManager: CallbackManager): Result<Boolean> {
        return remoteDataSource.loginViaFacebook(callbackManager)
    }

    override fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>): Result<Boolean> {
        return remoteDataSource.handleGoogleTask(completedTask)
    }

    override suspend fun getUser(token: String): Result<User> {
        return remoteDataSource.getUser(token)
    }

}