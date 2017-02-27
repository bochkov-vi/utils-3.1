#!/usr/bin/env bash
java -cp /home/home/.m2/repository/com/h2database/h2/1.4.193/h2-1.4.193.jar -Duser.dir=/development/maven/larisa org.h2.tools.Server -tcp
