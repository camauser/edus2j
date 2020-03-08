package edus2.domain.property;

public interface WriteableObservableProperty<T> extends ReadableObserableProperty<T> {
    void set(T value);
}
