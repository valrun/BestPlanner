package com.example.bestplanner

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.room.Room
import com.example.bestplanner.calendarList.CalendarDatabase
import com.example.bestplanner.calendarList.TimeItem
import com.example.bestplanner.calendarList.TimeItemAdapter
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.sql.Time

class MyApp : Application() {
    private lateinit var mRetrofit: Retrofit
    private lateinit var mService: ServiceInterface
    var mAdapter: TimeItemAdapter? = null

    private lateinit var mDateBase: CalendarDatabase
    private var mSharedPreference: SharedPreferences? = null
    private var emptyBase: Boolean = true

    var listItems: MutableList<TimeItem> = mutableListOf()
    private var indexOfNewItem = 0

    var textView: TextView? = null
    private var animatorSet: AnimatorSet? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        mRetrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mService = mRetrofit.create(ServiceInterface::class.java)

        mSharedPreference = getSharedPreferences("take", Context.MODE_PRIVATE)
        emptyBase = this.mSharedPreference?.getBoolean(EMPTY, true) ?: true
        mDateBase = Room.databaseBuilder(
            applicationContext,
            CalendarDatabase::class.java, "database"
        ).build()
    }

    fun loadItems(scope: LifecycleCoroutineScope) {
        startLoadAnimation()

        if (emptyBase) {
            try {
                scope.launch {
                    var result = arrayListOf<TimeItem>()

                    try {
                        result = mService.getPosts()
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            e.message?.let { toast(it) }
                        }
                    }

                    listItems.addAll(result)
                    withContext(Dispatchers.Main) {
                        mAdapter?.notifyDataSetChanged()
                    }
                    listItems.forEach {
                        if (indexOfNewItem < it.id + 1) {
                            indexOfNewItem = it.id + 1
                        }
                    }
                    mDateBase.timeItemDao()?.insertItems(listItems)
                    emptyBase = false
                    mSharedPreference?.edit()?.apply {
                        this.putBoolean(EMPTY, emptyBase)
                        apply()
                    }
                }
            } catch (e: CancellationException) {
                toast("something wrong :\n${e.message}")
            } finally {
                stopLoadAnimation()
            }
        } else {
            try {
                scope.launch {
                    val data = mDateBase.timeItemDao()?.getItems() as MutableList<TimeItem>
                    data.forEach { listItems.add(it) }
                    listItems.forEach {
                        if (indexOfNewItem < it.id + 1) {
                            indexOfNewItem = it.id + 1
                        }
                    }
                    withContext(Dispatchers.Main) {
                        mAdapter?.notifyDataSetChanged()
                    }
                }
            } catch (e: CancellationException) {
                toast("something wrong :\n${e.message}")
            } finally {
                stopLoadAnimation()
            }
        }
    }

    fun deleteItem(id: Int, scope : CoroutineScope) {
        try {
            scope.launch {
                try {
                    mService.deletePost(id)
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        e.message?.let { toast(it) }
                    }
                }

                var index = 0
                for (post in listItems) {
                    if (listItems[index].id == id) {
                        break
                    }
                    index++
                }
                mDateBase.timeItemDao()?.deleteByID(index)
                listItems.removeAt(index)
                withContext(Dispatchers.Main) {
                    mAdapter?.notifyDataSetChanged()
                }
            }
        } catch (e: CancellationException) {
            toast("something wrong :\n${e.message}")
        } finally {
            stopLoadAnimation()
        }
    }

    fun addItem(title: String, text: String, scope: LifecycleCoroutineScope) {
//        val timeItem = TimeItem(indexOfNewItem, title, text, 0)
        val timeItem = TimeItem(indexOfNewItem, title, Time(0))
        try {
            scope.launch {
                try {
                    mService.addPost(timeItem)
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        e.message?.let { toast(it) }
                    }
                }

                listItems.add(timeItem)
                mDateBase.timeItemDao()?.insertItem(timeItem)
                withContext(Dispatchers.Main) {
                    mAdapter?.notifyDataSetChanged()
                }
                indexOfNewItem += 1
            }
        } catch (e: CancellationException) {
            toast("something wrong :\n${e.message}")
        } finally {
            stopLoadAnimation()
        }
    }

    fun update(scope: LifecycleCoroutineScope) {
        startLoadAnimation()
        try {
            scope.launch {
                var result = arrayListOf<TimeItem>()

                try {
                    result = mService.getPosts()
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        e.message?.let { toast(it) }
                    }
                    return@launch
                }

                listItems.clear()
                listItems.addAll(result)
                withContext(Dispatchers.Main) {
                    mAdapter?.notifyDataSetChanged()
                }

                mDateBase.timeItemDao()?.deleteAll()
                mDateBase.timeItemDao()?.insertItems(listItems)
                emptyBase = false
                mSharedPreference?.edit()?.apply {
                    this.putBoolean(EMPTY, emptyBase)
                    apply()
                }
            }
        } catch (e: CancellationException) {
            toast("something wrong :\n${e.message}")
        } finally {
            stopLoadAnimation()
        }
    }

    private fun stopLoadAnimation() {
        textView?.visibility = View.INVISIBLE
        animatorSet?.end()
    }

    private fun startLoadAnimation() {
        textView?.visibility = View.VISIBLE
        if (animatorSet == null) {
//            animatorSet = AnimatorInflater.loadAnimator(this, R.animator.load) as AnimatorSet
            animatorSet!!.setTarget(textView)
        }
        animatorSet!!.start()
    }

    private fun toast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        lateinit var instance: MyApp
        const val EMPTY = "true"
    }
}