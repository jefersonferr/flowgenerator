 /**
 * FlowGenerator 
 * HashTable que contem todas as palavras reservadas do Java 1.5
 * Utilizada pela classe MyDocument para colorir as palavras reservadas
 * @author Jeferson Ferreira
 * @since  13/10/2006
 * @version 1.0
 */
package org.flowgenerator.controller;
import java.util.Hashtable;
@SuppressWarnings("serial")
public class KeyWords extends Hashtable<Object, Object> {
	public KeyWords() {
		Object dummyObject = new Object();
	    this.put( "abstract", dummyObject );
	    this.put( "boolean", dummyObject);
	    this.put( "break", dummyObject );
	    this.put( "byte", dummyObject );
	    this.put( "case", dummyObject );
	    this.put( "catch", dummyObject );
	    this.put( "char", dummyObject );
	    this.put( "class", dummyObject );
	    this.put( "const", dummyObject ); 
	    this.put( "continue", dummyObject );
	    this.put( "default", dummyObject );
	    this.put( "do", dummyObject );
	    this.put( "double", dummyObject );
	    this.put( "else", dummyObject );
	    this.put( "extends", dummyObject );
	    this.put( "false", dummyObject );
	    this.put( "final", dummyObject );
	    this.put( "finally", dummyObject );
	    this.put( "float", dummyObject );
	    this.put( "for", dummyObject );
	    this.put( "goto", dummyObject );
	    this.put( "if", dummyObject );
	    this.put( "implements", dummyObject );
	    this.put( "import", dummyObject );
	    this.put( "instanceof", dummyObject );
	    this.put( "int", dummyObject );
	    this.put( "interface", dummyObject );
	    this.put( "long", dummyObject );
	    this.put( "native", dummyObject );
	    this.put( "new", dummyObject );
	    this.put( "null", dummyObject );
	    this.put( "package", dummyObject );
	    this.put( "private", dummyObject );
	    this.put( "protected", dummyObject );
	    this.put( "public", dummyObject );
	    this.put( "return", dummyObject );
	    this.put( "short", dummyObject );
	    this.put( "static", dummyObject );
	    this.put( "strictfp", dummyObject );
	    this.put( "super", dummyObject );
	    this.put( "switch", dummyObject );
	    this.put( "synchronized", dummyObject );
	    this.put( "this", dummyObject );
	    this.put( "throw", dummyObject );
	    this.put( "throws", dummyObject );
	    this.put( "transient", dummyObject );
	    this.put( "true", dummyObject );
	    this.put( "try", dummyObject );
	    this.put( "void", dummyObject );
	    this.put( "volatile", dummyObject );
	    this.put( "while", dummyObject );
	}
}
