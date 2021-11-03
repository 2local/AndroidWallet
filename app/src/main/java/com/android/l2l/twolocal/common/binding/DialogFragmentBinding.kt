package com.android.l2l.twolocal.common.binding

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by Yoga C. Pranata on 5/6/21.
 * Android Engineer
 *
 * How to use:
 * - Call it inside your DialogFragment
 * private val binding: DialogSampleBinding by viewBinding()
 * and access it normally like you use local variable.
 * https://github.com/yogacp/android-viewbinding
 */
//inline fun <reified T : ViewBinding> DialogFragment.viewBinding() = DialogFragmentViewBindingDelegate(T::class.java)
//
//class DialogFragmentViewBindingDelegate<T : ViewBinding>(private val bindingClass: Class<T>) :
//    ReadOnlyProperty<DialogFragment, T> {
//    /**
//     * initiate variable for binding view
//     */
//    private var binding: T? = null
//
//    @Suppress("UNCHECKED_CAST")
//    override fun getValue(thisRef: DialogFragment, property: KProperty<*>): T {
//        binding?.let { return it }
//
//        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
//        val invokeLayout = inflateMethod.invoke(null, LayoutInflater.from(thisRef.context)) as T
//
//        return invokeLayout.also { this.binding = it }
//    }
//}

class DialogFragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: DialogFragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<DialogFragment, T> {
    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver =
                Observer<LifecycleOwner?> {
                    val viewLifecycleOwner = it ?: return@Observer

                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            binding = null
                        }
                    })
                }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerLiveDataObserver)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerLiveDataObserver)
            }
        })
    }

    override fun getValue(thisRef: DialogFragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
    }
}

fun <T : ViewBinding> DialogFragment.viewBinding(viewBindingFactory: (View) -> T) =
    DialogFragmentViewBindingDelegate(this, viewBindingFactory)

//private val binding by viewBinding(MainActivityBinding::binding)