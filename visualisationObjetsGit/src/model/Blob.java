package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Blob extends GitObject {

	enum fileType {
		IMAGE, TEXT, UNKNOWN
	}

	// le contenu est traité sans l'en-tete

	private Byte[] rawContent;
	private String stringContent;
	private fileType type;

	public Blob(File _file, Git _gitInstance) throws IOException {

		super(_file, _gitInstance);

		rawContent = null;
		stringContent = null;
		type = fileType.UNKNOWN;

	}

	public Blob(String _name, Git _gitInstance, int offset, Pack pack) throws IOException {

		super(_name, _gitInstance, offset, pack);

		rawContent = null;
		stringContent = null;
		type = fileType.UNKNOWN;

	}

	@Override
	protected void fill() throws IOException {

		if (!this.filled) {

			if (inPack) {

				this.rawContent = this.pack.getRawDatas(offsetInPack);

			} else {

				Byte[] contentWithHead = FileReading.readFile(this.getFile());
				rawContent = FileReading.removeHeading(contentWithHead);

			}

			stringContent = FileReading.toHex(rawContent);

			if (stringContent.startsWith("424d")) {
				// System.out.println("bmp");
				type = fileType.IMAGE;
			} else if (stringContent.startsWith("47494638")) {
				// System.out.println("gif");
				type = fileType.IMAGE;
			} else if (stringContent.startsWith("ffd8")) {
				// System.out.println("jpeg");
				type = fileType.IMAGE;
			} else if (stringContent.startsWith("89504E470D0A1A0A".toLowerCase())) {
				// System.out.println("png");
				type = fileType.IMAGE;
			} else {

				String textContent = FileReading.stringValue(rawContent);
				int countASCII = 0;

				for (int i = 0; i < textContent.length(); i++) {

					if (31 < (int) textContent.charAt(i) && (int) textContent.charAt(i) < 127) {

						countASCII++;

					}
				}

				// s'il y a plus des 2 tiers d'ascii
				// on peut considerer que c'est un fichier texte
				if (countASCII > textContent.length() * 2 / 3) {
					// System.out.println("text");
					type = fileType.TEXT;
					stringContent = textContent;
				} else {
					// System.out.println("unknown");
					type = fileType.UNKNOWN;
				}

			}

			this.filled = true;
		}
	}

	@Override
	public ArrayList<GitObjectProperty> getProperties() throws IOException {

		this.fill();

		ArrayList<GitObjectProperty> properties = new ArrayList<>();

		// String sContentNoHeading = stringContent.split("\0")[1];

		switch (this.type) {
		case IMAGE:
			properties.add(new GitObjectProperty("", GitObjectPropertyType.IMAGE, rawContent));
			break;

		case TEXT:
			properties.add(new GitObjectProperty("", GitObjectPropertyType.STRING_BLOC, stringContent));
			break;

		default:
			properties.add(new GitObjectProperty("", GitObjectPropertyType.UNKNOWN, stringContent));
			break;
		}

		return properties;
	}

}
