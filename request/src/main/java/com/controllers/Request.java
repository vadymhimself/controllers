package com.controllers;

/**
 * Created by Vadym Ovcharenko
 * 21.10.2016.
 */

public abstract class Request<T> {

    public abstract Request<T> onError(Promise.ErrorAction errorAction);

    public abstract Request<T> onCanceled(Promise.Action cancelledAction);

    public abstract Request<T> onFinally(Promise.Action finallyAction);

    public abstract Promise<T> execute(Promise.SuccessAction<T> successAction);

    public abstract Promise<T> execute();

    public abstract T executeSync() throws Throwable;

    static Promise.ErrorAction defaultErrorAction = new Promise.ErrorAction() {
        @Override
        public void call(Throwable e) {
            e.printStackTrace();
        }
    };

    public static void setDefaultErrorAction(Promise.ErrorAction action) {
        defaultErrorAction = action;
    }

    public static Promise.ErrorAction getDefaultErrorAction() {
        return defaultErrorAction;
    }

    public static <S> void registerService(final S s) {
        ServiceContainer.registerService(s);
    }

    public static <S> RequestFactory<S> with(ObservableController controller,
                                             Class<S> service) {
        return new RequestFactoryImpl<>(controller, service);
    }
}
