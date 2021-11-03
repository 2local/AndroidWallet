package com.android.l2l.twolocal.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class LiveDataCombineUtil {
    private LiveDataCombineUtil() {
    }

    public interface Combiner2<T1, T2, R> {
        @Nullable
        R combine(@Nullable T1 t1, @Nullable T2 t2);
    }

    @NonNull
    public static <T1, T2, R> LiveData<R> combine(@NonNull LiveData<T1> f1, @NonNull final LiveData<T2> f2, @NonNull final Combiner2<T1, T2, R> combiner2) {
        if (f1 == null) {
            throw new NullPointerException("f1 should not be null");
        }
        if (f2 == null) {
            throw new NullPointerException("f2 should not be null");
        }
        if (combiner2 == null) {
            throw new NullPointerException("combiner2 should not be null");
        }

        final MediatorLiveData<R> mediator = new MediatorLiveData<>();

        mediator.setValue(combiner2.combine(f1.getValue(), f2.getValue()));

        mediator.addSource(f1, new Observer<T1>() {
            @Override
            public void onChanged(T1 t1) {
                final T2 t2 = f2.getValue();
                mediator.setValue(combiner2.combine(t1, t2));
            }
        });

        mediator.addSource(f2, new Observer<T2>() {
            @Override
            public void onChanged(T2 t2) {
                final T1 t1 = f1.getValue();
                mediator.setValue(combiner2.combine(t1, t2));
            }
        });

        return mediator;
    }

    public interface Combiner3<T1, T2, T3, R> {
        @Nullable
        R combine(@Nullable T1 t1, @Nullable T2 t2, @Nullable T3 t3);
    }

    @NonNull
    public static <T1, T2, T3, R> LiveData<R> combine(@NonNull final LiveData<T1> f1, @NonNull final LiveData<T2> f2, @NonNull final LiveData<T3> f3, @NonNull final Combiner3<T1, T2, T3, R> combiner3) {
        if (f1 == null) {
            throw new NullPointerException("f1 should not be null");
        }
        if (f2 == null) {
            throw new NullPointerException("f2 should not be null");
        }
        if (f3 == null) {
            throw new NullPointerException("f3 should not be null");
        }
        if (combiner3 == null) {
            throw new NullPointerException("combiner3 should not be null");
        }

        final MediatorLiveData<R> mediator = new MediatorLiveData<>();

        mediator.setValue(combiner3.combine(f1.getValue(), f2.getValue(), f3.getValue()));

        mediator.addSource(f1, new Observer<T1>() {
            @Override
            public void onChanged(T1 t1) {
                final T2 t2 = f2.getValue();
                final T3 t3 = f3.getValue();
                mediator.setValue(combiner3.combine(t1, t2, t3));
            }
        });

        mediator.addSource(f2, new Observer<T2>() {
            @Override
            public void onChanged(T2 t2) {
                final T1 t1 = f1.getValue();
                final T3 t3 = f3.getValue();
                mediator.setValue(combiner3.combine(t1, t2, t3));
            }
        });

        mediator.addSource(f3, new Observer<T3>() {
            @Override
            public void onChanged(T3 t3) {
                final T1 t1 = f1.getValue();
                final T2 t2 = f2.getValue();
                mediator.setValue(combiner3.combine(t1, t2, t3));
            }
        });

        return mediator;
    }

    public interface Combiner4<T1, T2, T3, T4, R> {
        @Nullable
        R combine(@Nullable T1 t1, @Nullable T2 t2, @Nullable T3 t3, @Nullable T4 t4);
    }

    @NonNull
    public static <T1, T2, T3, T4, R> LiveData<R> combine(@NonNull final LiveData<T1> f1, @NonNull final LiveData<T2> f2, @NonNull final LiveData<T3> f3, @NonNull final LiveData<T4> f4, @NonNull final Combiner4<T1, T2, T3, T4, R> combiner4) {
        if (f1 == null) {
            throw new NullPointerException("f1 should not be null");
        }
        if (f2 == null) {
            throw new NullPointerException("f2 should not be null");
        }
        if (f3 == null) {
            throw new NullPointerException("f3 should not be null");
        }
        if (f4 == null) {
            throw new NullPointerException("f4 should not be null");
        }
        if (combiner4 == null) {
            throw new NullPointerException("combiner4 should not be null");
        }

        final MediatorLiveData<R> mediator = new MediatorLiveData<>();

        mediator.setValue(combiner4.combine(f1.getValue(), f2.getValue(), f3.getValue(), f4.getValue()));

        mediator.addSource(f1, new Observer<T1>() {
            @Override
            public void onChanged(T1 t1) {
                final T2 t2 = f2.getValue();
                final T3 t3 = f3.getValue();
                final T4 t4 = f4.getValue();
                mediator.setValue(combiner4.combine(t1, t2, t3, t4));
            }
        });

        mediator.addSource(f2, new Observer<T2>() {
            @Override
            public void onChanged(T2 t2) {
                final T1 t1 = f1.getValue();
                final T3 t3 = f3.getValue();
                final T4 t4 = f4.getValue();
                mediator.setValue(combiner4.combine(t1, t2, t3, t4));
            }
        });

        mediator.addSource(f3, new Observer<T3>() {
            @Override
            public void onChanged(T3 t3) {
                final T1 t1 = f1.getValue();
                final T2 t2 = f2.getValue();
                final T4 t4 = f4.getValue();
                mediator.setValue(combiner4.combine(t1, t2, t3, t4));
            }
        });

        mediator.addSource(f4, new Observer<T4>() {
            @Override
            public void onChanged(T4 t4) {
                final T1 t1 = f1.getValue();
                final T2 t2 = f2.getValue();
                final T3 t3 = f3.getValue();
                mediator.setValue(combiner4.combine(t1, t2, t3, t4));
            }
        });

        return mediator;
    }
}
