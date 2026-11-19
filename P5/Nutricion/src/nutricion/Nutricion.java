package nutricion;

import javax.swing.*;

public class Nutricion {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIFrame frame = new GUIFrame();
            frame.setVisible(true);
        });
    }
}

/*          IMPORTANT
*  FOR COMPILING USE:
*  javac -cp .:/Applications/SWI-Prolog.app/Contents/swipl/lib/jpl.jar Main.java
*
*  AND FOR RUNNING USE:
*  java -cp .:/Applications/SWI-Prolog.app/Contents/swipl/lib/jpl.jar \
     -Djava.library.path=/Applications/SWI-Prolog.app/Contents/swipl/lib/fat-darwin \
     Main
*
*  PATHS:
*  jpl.jar:          /Applications/SWI-Prolog.app/Contents/Resources/swipl/lib/jpl.jar
*  libjpl.dylib:     /Applications/SWI-Prolog.app/Contents/Plugins/swipl/libjpl.dylib
*
*  REMEMBER TO ADD jpl.jar TO THE PROJECT IN DEPENDENCIES->JARs or directories
*
*  REMEMBER TO SET libjpl.dylib AS A VM OPTION IN RUN->EDIT CONFIGURATIONS
*  -Djava.library.path=/Applications/SWI-Prolog.app/Contents/Plugins/swipl:/Applications/SWI-Prolog.app/Contents/Frameworks
*
*  ADD TO ENVIROMENT VARIABLES
*  DYLD_LIBRARY_PATH=/Applications/SWI-Prolog.app/Contents/Frameworks
*  SWI_HOME_DIR=/Applications/SWI-Prolog.app/Contents/Resources/swipl
* */