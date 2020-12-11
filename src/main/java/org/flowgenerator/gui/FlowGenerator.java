 /**
 * FlowGenerator 
 * Gera um conjunto de grafos de fluxo de controle para os metodos de uma classe java
 * @author Jeferson Ferreira
 * @since  20/08/2005
 */
package org.flowgenerator.gui;

import java.io.*;
import java.net.URISyntaxException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.border.Border;

import org.flowgenerator.domain.GraphNode;
import org.flowgenerator.domain.JarUtil;
import org.flowgenerator.domain.JavaParser;

import javax.swing.*;
import java.text.*;

import java.util.*;

public class FlowGenerator extends JFrame {
	
	private static final long 	serialVersionUID = 1L;
	//private static final String arqJar = "FlowGenerator.jar";
	private static String containerJar;
	private static String language;
	private static JLabel lbStatus;
	private static String sFile[] = 
		{ "new",    "new.png",    "N", 
	      "open",   "open.png",   "A",
	      "save",   "save.png",   "S", 
	      "saveas", "saveas.png", "c",
	      "close",  "close.png",  "F",
	      null,     null,           null, 
	      "print",  "print.png",  "I", 
	      null,     null,           null, 
	      "exit",   "exit.png",   "r" };
	private static String sEdit[] = 
		{ "undo",      "undo.png",    "u",
		  "redo",      "redo.png",    "r",
		  "cut",       "cut.png",   "R",  
		  "copy",      "copy.png",  "C", 
		  "paste",     "paste.png", "o", 
		  null,        null,          null, 
		  "selectall", "blank16.gif", "t", 
		  null,        null,          null };
	private static String sTools[] = 
	    { "parser", "java.png",   "J",
		  null,     null,         null, 
		  "view",   "view.png", "V" };
	private static String sHelp[] = 
	    { "help",  "about.png",  "A",
		  null,    null,          null, 
		  "about", "info.png", "S" };
	private static ArrayList<Object> components, files;
	private static int i;
	private static MyEditor taEditor;
	private static JTabbedPane pEdit, pConsole;
	public static MyLanguageProperties mlp;
	private JTextArea taConsole;
	private JPanel pMain, pStatus;
	private JToolBar tb;
	private ArvoreDiretorio arv;
	private JFileChooser fc = new JFileChooser("..");
	private JFileFilter java_sources = new JFileFilter();
	private MenuHandler mh = new MenuHandler(); 


