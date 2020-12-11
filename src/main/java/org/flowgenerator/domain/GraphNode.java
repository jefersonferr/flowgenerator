/**
 * 
 Classe...: Node
 Autor....: Jeferson Ferreira
 Data.....: 12/07/2005
 Objetivo.: Estrutura de dados que armazena as informacoes dos nos do grafo de fluxo de controle
 */
package org.flowgenerator.domain;

import java.util.LinkedList;

public class GraphNode {
	static private int nextLabel;
	private Integer numNode;
	private Integer labelNode;
	private Integer prevNode;
	private Integer levelNode;
	private Integer commandNode;
	private char statusNode, statusAnt;
	private LinkedList<Integer> arcsNode;
	private boolean processado;
	private String expression;
	private String descCommand;
	private Integer lineCommand;
	
	public GraphNode() {
		setNextLabel(0);
	}
	public GraphNode(int number, int previous, int level, int command) {
		numNode = Integer.valueOf(number);
		setLabelNode(Integer.valueOf(this.getNextLabel()));
		prevNode = Integer.valueOf(previous);
		levelNode = Integer.valueOf(level);
		commandNode = Integer.valueOf(command);
		this.active();
		this.setStatusAnt('A');
		arcsNode = new LinkedList<Integer>();
		setProcessado(false);
	}
	public Integer getNumber() {
		return numNode;
	}
	public int getPrevious() {
		return prevNode.intValue();
	}
	public int getLevel() {
		return levelNode.intValue();
	}
	public int getCommand() {
		return commandNode.intValue();
	}
	public void setCommand(int c) {
		commandNode = Integer.valueOf(c);
	}
	public char getStatus() {
		return statusNode;
	}
	public void inactive() {
		if (this.getStatus()!='E') {
			this.setStatusAnt(this.getStatus());
		}
		statusNode = 'I';
	}
	public void active() {
		if (this.getStatusAnt()!= 0){
			if (this.getStatus()!='E') {
				this.setStatusAnt(this.getStatus());
			}
		} else {
			this.setStatusAnt('A');
		}
		statusNode = 'A';
	}
	public void exception() {
		if (this.getStatus()!='E') {
			this.setStatusAnt(this.getStatus());
		}
		statusNode = 'E';
	}
	public void insertArc(int target) {
		arcsNode.add(Integer.valueOf(target));
	}
	public boolean removeArc(int target) {
		return arcsNode.remove(Integer.valueOf(target));
	}
	public LinkedList<Integer> getArcs() {
		return arcsNode;
	}
	public int numArcs() {
		return arcsNode.size();
	}
	public boolean equals(Object obj) {
		return numNode.equals(((GraphNode) obj).getNumber());
	}
	public Integer getLabelNode() {
		return labelNode;
	}
	public void setLabelNode(Integer labelNode) {
		this.labelNode = labelNode;
	}
	private int getNextLabel() {
		return nextLabel++;
	}
	public static void setNextLabel(int nextLabel) {
		GraphNode.nextLabel = nextLabel;
	}
	public char getStatusAnt() {
		return statusAnt;
	}
	public void setStatusAnt(char statusAnt) {
		this.statusAnt = statusAnt;
	}
	public boolean isProcessado() {
		return processado;
	}
	public void setProcessado(boolean processado) {
		this.processado = processado;
	}
	public String getDescCommand() {
		if (descCommand == null)
		   return "-";
		else
		   return descCommand;
	}
	public void setDescCommand(String descCommand) {
		this.descCommand = descCommand;
	}
	public String getExpression() {
		if (expression == null)
		   return "-";
		else
		   return expression.replace("<","&lt;");
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getLineCommand() {
		if (lineCommand == null){
			return "-";
		} else {
		    return lineCommand.toString();
		}
	}
	public void setLineCommand(Integer lineCommand) {
		this.lineCommand = lineCommand;
	}
}