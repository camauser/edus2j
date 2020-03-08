package edus2.domain.property;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public abstract class WriteableObservablePropertyTest {

    public abstract WriteableObservableProperty<Integer> createProperty(int initialValue);

    @Test
    public void get_shouldReturnInitialValue() {
        // Arrange
        WriteableObservableProperty<Integer> property = createProperty(1);

        // Act
        Integer actual = property.get();

        // Assert
        assertEquals(1, actual.intValue());
    }

    @Test
    public void get_shouldReturnValue_whenSetHasBeenUsed() {
        // Arrange
        WriteableObservableProperty<Integer> property = createProperty(1);
        property.set(5);

        // Act
        Integer actual = property.get();

        // Assert
        assertEquals(5, actual.intValue());
    }

    @Test
    public void registerListener_shouldRegisterListener() {
        // Arrange
        SimpleListener listener = new SimpleListener();
        WriteableObservableProperty<Integer> property = createProperty(1);

        // Act
        property.registerListener(listener);

        // Assert
        property.set(5);
        assertEquals(1, listener.timesCalled);
    }

    @Test
    public void registerListener_shouldRegisterListenerMultipleTimes_whenRegisterCalledMultipleTimes() {
        // Arrange
        SimpleListener listener = new SimpleListener();
        WriteableObservableProperty<Integer> property = createProperty(1);

        // Act
        property.registerListener(listener);

        // Assert
        property.set(5);
        property.set(5);
        assertEquals(2, listener.timesCalled);
    }

    @Test
    public void registerListener_shouldWorkWithMultipleDistinctListeners() {
        // Arrange
        SimpleListener listener = new SimpleListener();
        SimpleListener secondListener = new SimpleListener();
        WriteableObservableProperty<Integer> property = createProperty(1);

        // Act
        property.registerListener(listener);
        property.registerListener(secondListener);

        // Assert
        property.set(5);
        assertEquals(1, listener.timesCalled);
        assertEquals(1, secondListener.timesCalled);
    }

    @Test
    public void removeListener_shouldRemoveListener() {
        // Arrange
        SimpleListener listener = new SimpleListener();
        SimpleListener secondListener = new SimpleListener();
        WriteableObservableProperty<Integer> property = createProperty(1);
        property.registerListener(listener);
        property.registerListener(secondListener);

        // Act
        property.removeListener(secondListener);

        // Assert
        property.set(5);
        assertEquals(1, listener.timesCalled);
        assertEquals(0, secondListener.timesCalled);
    }

    private static class SimpleListener implements PropertyObserver<Integer> {

        private int timesCalled = 0;

        public void valueChanged(Integer newValue) {
            timesCalled++;
        }
    }

}