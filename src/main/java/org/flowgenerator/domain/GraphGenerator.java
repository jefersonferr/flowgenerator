package org.flowgenerator.domain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

import org.flowgenerator.gui.FlowGenerator;

/**
 * Classe responsavel pelo controle da geracao dos clusters de classes
 * e metodos
 * @author Jeferson Ferreira
 * @since  03/08/2005
 */
public class GraphGenerator {
	
    // nome do arquivo que esta sendo analisado
    private String sourceFile;
	// referencia da classe inicial
	private ClusterClass mainClass;
	// referencia da classe corrente
	private ClusterClass current_class;
	// referencia do metodo corrente
	private ClusterMethod current_method;
	// Pilha de Classes
	private Stack<ClusterClass> stackClass;
	// caractere que representa as aspas
    char aspas = 34;
    // variaveis responsaveis pelo calculo da CC
    int arcos, nos, cc;
	
	/**
	 * Construtor da classe 
	 * */
	public GraphGenerator(String source) {
		stackClass = new Stack<ClusterClass>();
                setSourceFile(source);
	}
	
	/**
	 * Retorna a classe que esta sendo analisada pelo parser no momento
	 * @return ClusterClass
	 */
	private ClusterClass getCurrent_class() {
		return current_class;
	}
	
	/**
	 * Altera a referencia da classe que esta sendo analisada pelo parser 
	 * no momento
	 * @param ClusterClass current_class - classe corrente
	 * @see ClusterClass
	 */
	private void setCurrent_class(ClusterClass current_class) {
		this.current_class = current_class;
	}
	
	/**
	 * Retorna o metodo que esta sendo analisado pelo parser no momento
	 * @return ClusterMethod
	 */
	private ClusterMethod getCurrent_method() {
		return current_method;
	}
	
	/**
	 * Altera a referencia do metodo ue esta sendo analisado pelo parser 
	 * no momento
	 * @param ClusterMethod current_method
	 * @see ClusterMethod
	 */
	private void setCurrent_method(ClusterMethod current_method) {
		this.current_method = current_method;
	}
	
	/**
	 * Abre um novo cluster de classe 
	 * @param label String - rotulo do cluster
	 * @see ClusterClass
	 */
	public void openClusterClass(String label) {
		// se a classe principal ainda nao tiver sido preenchida
		if (mainClass == null) {
			// a classe corrente assume o papel de classe principal
			setCurrent_class(new ClusterClass(label));
			mainClass = current_class;
		} else {
			// empilha as classe anterior para controle de ordem de criacao
			ClusterClass aux = new ClusterClass(label);
			getCurrent_class().addClasses(aux);
			stackClass.push(getCurrent_class());
			setCurrent_class(aux);	   
		}
	}
	
	/**
	 * Fecha um cluster de classe
	 * @see ClusterClass
	 */
	public void closeClusterClass() {
	   if (!stackClass.isEmpty()) {
		  setCurrent_class((ClusterClass)stackClass.pop());
	   }
	}

    /**
     * Abre um novo cluster de metodo
     * @param label String - rotulo do cluster
     * @see ClusterMethod
     */
    public void openMethodCluster(String label){
       ClusterMethod aux = new ClusterMethod(label);
       setCurrent_method(aux);
       getCurrent_class().addMetodos(aux);
    }
	
	/**
	 * Repassa o comando enviado pelo parser para o metodo
	 * que esta sendo analisado no momento
	 * @param c - int -numero do comando
	 * @see ClusterMethod
	 */
    public void processCommand(int c) {
    	getCurrent_method().processCommand(c);
    }
    
    public void addInformation(String c, String e, int l) {
    	getCurrent_method().addInformation(c,e, l);
    }
    /**
     * Gera um arquivo no formato dot com toda a estrutura necessaria
     * para visualizacao da classe, suas classes internas, seu metodos
     * e o GFC de cada metodo. Este arquivo � tratado pelo GraphViz
     * @link www.graphviz.org 
     */
    public void geraDot() {
    	 File         arq  = new File(getSourceFile()+".dot");
    	 StringBuffer sb   = new StringBuffer();
         ClusterClass classe;
         String       iden = new String("");
         // gera o cabecalho do arquivo .dot
    	 sb.append("digraph g {\n");
    	 sb.append(iden+"   fontsize=12;\n");
    	 sb.append(iden+"   node [fontsize=12];\n");
    	 sb.append(iden+"   node [style=filled];\n");
    	 sb.append(iden+"   node [color=lightgrey];\n");
    	 sb.append(iden+"   node [shape=circle];\n");
    	 sb.append(iden+"   node [fixedsize=true];\n");
         
         // gera os cluster das classes - invocando recursivamente o metodo geraClusterClass
         classe = getMainClass();
         sb.append((geraClusterClass(classe, iden)).toString());
         sb.append("}\n");
    	 try {
    	 FileWrite(sb.toString(), arq.getPath());
    	 } catch (IOException ioe) {
    	 } 
    }
    
