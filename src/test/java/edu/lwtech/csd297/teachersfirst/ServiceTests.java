package edu.lwtech.csd297.teachersfirst;

import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

class ServiceTests {

	Service basketweaving;
	Service metallurgy;
	Service painting;

	@BeforeEach
	void setUp() {
		basketweaving = new Service("Basketweaving", "Make the best baskets ev4r.", "Darren");
		metallurgy = new Service("Metallurgy", "Make a brass statue.", "Tanya");
		painting = new Service("Painting", "Paint all the things!", "Edmund, Fred");
	}

	@Test
	void testConstructor() {
		Exception ex = null;

		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Service(-666, "Basketweaving", "Make the best baskets ev4r.", "Darren");
		});
		assertTrue(ex.getMessage().contains("recID"));
		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Service(123, null, "Make the best baskets ev4r.", "Darren");
		});
		assertTrue(ex.getMessage().contains("name is null"));
		ex = assertThrows(IllegalArgumentException.class, () -> {
			new Service(123, "", "Make the best baskets ev4r.", "Darren");
		});

		assertTrue(ex.getMessage().contains("name is empty"));
	}

	@Test
	void testGetId() {
		assertEquals(-1, basketweaving.getRecID());
		assertEquals(-1, metallurgy.getRecID());
		assertEquals(-1, painting.getRecID());
	}

	@Test
	void testSetRecID() {
		Exception ex = null;

		basketweaving.setRecID(123);
		assertEquals(123, basketweaving.getRecID());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			basketweaving.setRecID(-666);
		});
		assertTrue(ex.getMessage().contains("negative"));

		ex = assertThrows(IllegalArgumentException.class, () -> {
			basketweaving.setRecID(666);
		});
		assertTrue(ex.getMessage().contains("already been added"));
	}

	@Test
	void testGetName() {
		assertEquals("Basketweaving", basketweaving.getName());
		assertEquals("Metallurgy", metallurgy.getName());
		assertEquals("Painting", painting.getName());
	}

	@Test
	void testGetDescription() {
		assertEquals("Make a brass statue.", metallurgy.getDescription());
	}

	@Test
	void testSetDescription() {
		Exception ex = null;

		basketweaving.setDescription("Craft a golden idol.");
		assertEquals("Craft a golden idol.", basketweaving.getDescription());

		ex = assertThrows(IllegalArgumentException.class, () -> {
			basketweaving.setDescription(null);
		});
		assertTrue(ex.getMessage().contains("null"));
	}

	@Test
	void testToString() {
		assertTrue(painting.toString().contains("Painting"));
		assertTrue(painting.toString().contains("Edmund"));
	}

	@Test
	void testEquals() {
		Service metallurgy2 = new Service("Metallurgy", "Make a brass statue.", "Tanya");
		assertTrue(metallurgy.equals(metallurgy2));
		assertEquals(metallurgy, metallurgy2);
	}

}
