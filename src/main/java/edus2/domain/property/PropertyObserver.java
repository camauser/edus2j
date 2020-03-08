package edus2.domain.property;

public interface PropertyObserver<T> {
    void valueChanged(T newValue);
}
