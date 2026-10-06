package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;
import static org.junit.jupiter.api.Assertions.*;

class TankTest {
    @Test
    void testTankMovesToDestination() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GridPoint2 obstacle = new GridPoint2(5, 5);
        
        tank.tryMove(Direction.UP, obstacle);
        assertTrue(tank.getMovementProgress() < 1f);
        
        tank.update(0.4f); 
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void testTankCannotMoveIntoObstacle() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GridPoint2 obstacle = new GridPoint2(1, 2); 
        
        tank.tryMove(Direction.UP, obstacle);
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }
}