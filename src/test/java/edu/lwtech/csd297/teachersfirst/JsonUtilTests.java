package edu.lwtech.csd297.teachersfirst;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import edu.lwtech.csd297.teachersfirst.pojos.*;

public class JsonUtilTests {

	private class JsonTester implements IJsonnable {
		private String text;
		public JsonTester(String text) { this.setText(text); }

		//public String getText() { return text; }

		public void setText(String text) { this.text = text; }
		@Override
		public String toJson() { return "{'text':'" + text + "'}"; }
	}

	@BeforeEach
	void setUp() {
	}

	@Test
	void testSetRecID() {
		assertEquals("'love':'war'", JsonUtils.queryToJson("love=war"));
		assertEquals("'love':'war'", JsonUtils.queryToJson("?love=war"));
		assertEquals("'love':undefined", JsonUtils.queryToJson("love"));
		assertEquals("", JsonUtils.queryToJson(""));
		assertEquals("", JsonUtils.queryToJson(null));
		assertEquals("", JsonUtils.queryToJson("fdsjklfds=fdsafads=fasdfsda"));
		List<JsonTester> bigArray = new ArrayList<JsonTester>();
		bigArray.add(new JsonTester("peace"));
		bigArray.add(new JsonTester("love"));
		bigArray.add(new JsonTester("joy"));
		String json = JsonUtils.BuildArrays(bigArray);
		assertEquals("[{'text':'peace'},{'text':'love'},{'text':'joy'}]", json);
	}

}
