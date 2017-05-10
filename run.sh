#!/bin/bash

ulimit -n 20000 > /dev/null 2>&1



CURRENT_DIR=`pwd`
SERVICE_NAME="$CURRENT_DIR/PetStore-0.0.1-SNAPSHOT"
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
PATHTOJAR="$DIR/PetStore-0.0.1-SNAPSHOT.jar"
JAVAEXEC="/opt/deploy/java/jdk1.8.0_73/bin/java"

# Minimal version to run the application
MINIMAL_VERSION=1.8.0

# Check if Java is present and the minimal version requirement
_java=`type $JAVAEXEC | awk '{ print $ NF }'`
CURRENT_VERSION=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
minimal_version=$(echo $MINIMAL_VERSION | awk -F'.' '{ print $2 }')
current_version=$(echo $CURRENT_VERSION | awk -F'.' '{ print $2 }')
if [ $current_version ]; then
        if [ $current_version -lt $minimal_version ]; then
                 echo "Error: Java version is too low to run $SERVICE_NAME. Needs at least Java >= ${MINIMAL_VERSION}."
                 exit 1
        fi
    else
         echo "Not able to find Java executable or version. Please check your Java installation."
         exit 1
fi

# This is the base heap size -- you may increase or decrease it to fit your
# system's memory availablity:
HEAP="-Xms1024m -Xmx1024m"

ARGS="$HEAP"

ARGS="$HEAP"

ARGUMENTS=( $@ )
ARGLEN=${#ARGUMENTS[@]}
JAVASYSTEMPROPARGS=${ARGUMENTS[@]:1:$ARGLEN}


getPID()
{
    re='^[0-9]+$'
    #get the process id of any running instance
    PID=$(pgrep -f $SERVICE_NAME);
}

start()
{
        echo "Starting...and remove every log files"
        rm -rf ../logs/*
    getPID
    echo "Starting $SERVICE_NAME ...";
    if ! [[ $PID =~ $re ]] ;  then
        nohup $JAVAEXEC $ARGS $JAVASYSTEMPROPARGS -jar $PATHTOJAR --logging.config=../conf/log4j2.xml --spring.config.location=file:../conf/ 2>> /dev/null >> /dev/null &
        echo "$SERVICE_NAME started ..."
    else
        echo "$SERVICE_NAME is already running ..."
    fi
}

stop()
{
    getPID
    if [[ $PID =~ $re ]];  then
        echo "$SERVICE_NAME stoping process id $PID"
        kill -9 $PID;
        echo "$SERVICE_NAME stopped ..."
    else
        echo "$SERVICE_NAME is not running ..."
    fi
}

restart()
{
    getPID
    echo "$SERVICE_NAME Restarting.."

    stop

    echo "Waiting 3 sec.."
    sleep 3

    start
}
debug()
{
    getPID
    echo "Debuging $SERVICE_NAME ...";
    if ! [[ $PID =~ $re ]] ;  then
        nohup $JAVAEXEC $ARGS $JAVASYSTEMPROPARGS -Dcom.sun.management.jmxremote.port=7091 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -agentlib:jdwp=tr
        echo "$SERVICE_NAME in debuging..."
    else
        echo "$SERVICE_NAME is already running ..."
    fi
}


case $1 in
    debug)
        debug;
    ;;
    start)
        start;
    ;;
    stop)
        stop;
    ;;
    restart)
        restart;
    ;;
    *)
        echo $"Usage: $0 (start|stop|restart|debug)"
        exit 1
esac


