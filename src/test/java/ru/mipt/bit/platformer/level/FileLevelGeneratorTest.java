package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tree;

import java.io.StringReader;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FileLevelGeneratorTest {

    private static Level load(String text) {
        return new FileLevelGenerator(() -> new StringReader(text), 0.4f).generate();
    }

    @Test
    void parsesExampleFromTask() {
        Level level = load(
                "___T__T___\n" +
                        "__TT__TTTT\n" +
                        "_________T\n" +
                        "TTTT__T__T\n" +
                        "_____X____\n" +
                        "__________\n");

        assertEquals(10, level.getWidth());
        assertEquals(6, level.getHeight());
        assertEquals(15, level.getTrees().size());
        // X во второй строке снизу → y = 1
        assertEquals(new GridPoint2(5, 1), level.getPlayerTank().getCoordinates());
    }

    @Test
    void firstLineIsTopRow() {
        Level level = load("T__\n___\nX__\n");
        Set<GridPoint2> trees = level.getTrees().stream().map(Tree::getCoordinates).collect(Collectors.toSet());

        assertEquals(Set.of(new GridPoint2(0, 2)), trees);
        assertEquals(new GridPoint2(0, 0), level.getPlayerTank().getCoordinates());
        assertFalse(level.isFree(new GridPoint2(0, 2)));
        assertTrue(level.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void ignoresEmptyLinesAndWindowsLineEndings() {
        Level level = load("_T\r\n\r\nX_\r\n\n");
        assertEquals(2, level.getHeight());
        assertEquals(1, level.getTrees().size());
    }

    @Test
    void rejectsLevelWithoutPlayer() {
        assertThrows(IllegalArgumentException.class, () -> load("__T\n___\n"));
    }

    @Test
    void rejectsTwoPlayers() {
        assertThrows(IllegalArgumentException.class, () -> load("X_\n_X\n"));
    }

    @Test
    void rejectsLinesOfDifferentLength() {
        assertThrows(IllegalArgumentException.class, () -> load("X__\n__\n"));
    }

    @Test
    void rejectsUnknownSymbol() {
        assertThrows(IllegalArgumentException.class, () -> load("X_#\n"));
    }

    @Test
    void rejectsEmptyFile() {
        assertThrows(IllegalArgumentException.class, () -> load(""));
    }
}