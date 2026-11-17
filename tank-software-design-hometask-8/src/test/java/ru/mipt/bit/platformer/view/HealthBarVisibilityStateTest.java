package ru.mipt.bit.platformer.view;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.ToggleHealthBarsCommand;

import static org.junit.jupiter.api.Assertions.*;

class HealthBarVisibilityStateTest {
    @Test
    void allConsumersObserveSameVisibility() {
        HealthBarVisibilityState state = new HealthBarVisibilityState();
        ToggleHealthBarsCommand command = new ToggleHealthBarsCommand(state);
        assertFalse(state.isVisible());
        command.execute();
        assertTrue(state.isVisible());
        HealthBarVisibilityState newViewState = state;
        assertTrue(newViewState.isVisible());
        command.execute();
        assertFalse(newViewState.isVisible());
    }
}
