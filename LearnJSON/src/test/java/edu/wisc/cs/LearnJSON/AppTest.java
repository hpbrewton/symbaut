package edu.wisc.cs.LearnJSON;

import org.junit.Test;

import com.google.gson.JsonParseException;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class AppTest 
{

	private final String schema1 = "/Users/harrison/symbaut/LearnJSON/src/test/java/edu/wisc/cs/LearnJSON/s1.json";

	@Test
    public void readerConstructor() throws FileNotFoundException, LearnJsonException, JsonParseException
    {
		SchemaObject obj = SchemaObject.read(new FileReader(schema1));
		System.out.println(obj);
	}

}
