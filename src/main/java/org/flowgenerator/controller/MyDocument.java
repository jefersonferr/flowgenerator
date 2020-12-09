 /**
 * FlowGenerator 
 * Classe responsavel pelo Highlight das palavras reservadas e comentarios
 * @author Jeferson Ferreira
 * @since  15/10/2006
 * @version 1.0
 */
package org.flowgenerator.controller;
import java.awt.Color;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
public class MyDocument extends DefaultStyledDocument {
	private KeyWords keywords;
	private DefaultStyledDocument doc;
	private MutableAttributeSet normal;
	private MutableAttributeSet keyword;
	private MutableAttributeSet comment;
	private MutableAttributeSet quote;
	private UndoManager undo = new UndoManager();
	private boolean highlighting;

	public MyDocument() {
		doc = this;
		putProperty( DefaultEditorKit.EndOfLineStringProperty, "\n" );
		// texto normal
		normal = new SimpleAttributeSet();
		StyleConstants.setForeground(normal, Color.black);
		// comentarios
		comment = new SimpleAttributeSet();
		StyleConstants.setForeground(comment, new Color(46,139,87));
		StyleConstants.setItalic(comment, true);
		// palavras reservadas
		keyword = new SimpleAttributeSet();
		StyleConstants.setForeground(keyword, Color.blue);
		//StyleConstants.setBold(keyword, true);
		// strings e caracteres
		quote = new SimpleAttributeSet();
		StyleConstants.setForeground(quote, Color.red);
		// carrega as palavras reservadas
		keywords = new KeyWords();
		this.addUndoableEditListener(new UndoableEditListener() {
				public void undoableEditHappened(UndoableEditEvent evt) {
					if (isHighlighting()) {
						return;
					}
					undo.addEdit(evt.getEdit());
				}
			});
		setHighlighting(false);
	}
	
	public void undoAction() {
		try {
			if (canUndoAction()) {
				undo.undo();
			}
		}
		catch (CannotRedoException cre) {}
	}
	
	public void redoAction()	{
		try {
			if (canRedoAction())	{
				undo.redo();
				try {
					processChangedLines(0,this.getLength());
				} catch (BadLocationException e) {

				}
			}
		}
		catch (CannotRedoException cre) {}
	}
	public boolean canUndoAction() {
		return undo.canUndo();
	}
	public boolean canRedoAction() {
		return undo.canRedo();
	}
	
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		   super.insertString(offset, str, a);
		   processChangedLines(offset, str.length());
	}
	
	public void remove(int offset, int length) throws BadLocationException {
		super.remove(offset, length);
		processChangedLines(offset, 0);
   }

   public void processChangedLines(int offset, int length) throws BadLocationException {
      String content = doc.getText(0, doc.getLength());
      Element root = doc.getDefaultRootElement();
      int startLine = root.getElementIndex( offset );
      int endLine = root.getElementIndex( offset + length );
      for (int i = startLine; i <= endLine; i++) {
         int startOffset = root.getElement( i ).getStartOffset();
         int endOffset = root.getElement( i ).getEndOffset();
         applyHighlighting(content, startOffset, endOffset - 1);
      }
   }

   public void applyHighlighting(String content, int startOffset, int endOffset) throws BadLocationException {
	   int index;
	   int lineLength = endOffset - startOffset;
	   int contentLength = content.length();
	   if (endOffset >= contentLength) {
		   endOffset = contentLength - 1;
	   } 
	   setHighlighting(true);
	   //  set normal attributes for the line
	   doc.setCharacterAttributes(startOffset, lineLength, normal, true);
	   
	   //  check for multi line comment
	   String multiLineStartDelimiter = "/*";
	   String multiLineEndDelimiter = "*/";
	   index = content.lastIndexOf( multiLineStartDelimiter, endOffset );
	   if (index > -1) {
		   int index2 = content.indexOf( multiLineEndDelimiter, index );
		   if ( (index2 == -1) || (index2 > endOffset) ) {
			   doc.setCharacterAttributes(index, endOffset - index + 1, comment, false);
			   return;
		   } else if (index2 >= startOffset) {
			   doc.setCharacterAttributes(index, index2 + 2 - index, comment, false);
			   return;
		   }
	   }
	   
	   //  check for single line comment
	   String singleLineDelimiter = "//";
	   index = content.indexOf( singleLineDelimiter, startOffset );
	   if ( (index > -1) && (index < endOffset) ) {
		   doc.setCharacterAttributes(index, endOffset - index + 1, comment, false);
		   endOffset = index - 1;
	   }
	   
	   //  check for tokens
	   checkForTokens(content, startOffset, endOffset);
	   setHighlighting(false);
   }
   
   private void checkForTokens(String content, int startOffset, int endOffset) {
	   while (startOffset <= endOffset) {
		   
		   //  find the start of a new token
		   while ( isDelimiter( content.substring(startOffset, startOffset + 1) ) ) {
			   if (startOffset < endOffset) {
				   startOffset++;
			   } else {
				   return;
			   }
		   }
		   
		   // delimitadores 
		   if ( isQuoteDelimiter( content.substring(startOffset, startOffset + 1) ) ) {
			   startOffset = getQuoteToken(content, startOffset, endOffset);
		   } else {
			   startOffset = getOtherToken(content, startOffset, endOffset);
		   }
	   }
   }
   
   private boolean isDelimiter(String character) {
	   String operands = ";:{}()[]+-/%<=>!&|^~*";
	   if (Character.isWhitespace( character.charAt(0) )||operands.indexOf(character) != -1 ) {
		   return true;
	   } else {
		   return false;
	   }
   }
   
   private boolean isQuoteDelimiter(String character) {
	   String quoteDelimiters = "\"'";
	   if (quoteDelimiters.indexOf(character) == -1) {
		   return false;
	   } else {
		   return true;
	   }
   }
   
   private boolean isKeyword(String token) {
	   Object o = keywords.get( token );
	   return o == null ? false : true;
   }
   
   private int getQuoteToken(String content, int startOffset, int endOffset) {
	   String quoteDelimiter = content.substring(startOffset, startOffset + 1);
	   String escapedDelimiter = "\\" + quoteDelimiter;
	   int index;
	   int endOfQuote = startOffset;
	   
	   //  skip over the escaped quotes in this quote
	   index = content.indexOf(escapedDelimiter, endOfQuote + 1);
	   while ( (index > -1) && (index < endOffset) ) {
		   endOfQuote = index + 1;
		   index = content.indexOf(escapedDelimiter, endOfQuote);
	   }
	   
	   // now find the matching delimiter
	   index = content.indexOf(quoteDelimiter, endOfQuote + 1);
	   if ( (index == -1) || (index > endOffset) ) {
		   endOfQuote = endOffset;
	   } else {
		   endOfQuote = index;
	   }
	   doc.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, quote, false);
	   return endOfQuote + 1;
   }
   
   private int getOtherToken(String content, int startOffset, int endOffset) {
	   int endOfToken = startOffset + 1;
	   while ( endOfToken <= endOffset ) {
		   if ( isDelimiter( content.substring(endOfToken, endOfToken + 1) ) ) {
			   break;
		   }
		   endOfToken++;
	   }
	   String token = content.substring(startOffset, endOfToken);
	   if (isKeyword( token )) {
		   doc.setCharacterAttributes(startOffset, endOfToken - startOffset, keyword, false);
	   }
	   return endOfToken + 1;
   }
   
   private boolean isHighlighting() {
	   return highlighting;
   }
   
   private void setHighlighting(boolean highlighting) {
	   this.highlighting = highlighting;
   }

}