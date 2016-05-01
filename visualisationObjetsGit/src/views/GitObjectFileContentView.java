package views;

import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.TextArea;
import model.Git;

public class GitObjectFileContentView extends TextArea implements Observer {

	private Git model;

	public GitObjectFileContentView(Git model) {

		super();

		model.addObserver(this);

		this.model = model;

		// texteArea non editable
		setEditable(false);
	}

	@Override
	public void update(Observable o, Object arg) {

		if (model.getSelectObject() == null) {
			setText("selectionnez un objet dans la liste sur la gauche.");
		} else {
			setText(model.getSelectObject().toString());
		}
	}

}