	/**
	 * Construtor da classe
	 */
	public FlowGenerator() {
		super("FlowGenerator");
		String src = null;
		try {
			src = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if (src.contains("jar")) {
		   containerJar = src;
	    } else {
	       containerJar = null;
	    }
	    
        getProperties();
        mlp = new MyLanguageProperties();
		// colecoes responsaveis pelo armazenamento do editores e dos arquivos correspondentes
		components = new ArrayList<Object>();
		
		// Filtros de arquivos
		java_sources.addType("java");
		java_sources.setDescription(mlp.getString("javafiles"));
	    fc.setFileFilter(java_sources);
	    
		// ToolBar
		tb = new JToolBar(JToolBar.HORIZONTAL);
		tb.setFloatable(true);

		
		// Menu
		JMenuBar mb = new JMenuBar();
		mb.add(criaMenu(mlp.getString("file"),  mlp.getString("file").charAt(0),  sFile,  mh));
		mb.add(criaMenu(mlp.getString("edit"),  mlp.getString("edit").charAt(0),  sEdit,  mh));
		mb.add(criaMenu(mlp.getString("tools"), mlp.getString("tools").charAt(0), sTools, mh));
		mb.add(criaMenu(mlp.getString("help"),  mlp.getString("help").charAt(0),  sHelp,  mh));
		setJMenuBar(mb);
		JMenu menu = mb.getMenu(1);
		JMenuItem mi = menu.add(new JCheckBoxMenuItem(mlp.getString("wrap"),true));
		mi.addActionListener(mh);
		
        // Arvore do diretorio
        arv = new ArvoreDiretorio("/");
			
		// Painel do Editor
		pEdit = new JTabbedPane();

		// Painel da Lista de Arquivos
		JScrollPane spDir = new JScrollPane(arv);
		
		// Divisao dos Paineis
		JSplitPane split1 =	new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spDir, pEdit);
		split1.setDividerLocation(200);
		split1.setOneTouchExpandable(true);
	
	    // Linha de Status
		pMain = new JPanel(new BorderLayout());
		pStatus = new JPanel(new BorderLayout());
		Border teste = BorderFactory.createRaisedBevelBorder();
		pStatus.setBorder(teste);
	    lbStatus = new JLabel();
	    lbStatus.setFont(new Font("Courier New",Font.PLAIN,11));
	    pStatus.add(lbStatus,"East");
	    pMain.add(split1,"Center");
		pMain.add(pStatus,"South");
	   
		// Console
		taConsole = new JTextArea();
		taConsole.setEditable(false);
		taConsole.setForeground(java.awt.Color.GRAY);
		pConsole = new JTabbedPane();
		JScrollPane spc = new JScrollPane(taConsole);
    	pConsole.addTab(mlp.getString("console"), new MyIcon("java.png").getIcone(), spc);
		JSplitPane split2 =	new JSplitPane(JSplitPane.VERTICAL_SPLIT, pMain, pConsole);
		split2.setDividerLocation(420);
		split2.setOneTouchExpandable(true);
		
		// adicao de componentes ao frame principal
		getContentPane().add(tb,"North");
		getContentPane().add(split2);
		setSize(800, 580);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public static void setStatus(int linha, int coluna){
		String padrao = mlp.getString("linecolumn");
		MessageFormat mf = new MessageFormat(padrao);
		Object parameters[] = { Integer.valueOf(linha),
				                Integer.valueOf(coluna) };
		String msg = mf.format(parameters, new StringBuffer(), null).toString();
		lbStatus.setText(msg);
	}
	public static void setIcon(ImageIcon icon){
		pEdit.setIconAt(pEdit.getSelectedIndex(),icon);
	}
	/**
	 * Metodo responsavel pela criacao dos itens do menu conforme a array informada
	 * @param nome - nome do menu
	 * @param acc - caractere de atalho
	 * @param itens - colecao de itens do menu
	 * @param al - listener do menu
	 * @return JMenu
	 */
	private JMenu criaMenu(String nome, int acc, String itens[], ActionListener al) {
		JMenu menu = new JMenu(nome);
		menu.setMnemonic(acc);
		JMenuItem mi;
		for (int i = 0; i < itens.length; i += 3) {
			if (itens[i] != null) {
				if (itens[i + 1] != null) {
					mi = new JMenuItem(mlp.getString(itens[i]), new ImageIcon(itens[i + 1]));
    				// Carrega a toolbar
					if (itens[i + 1]!="blank16.gif") {
					   JButton b = new JButton(new MyIcon(itens[i+1]).getIcone());
					   b.setActionCommand(mlp.getString(itens[i]));
					   b.setToolTipText(mlp.getString(itens[i]));
					   b.addActionListener(mh);
					   tb.add(b);
					}
				} else {
					mi = new JMenuItem(itens[i]);
					tb.addSeparator();
				}
				if (itens[i + 2] != null) {
					mi.setMnemonic(itens[i + 2].charAt(0));
				}
				mi.addActionListener(al);
				menu.add(mi);
			} else {
				menu.addSeparator();
			}
		}
		return menu;
	}

	/**
	 * Metodo responsavel por criar um novo editor e adiciona-lo numa nova aba e 
	 * ler o arquivo correspondente
	 */
	protected static void addTabedPane(File file) {
		if (file == null) {
			taEditor = new MyEditor();
		} else {
			taEditor = new MyEditor(file);
		}
		components.add(taEditor);
		i = pEdit.getTabCount();
		JScrollPane sp = new JScrollPane(taEditor);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    pEdit.addTab(taEditor.getFileName(), new MyIcon("java.png").getIcone(), sp);
	    if (!taEditor.getFileName().equals("NoName.java")) {
		   try {
			   taEditor.load(taEditor.getFileFullName());
		   } catch (IOException ioe) {
			   JOptionPane.showMessageDialog(null,
					   mlp.getString("file")+" "+ getFileName(i) + " "+mlp.getString("cannotopened"),
					   "FlowGenerator", JOptionPane.ERROR_MESSAGE);
		   }
	    }
	}
	protected void closeTabedPane() {
		i = pEdit.getSelectedIndex();
        pEdit.remove(i);
        components.remove(i);
        files.remove(i);
	}
	
	public static void main(String a[]) {
		new FlowGenerator().setVisible(true);
	}
	
	/**
	 * Metodo responsavel por retornar o nome do arquivo correspondente a aba passada como parametro
	 * @param i - numero da aba
	 * @return String - nome do arquivo
	 */
	public static String getFileName(int i) {
		return (String)files.get(i);
	}
	
	/**
	 * Metodo responsavel por armazenar os nomes dos arquivos abertos em cada editor
	 * @param fileName - nome do arquivo
	 * @param i - numero da Aba
	 */
	public static void setFileName(String fileName, int i) {
		if (files.size()<i) {
		   files.add(fileName);
		} else {
		   files.set(i,fileName);
		}
	}
	
	/**
	 * Classe responsavel pelo tratamento dos eventos do menu e da barra de ferramentas
	 * @author jeferson
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	class MenuHandler implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			String acao = ((AbstractButton) e.getSource()).getText();
			if (acao == "") {
				acao = ((AbstractButton) e.getSource()).getActionCommand();
			}

			// criar um novo editor 
			if (acao.equals(mlp.getString("new"))) {
			    addTabedPane(null);
					
		    // abrir um arquivo fonte existente em um novo editor
			} else if (acao.equals(mlp.getString("open"))) {
			    fc.setDialogTitle(mlp.getString("open"));
				int res = fc.showOpenDialog(getContentPane());
				if (res == JFileChooser.APPROVE_OPTION) {
					File arq = fc.getSelectedFile();
					addTabedPane(arq);
				}
            // sai do programa
			} else if (acao.equals(mlp.getString("exit"))) {
				System.exit(0);
				
			// chama o browser para visualizar o arquivo de ajuda
			} else if (acao.equals(mlp.getString("help"))) {
				GFCViewer browser = new GFCViewer("file:FlowGeneratorHelp.html");
				browser.setVisible(true);

			// exibe informacoes sobre a ferramenta
			} else if (acao.equals(mlp.getString("about"))) {
				JOptionPane.showMessageDialog(getContentPane(),mlp.getString("aboutmessage"),
						mlp.getString("about"), JOptionPane.INFORMATION_MESSAGE);
			}
			
			// verifica se existe pelo menos uma page, se existir pega o seu numero
			i = pEdit.getSelectedIndex();
			if (i>=0) {
				// encontra o Editor da page atual
	    		taEditor = (MyEditor)components.get(i);
				// salvar o conteudo do editor ativo no arquivo fonte correspondente
				if (acao.equals(mlp.getString("save"))) {
					if (pEdit.getTitleAt(pEdit.getSelectedIndex())!=mlp.getString("blank")) {
				    	try {
				    		//textFileWrite(taEditor.getText(),getFileName(i));
				    		taEditor.save();
				    	} catch (IOException ioe) {
				    		JOptionPane.showMessageDialog(getContentPane(),
				    		    mlp.getString("file")+" "+ taEditor.getFileName() + " "+mlp.getString("cannotsaved"),
								"FlowGenerator", JOptionPane.ERROR_MESSAGE);
				    	}
					} else {
						fc.setDialogTitle(mlp.getString("saveas"));
						int res = fc.showSaveDialog(getContentPane());
						if (res == JFileChooser.APPROVE_OPTION) {
							File arq = fc.getSelectedFile();
							try {
								//textFileWrite(taEditor.getText(), arq.getPath());
								taEditor.setFileName(arq.getPath());
								taEditor.save();
								pEdit.setTitleAt(i,arq.getName());
							} catch (IOException ioe) {
								JOptionPane.showMessageDialog(getContentPane(),
								mlp.getString("file")+" "+ arq.getPath() + " "+mlp.getString("cannotsaved"),
								"FlowGenerator", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
			    	FlowGenerator.setIcon(new MyIcon("java.png").getIcone());
			    	taEditor.setModified(false);
				// imprimir num arquivo 
				} else if (acao.equals(mlp.getString("print"))) {
					DocumentRenderer dr = new DocumentRenderer();
					dr.print(taEditor);
			    // salvar o conteudo do editor ativo num novo arquivo 
				} else if (acao.equals(mlp.getString("saveas"))) {
					fc.setDialogTitle(mlp.getString("saveas"));
					int res = fc.showSaveDialog(getContentPane());
					if (res == JFileChooser.APPROVE_OPTION) {
						File arq = fc.getSelectedFile();
						try {
							//textFileWrite(taEditor.getText(), arq.getPath());
							taEditor.setFileName(arq.getPath());
							taEditor.save();
							pEdit.setTitleAt(i,arq.getName());
						} catch (IOException ioe) {
							JOptionPane.showMessageDialog(getContentPane(),
									mlp.getString("file")+" "+ arq.getPath() + " "+mlp.getString("cannotsaved"),
									"FlowGenerator", JOptionPane.ERROR_MESSAGE);
						}
						FlowGenerator.setIcon(new MyIcon("java.png").getIcone());
					}
				
			    // fechar o editor ativo
				} else if (acao.equals(mlp.getString("close"))) {
					int opt;
					if (taEditor.isModified()) {
						opt = JOptionPane.showConfirmDialog(getContentPane(),
								mlp.getString("closing"),
								mlp.getString("close"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (opt==JOptionPane.YES_OPTION) {
							closeTabedPane();
						}
				   } else {
					   closeTabedPane();
				   }

				// copiar selecao para area de transferencia   	
				} else if (acao.equals(mlp.getString("copy"))) {
					taEditor.copy();
				
			    // colar texto da area de transferencia   	
				} else if (acao.equals(mlp.getString("paste"))) {
					taEditor.paste();
					
				// desfazer	
				} else if (acao.equals(mlp.getString("undo"))) {
					if (taEditor.canUndoAction()) {
						taEditor.undoAction();
					}
					
			    // refazer	
				} else if (acao.equals(mlp.getString("redo"))) {
					if (taEditor.canRedoAction()) {
						taEditor.redoAction();
					}
					
		        // recortar texto selecionado e copiar para a area de transfer�ncia   	
				} else if (acao.equals(mlp.getString("cut"))) {
					taEditor.cut();

			    // selecionar todo o texto   	
				} else if (acao.equals(mlp.getString("selectall"))) {
					taEditor.selectAll();

			    // habilita e desabilita a quebra de linha no editor ativo
				} else if (acao.equals(mlp.getString("wrap"))) {
					if (taEditor.getLineWrap()) {
						taEditor.setLineWrap(false);
					} else {
						taEditor.setLineWrap(true);
					}

                // chama o programa que realiza o parser, monta o grafo na memoria, gera o arquivo .dot e gera o arquivo .png
				} else if (acao.equals(mlp.getString("parser"))) {
					if (!taEditor.isModified()) {
						// inicializa a numeracao dos nos
						new GraphNode();
						JavaParser.main(taEditor.getFileFullName(),taConsole);
						try {
							//taConsole.append(mlp.getString("generating_png")+((String)files.get(i)).replace(".java","")+".png");
							Runtime.getRuntime().exec("dot -Tpng -o"+(taEditor.getFileFullName()).replace(".java","")+".png "+(taEditor.getFileFullName()).replace(".java","")+".dot");
						} catch (IOException erro) {
							taConsole.append(mlp.getString("graphviz"));
						}				    	
				   } else {
						JOptionPane.showMessageDialog(getContentPane(),
								mlp.getString("parsing"),
								"FlowGenerator", JOptionPane.INFORMATION_MESSAGE);
				   }
				
			    // chama o componente responsavel pela visualizacao do grafo (.png)
				} else if (acao.equals(mlp.getString("view"))) {
					GFCViewer browser = new GFCViewer("file:"+(taEditor.getFileFullName()).replace(".java","")+".html");
					browser.setVisible(true);
				}
			} else {
				if (!acao.equals(mlp.getString("about"))&&!acao.equals(mlp.getString("help"))) {
				   JOptionPane.showMessageDialog(getContentPane(),
				       mlp.getString("file_opened"),
					   "FlowGenerator", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	private static void getProperties() {
		Properties config = new Properties();
		try {
			if (containerJar != null) {
			   config.load(new ByteArrayInputStream(JarUtil.extract(containerJar,"FlowGenerator.properties")));
			} else {
				FileReader reader=new FileReader("./src/main/resources/FlowGenerator.properties");
				config.load(reader);
			}
		
			// carrega as propriedades
			setLanguage(config.getProperty("language","./"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					mlp.getString("loading_properties")+" "+e,
					"FlowGenerator", JOptionPane.ERROR_MESSAGE);
		}
	}
	public static String getLanguage() {
		return language;
	}
	private static void setLanguage(String language) {
		FlowGenerator.language = language;
	}
	public static String getArqJar() {
		return containerJar;
	}
}