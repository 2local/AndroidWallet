package com.android.l2l.twolocal.common.binding

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Updated by Yoga C. Pranata on 06/05/2021.
 * Android Engineer
 *
 * How to use:
 * - Call it inside your fragment
 * private val binding: FragmentSampleBinding by viewBinding()
 * and access it normally like you use local variable.
 *
 * For the referrence:
 * Thanks to Gabor Varadi for his article
 *
 * ***** https://link.medium.com/TvisGhIS1ab
 */
//
//class FragmentViewBindingDelegate<T : ViewBinding>(
//    bindingClass: Class<T>,
//    private val fragment: Fragment
//) : ReadOnlyProperty<Fragment, T> {
//
//    private var binding: T? = null
//    private val bindMethod = bindingClass.getMethod("bind", View::class.java)
//
//    init {
//        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
//            val viewLifecycleOwnerLiveDataObserver =
//                Observer<LifecycleOwner?> {
//                    val viewLifecycleOwner = it ?: return@Observer
//
//                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
//                        override fun onDestroy(owner: LifecycleOwner) {
//                            binding = null
//                        }
//                    })
//                }
//
//            override fun onCreate(owner: LifecycleOwner) {
//                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerLiveDataObserver)
//            }
//
//            override fun onDestroy(owner: LifecycleOwner) {
//                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerLiveDataObserver)
//            }
//        })
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
//        binding?.let { return it }
//
//        /**
//         * Adding observer to the fragment lifecycle
//         */
////        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
////            override fun onCreate(owner: LifecycleOwner) {
////                fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
////                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
////                        override fun onDestroy(owner: LifecycleOwner) {
////                            /**
////                             * Clear the binding when Fragment lifecycle called the onDestroy
////                             */
////                            binding = null
////                        }
////                    })
////                }
////            }
////        })
//
//        val lifecycle = fragment.viewLifecycleOwner.lifecycle
//        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
//            error("Cannot access view bindings. View lifecycle is ${lifecycle.currentState}!")
//        }
//

//        val invoke = bindMethod.invoke(null, thisRef.requireView()) as T
//
//        return invoke.also { this.binding = it }
//    }
//}

//inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(T::class.java, this)

//https://zhuinden.medium.com/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
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

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
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

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

//private val binding by viewBinding(MainActivityBinding::inflate)

