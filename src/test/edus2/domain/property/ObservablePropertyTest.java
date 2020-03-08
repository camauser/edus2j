package edus2.domain.property;

public class ObservablePropertyTest extends WriteableObservablePropertyTest {

    public WriteableObservableProperty<Integer> createProperty(int initialValue) {
        return new ObservableProperty<>(initialValue);
    }
}