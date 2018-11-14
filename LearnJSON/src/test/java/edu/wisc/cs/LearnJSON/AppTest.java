package edu.wisc.cs.LearnJSON;

import org.junit.Test;

import automata.sfta.Tree;

import com.google.gson.JsonParseException;
import com.google.gson.JsonStreamParser;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.FileNotFoundException;

public class AppTest 
{

	private final String schema1 = "/Users/harrison/symbaut/LearnJSON/src/test/java/edu/wisc/cs/LearnJSON/s1.json";
	private final String json1 = "/Users/harrison/symbaut/LearnJSON/src/test/java/edu/wisc/cs/LearnJSON/j1.json";

	@Test
    public void readerConstructor() throws FileNotFoundException, LearnJsonException, JsonParseException
    {
		SchemaObject obj = SchemaObject.read(new FileReader(schema1));
		System.out.println(obj);
	}

	@Test
	public void readJson() throws FileNotFoundException, LearnJsonException, JsonParseException
	{
		JsonStreamParser parser = new JsonStreamParser(new FileReader(json1));	
		JsonElement element;
		while(parser.hasNext()) {
			element = parser.next();
			Tree<JsonLetter> tree = JsonLetter.treeFromJson(element);
			tree.debugPrint(0);	
		}
	}

}
