/**
 * Classe responsavel pelas informacoes do arquivos
 * @author Jeferson Ferreira
 * @since  12/11/2005
 */
package org.flowgenerator.controller;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.flowgenerator.gui.FlowGenerator;

public class FileData implements Comparable<Object> {
    private String fileName;
	private String fileType;
	private String filePath;
	private String fileLength;
	private String fileLastDate;
	private String fileFullPath;
	
	/** Creates a new instance of FileData */
	public FileData() {
	}
	public FileData(String fileName, String filePath) {
		this.fileName     = fileName;
		this.filePath     = filePath.replace("\\", "/");
		this.fileFullPath = this.filePath;
	}
	
	public FileData( String fileName, String filePath, String fileType ) {
		this(fileName,filePath);
        this.fileType = fileType;
	}
	
	public FileData(File file) {
		this(file.getName(),file.getPath());
        this.fileType = file.isFile() ? FlowGenerator.mlp.getString("file") : FlowGenerator.mlp.getString("directory");
        this.fileLength = NumberFormat.getInstance().format( file.length() / 1024 );
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
        this.fileLastDate = sdf.format( new Date(file.lastModified()));
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getFileLength() {
		return fileLength;
	}
	
	public void setFileLength(String fileLength) {
		this.fileLength = fileLength;
	}
	
	public void setFileLastDate(String fileLastDate) {
		this.fileLastDate = fileLastDate;
	}
	
	public String getInfoString () {
		StringBuffer sBuff = new StringBuffer("<html>");
		sBuff.append(FlowGenerator.mlp.getString("name")+" ").append(fileName).append("<br>");
		sBuff.append(FlowGenerator.mlp.getString("type")+" ").append(fileType).append("<br>");
		sBuff.append(FlowGenerator.mlp.getString("length")+" ").append(fileLength).append(" KB").append("<br>");
		sBuff.append(FlowGenerator.mlp.getString("lastupdate")+" ").append(fileLastDate);
		sBuff.append("</html>");
		return sBuff.toString();
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		this.fileFullPath = filePath;
	}
	
	public String getFileFullPath() {
		return fileFullPath;
	}
	public int compareTo(Object obj) {
	    FileData comp = (FileData) obj;
	    // Recupera a informacao se o arquivo e um diretorio ou arquivo
	    char thisTypeFile = this.getFileType().equals(FlowGenerator.mlp.getString("file")) ? '2' : '1';
	    char compTypeFile = comp.getFileType().equals(FlowGenerator.mlp.getString("file")) ? '2' : '1';
	    // Transforma para minusculo e concatena o tipo do arquivo 2=arquivo e 1=diretorio
	    // para que os diretorios sejam exibidos primeiro
	    String thisFileName = thisTypeFile+(this.getFileName()).toLowerCase();
	    String compFileName = compTypeFile+(comp.getFileName()).toLowerCase();
	    return thisFileName.compareTo(compFileName);
	}
}
