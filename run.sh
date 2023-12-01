#!/bin/sh

app_platform=${APP_PLATFORM:-$(echo $0)}
echo "Running on "$app_platform

app_data_root=""
if test $app_platform = "docker"
then
    app_data_root="/data"
else
    app_data_root="data"
fi

# These variables are used in the application
export app_data_root
export app_data_inputs=$app_data_root"/inputs"
export app_data_outputs=$app_data_root"/outputs"

echo "Reading from "$app_data_inputs
echo "Writing to "$app_data_outputs

# java -cp FIRM2.jar uk.ac.ncl.nclwater.firm2.firm2.Firm2
java -cp target/FIRM2.jar uk.ac.ncl.nclwater.firm2.DAFNITest.DAFNITest

cp -r $app_data_inputs/* $app_data_outputs/