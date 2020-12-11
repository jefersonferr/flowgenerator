/**
 * MyEditor
 * Editor de textos responsavel pela edicao dos arquivos fonte
 * @author Jeferson Ferreira
 * @since  12/11/2005
 * @version 1.0
 */
package org.flowgenerator.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import org.flowgenerator.controller.FileData;
import org.flowgenerator.controller.MyDocument;

public class MyEditor extends JTextPane {
	private static final long serialVersionUID = 1L;
	private boolean modified;
	private boolean wrap;
	private MyDocument doc;
	private int lastLength;
	private FileData file;

	@SuppressWarnings("serial")
	public MyEditor() {
		this.file = new FileData( "NoName.java", "." );
		this.setEditorKit(new StyledEditorKit());
		this.setEditable(true);
		doc = new MyDocument();
		this.setDocument(doc);
		EditorHandler cl = new EditorHandler();
		this.addCaretListener(cl);
		this.setFont(new Font("Courier New",Font.PLAIN,12));
		this.setTabSize(4);
		this.setLineWrap(false);
        // Acoes 
	    this.getActionMap().put("Undo",
	        new AbstractAction("Undo") {
				public void actionPerformed(ActionEvent evt) {
					undoAction();
				}
	    	});
	    this.getActionMap().put("Redo",
	    	new AbstractAction("Redo") {
	            public void actionPerformed(ActionEvent evt) {
	            	redoAction();
	            }
	    	});
	    
	    // Configura as teclas de atalho
	    this.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
	    this.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
	}
	public MyEditor(File fileName){
		this();
		this.file = new FileData(fileName);
		try {
			this.load(file.getFileFullPath());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					   FlowGenerator.mlp.getString("file")+" "+ 
					   file.getFileName()+ " "+
					   FlowGenerator.mlp.getString("cannotopened"),
					   "FlowGenerator", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void undoAction() {
		doc.undoAction();
	}
	public void redoAction() {
		doc.redoAction();
	}
	public boolean canRedoAction() {
		return doc.canRedoAction();
	}
	public boolean canUndoAction() {
		return doc.canUndoAction();
	}
	public boolean isModified() {
		return modified;
	}
	
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public int getLastLength() {
		return lastLength;
	}
	
	public void setLastLength(int lastLength) {
		this.lastLength = lastLength;
	}

	public void setBounds(int x, int y, int width, int height) {
	   if (wrap) {
		  super.setBounds(x,y,width,height);
	   } else {
	      Dimension size = this.getPreferredSize();
	      super.setBounds(x,y,Math.max(size.width,width),height);
	   }
	}
	
	public void setLineWrap(boolean b) {
		this.wrap = b;
	}
	
	public boolean getLineWrap() {
		return this.wrap;
	}
	
	public void setTabSize(int charactersPerTab) {
		FontMetrics fm = this.getFontMetrics( this.getFont() );
		int charWidth = fm.charWidth('w');
		int tabWidth = charWidth * charactersPerTab;
		TabStop[] tabs = new TabStop[50];
		for (int j = 0; j < tabs.length; j++) {
			int tab = j + 1;
			tabs[j] = new TabStop( tab * tabWidth );
		}
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = this.getDocument().getLength();
		this.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
	}
	
	/**
	 * Grava o conteudo do componente num arquivo texto
	 * @throws IOException
	 */
	public void save() throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(file.getFileFullPath()));
		out.print(this.getText());
		out.close();
		out.flush();
	}
	
	public void load(String fileName) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String line;
		StringBuffer buffer = new StringBuffer();
		while ((line = in.readLine()) != null)
			buffer.append(line + "\n");
		in.close();
		this.setText(buffer.toString());
	}
	
	/**
	 * Metodo responsavel por retornar o nome e o caminho completo do arquivo contido neste componente
	 * @return String - nome do arquivo
	 */
	public String getFileFullName() {
		return file.getFileFullPath();
	}
	
	/**
	 * Metodo responsavel por retornar somenteo o nome do arquivo contido neste componente
	 * @return String - nome do arquivo
	 */
	public String getFileName() {
		return file.getFileName();
	}
	/**
	 * Metodo responsavel por modificar o nome do arquivo contido neste componente
	 * @param fileName - nome do arquivo
	 * @param i - numero da Aba
	 */
	public void setFileName(String fileName) {
		this.file.setFilePath(fileName);
	}
	
	private class EditorHandler implements CaretListener {
		private int linha = 1, coluna;
		public void caretUpdate(CaretEvent e) {
			String texto = getText();
			posicaoCursor(e, texto+" ");
			verificaMudanca(e, texto);
			FlowGenerator.setStatus(linha, coluna);
			setLastLength(texto.length());
		}
		private void posicaoCursor(CaretEvent ce, String texto) {
			if (texto.length()>1) {
				String temp = texto.substring(0, ce.getMark());
				
				// conta as linhas
    		    Element root = doc.getDefaultRootElement();
			    linha = root.getElementIndex( ce.getMark() ) + 1;
			    
				// conta as Colunas
				String tempColuna;
				coluna = 1;
				if (temp.lastIndexOf("\n") > -1) {
					tempColuna = temp.substring(temp.lastIndexOf("\n") + 1,temp.length());
				} else {
					tempColuna = temp;
				}
				for (int i = 0; i < tempColuna.length(); i++) {
					if (tempColuna.charAt(i) == '\t') {
						coluna += 10 - ((coluna-1) % 10);
					} else {
						coluna ++;
					}
				}
			}  
		}
		
		private void verificaMudanca(CaretEvent ce,String texto) {
			int tamAnt, tamanho;
			tamAnt = getLastLength();
			tamanho = texto.length();
			if (tamAnt != 0) {
				if (tamanho != tamAnt) {
					FlowGenerator.setIcon(new MyIcon("java_mod.png").getIcone());
				    setModified(true);
				}
			}
		}
	}
}