    /**
     * Gera um cluster para uma classe, apos o termino pega a lista de classes
     * internas e executa recursivamente para cada classe da lista de classes
     * @param classe - classe
     * @param iden - string responsavel pela identacao do arquivo .dot
     * @return StringBuffer 
     * @link www.graphviz.org
     */
    private StringBuffer geraClusterClass(ClusterClass classe, String iden) {    
           LinkedList<ClusterClass> listClasses;
           LinkedList<Integer> listArcs;
           LinkedList<GraphNode> listNodes;
		   LinkedList<ClusterMethod> listMetodos;
    	   ListIterator<ClusterClass> liClasses;
    	   ListIterator<Integer> liArcs;
    	   ListIterator<GraphNode> liNodes;
		   ListIterator<ClusterMethod> liMetodos;
           ClusterClass innerClass;
           ClusterMethod metodo;
           ControlFlowGraph graphNodes;
    	   GraphNode valueNode;
           boolean    gerou;
           StringBuffer sb = new StringBuffer();
           int dest;
           
           iden = new String(iden+"   ");   
           sb.append(iden+"subgraph "+classe.getNumberCluster()+" {\n");
           sb.append(iden+"   label = "+aspas+classe.getLabelCluster()+aspas+";\n");
           sb.append(iden+"   style = "+aspas+classe.getStyleCluster()+aspas+";\n");
           sb.append(iden+"   color = "+aspas+classe.getColorCluster()+aspas+";\n");

           // gera os clusters dos metodos
           listMetodos = classe.getMetodos();
           liMetodos = listMetodos.listIterator();
           while (liMetodos.hasNext()) {
                  metodo = (ClusterMethod)liMetodos.next();
                  sb.append(iden+"   subgraph "+metodo.getNumberCluster()+" {\n");
                  sb.append(iden+"      label = "+aspas+metodo.getLabelCluster()+aspas+";\n");
                  sb.append(iden+"      style = "+aspas+metodo.getStyleCluster()+aspas+";\n");
                  sb.append(iden+"      color = "+aspas+metodo.getColorCluster()+aspas+";\n");
                  StringBuffer sb2 = new StringBuffer();
             	  sb2.append("digraph g {\n");
           	      sb2.append("   fontsize=12;\n");
           	      sb2.append("   node [fontsize=12];\n");
           	      sb2.append("   node [style=filled];\n");
           	      sb2.append("   node [color=lightgrey];\n");
           	      sb2.append("   node [shape=circle];\n");
           	      sb2.append("   node [fixedsize=true];\n");
                  File arq2  = new File(getSourceFile()+"_"+metodo.getNameFileCluster()+".dot");
                  sb2.append("      style = "+aspas+metodo.getStyleCluster()+aspas+";\n");
                  sb2.append("      color = "+aspas+metodo.getColorCluster()+aspas+";\n");
                  
                  graphNodes = metodo.getGfc();
      	          listNodes = graphNodes.getNodes();
    	          liNodes = listNodes.listIterator();
    	          gerou = false;
    	          nos = 0;
	              arcos = 0;
	              while (liNodes.hasNext()) {
    	        	     nos++;
    	                 valueNode = (GraphNode)liNodes.next();
    	                 listArcs = valueNode.getArcs();
    	                 liArcs = listArcs.listIterator();

    	                 while (liArcs.hasNext()) {
    	                	    arcos++;
    	                	    dest = (Integer)liArcs.next();
    	                        sb.append(iden+"      "+valueNode.getLabelNode()+" -> "+dest+"\n");
    	                        sb2.append("      "+valueNode.getLabelNode()+" -> "+dest+"\n");
    	                        gerou = true;
    	                 }
    	     	         if (!gerou) {
    	     	        	 sb.append(iden+"      "+valueNode.getLabelNode()+"\n");
    	     	        	 sb2.append("      "+valueNode.getLabelNode()+"\n");
    	      	         }
    	          }                  
    	          metodo.setCc(arcos-nos+2);
                  sb.append(iden+"      "+metodo.getNumberCluster()+" [label="+aspas+"CC="+metodo.getCc()+aspas+", shape=plaintext, fixedsize=false, style=none, color=black];\n");
                  sb.append(iden+"   }\n");
                  sb2.append("      "+metodo.getNumberCluster()+" [label="+aspas+"CC="+metodo.getCc()+aspas+", shape=plaintext, fixedsize=false, style=none, color=black];\n");
                  sb2.append("}\n");
                  
             	 try {
                	 FileWrite(sb2.toString(), arq2.getPath());
                 } catch (IOException ioe) {
                 } 
				 try {
    				Runtime.getRuntime().exec("dot -Tpng -o"+(arq2.getPath()).replace(".dot","")+".png "+arq2.getPath());
				 } catch (IOException erro) {
				 }				    	
           }
           listClasses = (LinkedList<ClusterClass>)classe.getClasses();
           liClasses = listClasses.listIterator();
           while (liClasses.hasNext()) {
                  innerClass = (ClusterClass)liClasses.next();
                  sb.append((geraClusterClass(innerClass, iden)).toString());
           }     
           sb.append(iden+"}\n");
           return sb;
    }
    
