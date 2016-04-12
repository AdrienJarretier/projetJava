package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.InflaterInputStream;

public final class FileReading {

	private FileReading() {
	}; // interdit l'instanciation de cette classe

	// FIXME: les méthodes, même statique commencent avec une minuscule
	public static Byte[] readFile(File f) throws FileNotFoundException, IOException {
		// Decompression des objets + stockage des octets dans un tableau
		FileInputStream fis = new FileInputStream(f);

		InflaterInputStream decompresser = new InflaterInputStream(fis);

		// les variables commence par des minuscule
		ArrayList<Byte> lectureFichier = new ArrayList<>();
		int caract;

		try {
			// FIXME: vous lisez bit à bit... c'ets beaucoup trop lent !
			// FIXME: la taille de la liste est réalouée dynamiquement à chaque
			// fois... c'est très lent !
			while ((caract = decompresser.read()) != -1) {
				lectureFichier.add((byte) caract);
			}
		} catch (IOException e) {
			// si l'exception est levée, le stream n'est pas fermé...
			throw new IOException("fichier " + f.getName() + " : " + e.getMessage());
		} finally {
			if (fis != null) {
				fis.close();
			}
		}

		return lectureFichier.toArray(new Byte[0]);
	}

	public static Byte[] inflate(FileInputStream fis) throws FileNotFoundException, IOException {
		// FIXME: le code est très très semblable à celui de readFile... avec
		// les mêmes erreurs...
		// ça pourrai être factorisé.

		InflaterInputStream decompresser = new InflaterInputStream(fis);

		ArrayList<Byte> LectureFichier = new ArrayList<>();
		int caract;

		try {
			while ((caract = decompresser.read()) != -1) {
				LectureFichier.add((byte) caract);
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}

		decompresser.close();

		return LectureFichier.toArray(new Byte[0]);

	}

	public static String stringValue(Byte[] inflated) {
		// FIXME: ne fonctionne pas avec les caractère unicode
		// (si ce n'était que ça...)

		// converti les octets du tableau en characteres
		StringBuilder content = new StringBuilder();

		int i = 0;
		char c;
		while (i < inflated.length) {

			c = (char) inflated[i].byteValue();
			content.append(c);
			i++;
		}

		return content.toString();

	}

	public static String toHex(Byte[] inflated) {

		StringBuilder content = new StringBuilder();

		int i = 0;

		while (i < inflated.length) {
			content.append(String.format("%02x", inflated[i]));
			i++;
		}

		return content.toString();
	}

	public static Byte[] removeHeading(Byte[] inflated) {

		int i = 0;
		char c;

		do {
			c = (char) inflated[i].byteValue();
			i++;
		} while (c != '\0');

		// FIXME: ho encore le même code...
		ArrayList<Byte> LectureFichier = new ArrayList<>();

		while (i < inflated.length) {
			LectureFichier.add(inflated[i]);
			i++;
		}

		return LectureFichier.toArray(new Byte[0]);

	}

}
