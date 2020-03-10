package studio.saladjam.iwanttobenovelist.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import studio.saladjam.iwanttobenovelist.repository.remote.IWBNRemoteDataSource

object ServiceLocator {
    @Volatile
    var repository: Repository? = null
        @VisibleForTesting set

    fun getRepository(context: Context): Repository {
        // Locked this such that no more than 1 Repository can be created
        synchronized(this) {
            if (repository == null) {
                repository = createRepository(context)
            }
            return repository!!
        }
    }

    private fun createRepository(context: Context): Repository {
        return DataSource(createLocalDataSource(context),
            IWBNRemoteDataSource
        )
    }

    private fun createLocalDataSource(context: Context): Repository {
        return IWBNLocalDataSource(context)
    }
}