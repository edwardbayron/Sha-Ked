package org.steamzone.shaked.rx.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import io.reactivex.*

fun <T> Observable<T>.bindToLifecycle(owner: LifecycleOwner): Observable<T> =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindToLifecycle())

fun <T> Observable<T>.bindUntilEvent(owner: LifecycleOwner, event: Lifecycle.Event): Observable<T> =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindUntilEvent(event))

fun <T> Flowable<T>.bindToLifecycle(owner: LifecycleOwner): Flowable<T> =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindToLifecycle())

fun <T> Flowable<T>.bindUntilEvent(owner: LifecycleOwner, event: Lifecycle.Event): Flowable<T> =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindUntilEvent(event))

fun <T> Single<T>.bindToLifecycle(owner: LifecycleOwner): Single<T> =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindToLifecycle())

fun <T> Single<T>.bindUntilEvent(owner: LifecycleOwner, event: Lifecycle.Event): Single<T> =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindUntilEvent(event))

fun <T> Maybe<T>.bindToLifecycle(owner: LifecycleOwner): Maybe<T> =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindToLifecycle())

fun <T> Maybe<T>.bindUntilEvent(owner: LifecycleOwner, event: Lifecycle.Event): Maybe<T> =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindUntilEvent(event))

fun Completable.bindToLifecycle(owner: LifecycleOwner): Completable =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindToLifecycle<Completable>())

fun Completable.bindUntilEvent(owner: LifecycleOwner, event: Lifecycle.Event): Completable =
        this.compose(AndroidLifecycle.createLifecycleProvider(owner).bindUntilEvent<Completable>(event))
