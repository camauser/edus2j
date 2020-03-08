package edus2.domain.property;

public interface ReadableObserableProperty<T> {

    T get();

    void registerListener(PropertyObserver<T> listener);

    void removeListener(PropertyObserver<T> listener);
}
