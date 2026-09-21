package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.apiS.Test;
import ru.mipt.bit.platformer.model.Direction;
import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {
    @Test
    void testNextCoordinate() {
        GridPoint2 start = new GridPoint2(5, 5);
        assertEquals(new GridPoint2(5, 6), Direction.UP.getNextCoordinate(start));
        assertEquals(new GridPoint2(5, 4), Direction.DOWN.getNextCoordinate(start));
        assertEquals(new GridPoint2(4, 5), Direction.LEFT.getNextCoordinate(start));
        assertEquals(new GridPoint2(6, 5), Direction.RIGHT.getNextCoordinate(start));
    }
}