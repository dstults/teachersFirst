package edu.lwtech.csd297.skeleton;

import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.skeleton.pojos.*;

class SkeletonTests {

    Skeleton fred;
    Skeleton amy;
    Skeleton juan;

    @BeforeEach
    void setUp() {
        fred = new Skeleton("Fred", 66);
        amy = new Skeleton("Amy", 43);
        juan = new Skeleton("Juan", 22);
    }

    @Test
    void testConstructor() {
        Exception ex = null;

        ex = assertThrows(IllegalArgumentException.class,
            () -> { new Skeleton(-666, "Fred", 66); }
        );
        assertTrue(ex.getMessage().contains("recID"));
        ex = assertThrows(IllegalArgumentException.class,
            () -> { new Skeleton(123, null, 66); }
        );
        assertTrue(ex.getMessage().contains("name is null"));
        ex = assertThrows(IllegalArgumentException.class,
            () -> { new Skeleton(123, "", 66); }
        );
        assertTrue(ex.getMessage().contains("name is empty"));
        ex = assertThrows(IllegalArgumentException.class,
            () -> { new Skeleton(123, "Fred", -66); }
        );
        assertTrue(ex.getMessage().contains("negative"));
    }


    @Test
    void testGetId() {
        assertEquals(-1, fred.getRecID());
        assertEquals(-1, amy.getRecID());
        assertEquals(-1, juan.getRecID());
    }

    @Test
    void testSetRecID() {
        Exception ex = null;

        fred.setRecID(123);
        assertEquals(123, fred.getRecID());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { fred.setRecID(-666); }
        );
        assertTrue(ex.getMessage().contains("negative"));

        ex = assertThrows(IllegalArgumentException.class,
            () -> { fred.setRecID(666); }
        );
        assertTrue(ex.getMessage().contains("already been added"));
    }

    @Test
    void testGetName() {
        assertEquals("Fred", fred.getName());
        assertEquals("Amy", amy.getName());
        assertEquals("Juan", juan.getName());
    }

    @Test
    void testGetAge() {
        assertEquals(66, fred.getAge());
    }
    
    @Test
    void testSetAge() {
        Exception ex = null;

        fred.setAge(1);
        assertEquals(1, fred.getAge());

        ex = assertThrows(IllegalArgumentException.class,
            () -> { fred.setAge(-666); }
        );
        assertTrue(ex.getMessage().contains("negative"));
    }
    
    @Test
    void testToString() {
        assertTrue(fred.toString().contains("Fred"));
        assertTrue(fred.toString().contains("66"));
    }

}
