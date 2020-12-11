package org.flowgenerator.domain;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
public final class JarUtil {
	// metodo que extrai um recurso de um arquivo JAR
	public static byte[] extract(String nomeJar, String recurso) throws IOException {
		// abre arquivo JAR
		JarFile jar = new JarFile(nomeJar, false);
		// obtem a entrada desejada
		JarEntry je = jar.getJarEntry(recurso);
		if (je == null) {
			jar.close();
			throw new IOException("Entrada nao localizada: "+recurso);
		}
		int tam = (int)je.getSize(); // obtem o tamanho da entrada
		if (tam == -1) { // se tamanho indeterminado lanca excecao
			jar.close();
			throw new IOException("Tamanho indeterminado: "+recurso);
		}
		// abre stream de entrada para ler recurso
		BufferedInputStream in = new BufferedInputStream(jar.getInputStream(je));
		// declara e prepara array com tamanho da entrada
		byte dados[] = new byte[tam];
		// variaveis auxiliares
		int lidos = 0, bloco = 0;
		// efetua leitura (de uma vez se possivel)
		while((tam-lidos)>0){
			bloco = in.read(dados, lidos, tam-lidos);
			if (bloco == -1) {break;}
			lidos += bloco;
		}
		// fecha streams da entrada e do arquivo JAR
		in.close(); jar.close();
		return dados; // retorna dados extraidos do JAR
	}
}