    /**
     * Gera um arquivo no formato dot com toda a estrutura necessaria
     * para visualizacao da classe, suas classes internas, seu metodos
     * e o GFC de cada metodo. Este arquivo e tratado pelo GraphViz
     * @link www.graphviz.org 
     */
    public void geraHtml() {
    	 File         arq    = new File(getSourceFile()+".html");
    	 StringBuffer sb     = new StringBuffer();
         String       classe = (arq.getName()).replace(".html","");
         Date         data   = new Date();
         ClusterClass classeMain = getMainClass();
         String       iden = new String("");
         // gera o arquivo HTML
    	 sb.append("<HTML>\n");
    	 sb.append("<!-- Generated by FlowGenerator on "+data.toString()+" -->\n");
    	 sb.append("<HEAD><TITLE>"+FlowGenerator.mlp.getString("gfc")+" - "+arq.getName()+"</TITLE></HEAD>\n");
    	 sb.append("   <BODY>\n");
    	 sb.append("      <H1>FlowGenerator</H1>\n");
    	 sb.append("      <TABLE BORDER="+aspas+"1"+aspas+" WIDTH="+aspas+"100%"+aspas+" CELLPADDING="+aspas+"3"+aspas+">\n");
    	 sb.append("      <TR BGCOLOR="+aspas+"#CCCCFF"+aspas+" CLASS="+aspas+"TableHeadingColor"+aspas+">\n");
      	 sb.append("      <TD ALIGN="+aspas+"left"+aspas+" ><FONT SIZE="+aspas+"+2"+aspas+">\n");
      	 sb.append("      <B>"+FlowGenerator.mlp.getString("gfc")+"</B></FONT></TD>\n");
      	 sb.append("      </TR>");
    	 sb.append("      <TR BGCOLOR="+aspas+"white"+aspas+" CLASS="+aspas+"TableRowColor"+aspas+">\n");
    	 sb.append("      <TD><IMG SRC = "+aspas+classe+".png"+aspas+"></TD>\n");
    	 sb.append("      </TR>");
    	 sb.append("      </TABLE>");
         sb.append((geraClusterClassHtml(classeMain, iden)).toString());
    	 sb.append("   </BODY>\n");
    	 sb.append("</HTML>\n");
    	 try {
    	 FileWrite(sb.toString(), arq.getPath());
    	 } catch (IOException ioe) {
    	 } 
    }
    
