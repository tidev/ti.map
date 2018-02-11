#!/usr/bin/env bash

# Titanium Windows Native Module - ti.map
#
# Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
# Licensed under the terms of the Apache Public License.
# Please see the LICENSE included with this distribution for details.

if ! test -d "${GTEST_ROOT}"; then
    echo "GTEST_ROOT must point to your Google Test installation."
    exit 1
fi

declare -rx VERBOSE=1

declare -r TitaniumWindows_TiMap_DISABLE_TESTS="OFF"

#declare -r target_platform="WindowsStore"
declare -r target_platform="WindowsPhone"
declare -r target_architecture="Win32"
declare -r target_version="8.1"
declare -r cmake_generator_name="Visual Studio 12 2013"

declare -r project_name=$(grep -E '^\s*project[(][^)]+[)]\s*$' CMakeLists.txt | awk 'BEGIN {FS="[()]"} {printf "%s", $2}')
declare -r solution_file_name="${project_name}.sln"
declare -r MSBUILD_PATH="c:/Program Files (x86)/MSBuild/12.0/Bin/MSBuild.exe"
declare -r BUILD_DIR="build"

function echo_and_eval {
    local -r cmd="${1:?}"
    echo "${cmd}" && eval "${cmd}"
}

cmd+="cmake"
cmd+=" -G \"${cmake_generator_name}\""
cmd+=" -DCMAKE_SYSTEM_NAME=${target_platform}"
cmd+=" -DCMAKE_SYSTEM_VERSION=${target_version}"
cmd+=" -DTitaniumWindows_TiMap_DISABLE_TESTS=${TitaniumWindows_TiMap_DISABLE_TESTS}"
cmd+=" -DJavaScriptCoreCPP_DISABLE_TESTS=ON"
cmd+=" -DTitaniumKit_DISABLE_TESTS=ON"
cmd+=" ../"

echo_and_eval "rm -rf \"${BUILD_DIR}\""
echo_and_eval "mkdir -p \"${BUILD_DIR}\""
echo_and_eval "pushd \"${BUILD_DIR}\""
echo_and_eval "${cmd}"
echo_and_eval "\"${MSBUILD_PATH}\" ${solution_file_name}"

if [ "${TitaniumWindows_TiMap_DISABLE_TESTS}" != "ON" ]; then
    echo_and_eval "ctest -VV --output-on-failure"
fi

echo_and_eval "popd"