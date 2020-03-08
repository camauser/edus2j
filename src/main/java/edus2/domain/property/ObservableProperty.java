package edus2.domain.property;

import java.util.LinkedList;
import java.util.List;

public class ObservableProperty<T> implements WriteableObservableProperty<T> {

    private T value;
    private List<PropertyObserver<T>> listeners;

    public ObservableProperty(T initialValue) {
        this.value = initialValue;
        this.listeners = new LinkedList<>();
    }

    public void set(T value) {
        this.value = value;
        notifyListeners();
    }

    public T get() {
        return value;
    }

    private void notifyListeners() {
        for (PropertyObserver<T> listener : listeners) {
            listener.valueChanged(value);
        }
    }

    public void registerListener(PropertyObserver<T> listener) {
        this.listeners.add(listener);
    }

    public void removeListener(PropertyObserver<T> listener) {
        this.listeners.remove(listener);
    }
}