    private StringBuffer geraClusterClassHtml(ClusterClass classe, String iden) {   
    	   File         arq    = new File(getSourceFile());
    	   String       nome_arq = arq.getName();
           LinkedList<?> listClasses, listMetodos, listNodes;
    	   ListIterator<?> liClasses, liMetodos, liNodes;
           ClusterClass innerClass;
           ClusterMethod metodo;
           ControlFlowGraph graphNodes;
    	   GraphNode valueNode;
           StringBuffer sb = new StringBuffer();
           iden = new String(iden+"   ");  
           sb.append(iden+"      <TABLE BORDER="+aspas+"1"+aspas+" WIDTH="+aspas+"100%"+aspas+" CELLPADDING="+aspas+"3"+aspas+">\n");
      	   sb.append(iden+"      <TR BGCOLOR="+aspas+"#CCCCFF"+aspas+" CLASS="+aspas+"TableHeadingColor"+aspas+">\n");
      	   sb.append(iden+"      <TH ALIGN="+aspas+"left"+aspas+" ><FONT SIZE="+aspas+"+2"+aspas+">\n");
      	   sb.append(iden+"      <B>"+classe.getLabelCluster()+"</B></FONT></TH>\n");
      	   sb.append(iden+"      </TR>\n");
      	   sb.append(iden+"      </TABLE>\n");

           // gera os clusters dos metodos
           listMetodos = (LinkedList<?>)classe.getMetodos();
           liMetodos = listMetodos.listIterator();
          while (liMetodos.hasNext()) {
                  metodo = (ClusterMethod)liMetodos.next();
                  sb.append(iden+"      <TABLE BORDER="+aspas+"1"+aspas+"  CELLPADDING="+aspas+"3"+aspas+">\n");
              	  sb.append(iden+"      <CAPTION><FONT SIZE="+aspas+"+1"+aspas+">"+metodo.getLabelCluster()+"</FONT></CAPTION>\n");
              	  sb.append(iden+"<TD>\n");
                  sb.append(iden+"      <TABLE BORDER="+aspas+"0"+aspas+"  CELLPADDING="+aspas+"3"+aspas+">\n");
                  sb.append(iden+"</TR>      <TR BGCOLOR="+aspas+"white"+aspas+" CLASS="+aspas+"TableRowColor"+aspas+">");
             	  sb.append(iden+"      <IMG SRC = "+aspas+nome_arq+"_"+metodo.getNameFileCluster()+".png"+aspas+">\n");
              	  sb.append(iden+"      </TABLE>\n");
              	  sb.append(iden+"</TD>\n");
                  graphNodes = metodo.getGfc();
      	          listNodes = (LinkedList<?>)graphNodes.getNodes();
    	          liNodes = listNodes.listIterator();
              	  sb.append(iden+"<TD>\n");
                  sb.append(iden+"      <TABLE BORDER="+aspas+"1"+aspas+"  CELLPADDING="+aspas+"3"+aspas+">\n");
               	  sb.append(iden+"      <FONT SIZE="+aspas+"+1"+aspas+">\n");
               	  sb.append(iden+"      <TR BGCOLOR="+aspas+"#CCCCFF"+aspas+" CLASS="+aspas+"TableHeadingColor"+aspas+">\n");
 	              sb.append(iden+"      <TH><B>"+FlowGenerator.mlp.getString("node")+"</B></TH>\n");
	              sb.append(iden+"      <TH><B>"+FlowGenerator.mlp.getString("command")+"</B></TH>\n");  
	              sb.append(iden+"      <TH><B>"+FlowGenerator.mlp.getString("expression")+"</B></TH>\n");
	              sb.append(iden+"      <TH><B>"+FlowGenerator.mlp.getString("sourceline")+"</B></TH>\n");
    	          iden = iden + "   ";	          
  	              while (liNodes.hasNext()) {
    	                 valueNode = (GraphNode)liNodes.next();
    	                 sb.append(iden+"      <TR BGCOLOR="+aspas+"white"+aspas+" CLASS="+aspas+"TableHeadingColor"+aspas+">\n");
    	                 sb.append(iden+"      <TD ALIGN="+aspas+"center"+aspas+">"+valueNode.getLabelNode()+"</TD>\n");
    	                 sb.append(iden+"      <TD ALIGN="+aspas+"center"+aspas+">"+valueNode.getDescCommand()+"</TD>\n");  
    	                 sb.append(iden+"      <TD ALIGN="+aspas+"center"+aspas+">"+valueNode.getExpression()+"</TD>\n");
    	                 sb.append(iden+"      <TD ALIGN="+aspas+"center"+aspas+">"+valueNode.getLineCommand()+"</TD>\n");
    	                 sb.append(iden+"      </TR>\n");
    	          }          
             	  sb.append(iden+"      </FONT></TABLE>\n");
              	  sb.append("</TD>\n");
              	  sb.append("</TABLE>\n");
           }
           listClasses = (LinkedList<?>)classe.getClasses();
           liClasses = listClasses.listIterator();
           while (liClasses.hasNext()) {
                  innerClass = (ClusterClass)liClasses.next();
                  sb.append((geraClusterClassHtml(innerClass, iden)).toString());
           }
           return sb;
    }
    
 
    /**
     * Grava o conteudo fornecido em content num arquivo denominado filename
     * @param  content - String - conteudo 
     * @param  filename - String - nome do arquivo
     * @throws IOException
     * @see PrintWriter
     */
    public void FileWrite(String content, String filename) throws IOException {
    	PrintWriter out = new PrintWriter(new FileWriter(filename));
    	out.print(content);
    	out.close();
    }
	
    /**
     * retorna o nome do arquivo fonte que esta sendo analisado pelo Parser
     * @return String
     */
     public String getSourceFile() {
         return sourceFile;
     }
	
     /** 
      * altera o nome do arquivo fonte que esta sendo analisado pelo Parser
      * @param source - estilo da borda
      */
      private void setSourceFile(String source) {
         this.sourceFile = source;
      }  
      
    /**
     * retorna o nome do classe principal
     * @return ClusterClass
     */
     private ClusterClass getMainClass() {
         return mainClass;
     }

}