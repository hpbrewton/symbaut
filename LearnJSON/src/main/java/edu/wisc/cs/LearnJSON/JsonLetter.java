package edu.wisc.cs.LearnJSON;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import automata.sfta.Tree;

public class JsonLetter {
	public enum Control {
		OBJ,
		KVP,
		KEY,
		NUM,
		CHR,
		STR,
		NULL,
		BOOL,
		ARR,
		ARRS,
		END
	}

	Control cont;
	Number num;
	Character chr;
	boolean bl;

	public JsonLetter(Number num) {
		this.num = num;	
		this.cont = Control.NUM;
	}

	public JsonLetter(Character chr) {
		this.chr = chr;
		this.cont = Control.CHR;
	}

	public JsonLetter(boolean bl) {
		this.bl = bl;	
		this.cont = Control.BOOL;
	}

	public JsonLetter(Control cont) throws LearnJsonException {
		switch (cont) {
		case OBJ:
		case KVP:
		case KEY:
		case STR:
		case NULL:
		case ARR:
		case ARRS:
		case END:
			this.cont = cont;
			break;
		default:
			throw new LearnJsonException("Can't make a JsonLetter with just " + cont);		
		}
	}

	public String toString() {
		switch (cont) {
		case OBJ: return ":obj";
		case KVP: return ":kvp";
		case KEY: return ":key";
		case STR: return ":str";
		case NULL: return "null";
		case ARR: return ":arr";
		case ARRS: return ":arr-start";
		case END: return ":end";
		case NUM: return num.toString();	
		case CHR: return chr.toString();	
		case BOOL: return this.bl ? "T" : "F";	
		}
		return ":invalid";
	}

	public static Tree<JsonLetter> treeFromJson(JsonElement elem) throws LearnJsonException {
		if (elem.isJsonNull()) {
			return new Tree(new JsonLetter(Control.NULL), new ArrayList<>());
		} if (elem.isJsonPrimitive()) {
			JsonPrimitive prim = elem.getAsJsonPrimitive(); 
			if (prim.isBoolean()) {
				return new Tree(new JsonLetter(prim.getAsBoolean()), new ArrayList<>());
			} else if (prim.isNumber()) {
				return new Tree(new JsonLetter(prim.getAsNumber()), new ArrayList<>());
			} else if (prim.isString()) {
				Tree tail = new Tree(new JsonLetter(Control.END), new ArrayList<>());
				char chrs [] = prim.getAsString().toCharArray();
				for (int i = chrs.length-1; i >= 0; --i) {
					Tree ch = new Tree(new JsonLetter(new Character(chrs[i])), new ArrayList(Arrays.asList(tail)));
					tail = ch;
				}
				return new Tree(new JsonLetter(Control.STR), new ArrayList(Arrays.asList(tail)));
			}
		} else if (elem.isJsonArray()) {
			JsonArray arr = elem.getAsJsonArray();
			Tree tail = new Tree(new JsonLetter(Control.END), new ArrayList<>());
			
			for (JsonElement arrElem : Lists.reverse(Lists.newArrayList(arr.iterator()))) {
				tail = new Tree(new JsonLetter(Control.ARR), new ArrayList(Arrays.asList(treeFromJson(arrElem), tail)));
			}	
			return tail;
		}
		return null;
	}
}
