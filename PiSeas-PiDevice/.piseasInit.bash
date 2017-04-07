#~ Check if Java installed
if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
fi

#~ check version
if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    if [[ "$version" > "1.5" ]]; then
        echo version is more than 1.5
    else         
        echo version is less than 1.5
    fi
    if ! grep '#~ PISEAS' ~/.bashrc > /dev/null; then
		echo '#PISEAS: appending required paths to ~/.bashrc'
		_javaPath=$(find /usr/lib/jvm -type d -name "*jdk*")
		echo "export JAVA_HOME=$_javaPath" >> ~/.bashrc
		echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
		echo 'export LIBPATH=$JAVA_HOME/jre/lib/arm/server' >> ~/.bashrc
		echo 'export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:${LIBPATH}' >> ~/.bashrc
    fi
fi

#~ set varible for PiSeas config
_TANKID=x
_LIGHT_PIN=26
_FAN_PIN=19
_HEATER_PIN=13
_IN_PUMP_PIN=6
_OUT_PUMP_PIN=5
_WATER_LEVEL_SENSOR_PIN=16
#~ Piseas Config Init
if [ -f ~/.piseas.ini ] ; then
	echo "#PISEAS: Config file currently exists"
	cat ~/.piseas.ini 
else
	echo "#PISEAS: Config file does not exist; Creating"
	touch ~/.piseas.ini
	echo "TANKID=$_TANKID" > ~/.piseas.ini
	cat ~/.piseas.ini 
fi

