/**
 * GFCViewer 
 * Browser responsavel pela exibicao do arquivo html gerado pela ferramenta
 * onde se visualiza o GFC e as demais informacoes dos metodos analisados
 * 
 * @author Jeferson Ferreira
 * @since  30/11/2005
 */
package org.flowgenerator.gui;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class GFCViewer extends JFrame implements ActionListener, HyperlinkListener {
	private static final long serialVersionUID = 1L;
	private JButton bAbrir, bVoltar, bAvancar;
	private JEditorPane conteudo = new JEditorPane();
	private JFileFilter html_sources = new JFileFilter();
	private JFileChooser fc = new JFileChooser();
	private JLabel lStatus = new JLabel("Bem-vindo!");
	private JTextField urlUsuario = new JTextField();
	private URL urlAtual;
	private Stack<Object> urlAnt = new Stack<Object>(), urlPost = new Stack<Object>();
	
	public GFCViewer(String File) {
		super("FlowGenerator - Viewer");
		
		// Filtros de arquivos
		html_sources.addType("html");
		html_sources.setDescription(FlowGenerator.mlp.getString("htmlfiles"));
	    fc.setFileFilter(html_sources);
	    
		JToolBar tools = new JToolBar();
		tools.add(bVoltar = new JButton(new MyIcon("back16.gif").getIcone()));
		tools.add(bAvancar = new JButton(new MyIcon("forward16.gif").getIcone()));
		tools.add(bAbrir = new JButton(new MyIcon("open16.gif").getIcone()));
		tools.addSeparator();
		tools.add(urlUsuario);
		getContentPane().add(tools, "North");
		bAbrir.addActionListener(this);
		bAvancar.addActionListener(this);
		bVoltar.addActionListener(this);
		urlUsuario.addActionListener(this);
		JScrollPane sp = new JScrollPane(conteudo);
		conteudo.setEditable(false);
		conteudo.addHyperlinkListener(this);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setBorder(BorderFactory.createEtchedBorder());
		getContentPane().add(sp, "Center");
		getContentPane().add(lStatus, "South");
		lStatus.setBorder(BorderFactory.createEmptyBorder(1,2,1,2));
		fc.setCurrentDirectory(new File("."));
		try {
			carregaURL(0, new URL(File));
		} catch (MalformedURLException mue) { }
		setSize(800, 580);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==urlUsuario) {
			try {
				URL u = new URL(urlUsuario.getText());
				carregaURL(-1, u);
			} catch (MalformedURLException mue) {
				lStatus.setText(FlowGenerator.mlp.getString("invalid_url"));
			}
		} else if (e.getSource()==bVoltar) {
			if (!urlAnt.empty()) {
				carregaURL(+1,(URL)urlAnt.pop());
			}
		} else if (e.getSource()==bAvancar) {
			if (!urlPost.empty()) {
				carregaURL(-1,(URL)urlPost.pop());
			}
		} else if (e.getSource()==bAbrir) {
		//	new Report();
			int res = fc.showOpenDialog(this);
			if (res==JFileChooser.APPROVE_OPTION) {
				try {
					File arq = fc.getSelectedFile();
					URL u = new URL("file:"+arq.getPath());
					carregaURL(-1, u);
				} catch (MalformedURLException mue) {
					lStatus.setText(FlowGenerator.mlp.getString("invalid_url"));
				}
			}
		}
	}
	
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			carregaURL(-1, e.getURL());
		}
	}
	
	public void carregaURL(int mode, URL url) {
		try {
			lStatus.setText(FlowGenerator.mlp.getString("loading"));
			conteudo.setPage(url);
			setTitle("FlowGenerator - Viewer ["+url.toString()+"]");
			lStatus.setText(conteudo.getContentType()+"");
			urlUsuario.setText(url.toString());
			switch(mode) {
			case -1:
				urlAnt.push(urlAtual);
				break;
			case 1:
				urlPost.push(urlAtual);
				break;
			}
			urlAtual = url;
		} catch (IOException ioe) {
			lStatus.setText(FlowGenerator.mlp.getString("url_error")+" "+url.toString()+".");
		}
	}
}
