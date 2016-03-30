package views;

import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.TextArea;

public class GitObjectFileContentView extends TextArea implements Observer{
    
    public GitObjectFileContentView(){
        
        super();
        
        //texteArea non editable
        setEditable(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        setText("");
    }

}