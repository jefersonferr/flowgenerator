package org.flowgenerator.gui;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.flowgenerator.domain.JarUtil;
public class MyIcon extends ImageIcon {
	private static final long serialVersionUID = 1L;
	private ImageIcon icone;
	public ImageIcon getIcone() {
		return icone;
	}
	public MyIcon(String nameIcone) {
		try {
		   if (FlowGenerator.getArqJar()==null) {
			   String fileIcon = "./src/main/resources/icones/"+nameIcone;
			   this.icone = new ImageIcon(fileIcon);
		   } else {
			   byte data[] = JarUtil.extract(FlowGenerator.getArqJar(),nameIcone);
		       this.icone = new ImageIcon(data);
		   }
        } catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					FlowGenerator.mlp.getString("error_icon")+" "+nameIcone+" - "+e,
					"FlowGenerator", JOptionPane.ERROR_MESSAGE);
		}
	}
}
