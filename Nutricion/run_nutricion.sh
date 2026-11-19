#!/bin/bash

# Ruta a tu JDK (ajústala si es diferente)
JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home"
JAVA_BIN="$JAVA_HOME/bin/java"

# Ruta a SWI-Prolog
SWIPL_HOME="/Applications/SWI-Prolog.app/Contents/Resources/swipl"
JPL_LIB="/Applications/SWI-Prolog.app/Contents/Plugins/swipl"
SWIPL_FRAMEWORKS="/Applications/SWI-Prolog.app/Contents/Frameworks"
JPL_JAR="$SWIPL_HOME/lib/jpl.jar"

# Ruta a tu proyecto compilado en IntelliJ
PROJECT_CLASSES="/Users/ernestou/Downloads/Nutricion/out/production/Nutricion"

# Clase principal Java
MAIN_CLASS="nutricion.Nutricion"

# Ejecutar el programa
"$JAVA_BIN" \
  -Djava.library.path="$JPL_LIB:$SWIPL_FRAMEWORKS" \
  -cp "$PROJECT_CLASSES:$JPL_JAR" \
  -Dfile.encoding=UTF-8 \
  -Dsun.stdout.encoding=UTF-8 \
  -Dsun.stderr.encoding=UTF-8 \
  -Duser.language=en \
  -Duser.country=US \
  -Duser.variant= \
  -Duser.timezone=UTC \
  -Duser.dir="$(pwd)" \
  -Duser.home="$HOME" \
  -Duser.name="$(whoami)" \
  -Dapple.awt.UIElement=true \
  -Dawt.useSystemAAFontSettings=on \
  -Dsun.java2d.opengl=true \
  -Dsun.java2d.noddraw=true \
  -Dsun.java2d.d3d=false \
  -Dsun.java2d.pmoffscreen=false \
  -Dsun.awt.noerasebackground=true \
  -Dsun.io.useCanonCaches=false \
  -Dfile.encoding.pkg=sun.io \
  -Dsun.jnu.encoding=UTF-8 \
  -Dcom.apple.mrj.application.apple.menu.about.name="Nutricion" \
  -Dapple.laf.useScreenMenuBar=true \
  -Dapple.awt.application.name="Nutricion" \
  -Dapple.awt.graphics.UseQuartz=true \
  -Dcom.apple.macos.use-file-dialog-packages=true \
  -Dcom.apple.macos.useScreenMenuBar=true \
  -Dsun.awt.enableExtraMouseButtons=true \
  -Dsun.awt.CausedFocusEvent.enabled=false \
  -Dsun.font.layoutengine=icu \
  -Dsun.arch.data.model=64 \
  -Dsun.boot.library.path="$JAVA_HOME/lib" \
  -Dsun.java.command="$MAIN_CLASS" \
  -Dsun.java.launcher=SUN_STANDARD \
  -Dsun.java.launcher.advocated=true \
  -Dsun.management.compiler="HotSpot 64-Bit Tiered Compilers" \
  -Djava.vendor="Eclipse Adoptium" \
  -Djava.version="21" \
  -Djava.runtime.version="21+35" \
  -Djava.vm.version="21+35" \
  -Djava.vm.name="OpenJDK 64-Bit Server VM" \
  -Djava.vm.specification.version="21" \
  -Djava.vm.specification.vendor="Oracle Corporation" \
  -Djava.vm.specification.name="Java Virtual Machine Specification" \
  -Djava.class.version="65.0" \
  -Dsun.boot.class.path="$JAVA_HOME/lib/modules" \
  -Djava.library.path="$JPL_LIB:$SWIPL_FRAMEWORKS" \
  -Dsun.java.command="$MAIN_CLASS" \
  -DSWI_HOME_DIR="$SWIPL_HOME" \
  -DDYLD_LIBRARY_PATH="$SWIPL_FRAMEWORKS" \
  "$MAIN_CLASS"