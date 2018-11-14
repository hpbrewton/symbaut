package edu.wisc.cs.LearnJSON;

import com.google.gson.JsonStreamParser;
import com.google.gson.JsonParseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;

import java.io.Reader;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

// for testing
import java.io.StringReader;

/**
 * Hello world!
 *
 */
public class SchemaObject 
{

	private String description;
	private String type;
	private Map<String, SchemaObject> properties;
	private Collection<String> required;

	public SchemaObject() {
	}

	public SchemaObject(JsonObject object) throws LearnJsonException {
		JsonElement descriptionElem = object.get("description");	
		if (descriptionElem != null &&
			descriptionElem.isJsonPrimitive() && 
			descriptionElem.getAsJsonPrimitive().isString()) {
			this.description = descriptionElem.getAsJsonPrimitive().getAsString();
		} else {
			description = null;
		}

		JsonElement typeElem = object.get("type");	
		if (typeElem != null &&
			typeElem.isJsonPrimitive() && 
			typeElem.getAsJsonPrimitive().isString()) {
			this.type = typeElem.getAsJsonPrimitive().getAsString();
		} else {
			throw new LearnJsonException(typeElem.toString() + " is not a string, and so can not be a type");
		}

		JsonElement propertiesElem = object.get("properties");
		if (!type.equals("object"))	{
			if (propertiesElem != null) {
				throw new LearnJsonException("properties on a none object schema");
			}
			return;
		}
		properties = new HashMap<String, SchemaObject>();
		for (Map.Entry<String, JsonElement> kvp : propertiesElem.getAsJsonObject().entrySet()) {
			if (!kvp.getValue().isJsonObject()) {
				throw new LearnJsonException(kvp.getValue().toString() + " is not an object, and so can not be a property");	
			}
			properties.put(kvp.getKey(), new SchemaObject(kvp.getValue().getAsJsonObject()));
		}

		JsonElement requiredElem = object.get("required");
		required = new HashSet();
		if (requiredElem != null &&
			requiredElem.isJsonArray()) {
			for (JsonElement element : requiredElem.getAsJsonArray()) {
				if (element.isJsonPrimitive() &&
					typeElem.getAsJsonPrimitive().isString()) {
					required.add(element.getAsJsonPrimitive().getAsString());
				} else {
					throw new LearnJsonException(element.toString() + " could not be parsed as a json string in a required array");
				}
			}
		}
	}

	public static SchemaObject read(Reader reader) throws JsonParseException, LearnJsonException {
		JsonStreamParser parser = new JsonStreamParser(reader);
		JsonElement element = parser.next();
		if (!element.isJsonObject()) {
			throw new LearnJsonException(element.toString() + " could not be parsed as a json object");	
		} else {
			return new SchemaObject(element.getAsJsonObject());
		}	
	}

	public String toString() {
		return String.format("{\"description\":%s,\"type\":%s,\"properties\":%s,\"required\":%s}", 
			description,
			type,
			properties != null ? properties.toString() : "none",
			required != null ? required.toString() : "none");
	}
}
