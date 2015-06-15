# FindLayoutEngine
# Author: Chris Williams
#
# Copyright (c) 2014 by Appcelerator, Inc. All Rights Reserved.
# Licensed under the terms of the Apache Public License.
# Please see the LICENSE included with this distribution for details.

# Author: Chris Williams
# Created: 2014.12.02

if("${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION}" LESS 2.5)
   message(FATAL_ERROR "CMake >= 2.6.0 required")
endif()
cmake_policy(PUSH)
cmake_policy(VERSION 2.6)
#----------------------------------------------------------------
# Generated CMake target import file.
#----------------------------------------------------------------

# Commands may need to know the format version.
set(CMAKE_IMPORT_FILE_VERSION 1)

# Protect against multiple inclusion, which would fail when already imported targets are added once more.
set(_targetsDefined)
set(_targetsNotDefined)
set(_expectedTargets)
foreach(_expectedTarget LayoutEngine)
  list(APPEND _expectedTargets ${_expectedTarget})
  if(NOT TARGET ${_expectedTarget})
    list(APPEND _targetsNotDefined ${_expectedTarget})
  endif()
  if(TARGET ${_expectedTarget})
    list(APPEND _targetsDefined ${_expectedTarget})
  endif()
endforeach()
if("${_targetsDefined}" STREQUAL "${_expectedTargets}")
  set(CMAKE_IMPORT_FILE_VERSION)
  cmake_policy(POP)
  return()
endif()
if(NOT "${_targetsDefined}" STREQUAL "")
  message(FATAL_ERROR "Some (but not all) targets in this export set were already defined.\nTargets Defined: ${_targetsDefined}\nTargets not yet defined: ${_targetsNotDefined}\n")
endif()
unset(_targetsDefined)
unset(_targetsNotDefined)
unset(_expectedTargets)


# Create imported target LayoutEngine

if(${CMAKE_SYSTEM_NAME} STREQUAL "WindowsPhone")
  set(PLATFORM phone)
elseif(${CMAKE_SYSTEM_NAME} STREQUAL "WindowsStore")
  set(PLATFORM store)
else()
  message(FATAL_ERROR "This app supports Store / Phone only.")
endif()

set(LayoutEngine_ARCH "x86")
if(CMAKE_GENERATOR MATCHES "^Visual Studio .+ ARM$")
  set(LayoutEngine_ARCH "arm")
endif()

# Create imported target LayoutEngine
add_library(LayoutEngine SHARED IMPORTED)

set_target_properties(LayoutEngine PROPERTIES
  COMPATIBLE_INTERFACE_STRING "LayoutEngine_MAJOR_VERSION"
  INTERFACE_INCLUDE_DIRECTORIES "${WINDOWS_SOURCE_DIR}/lib/LayoutEngine/include"
  INTERFACE_LayoutEngine_MAJOR_VERSION "0"
)
set_target_properties(LayoutEngine PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES "CXX"
  IMPORTED_IMPLIB "${WINDOWS_SOURCE_DIR}/lib/LayoutEngine/${PLATFORM}/${LayoutEngine_ARCH}/LayoutEngine.lib"
  )


# This file does not depend on other imported targets which have
# been exported from the same project but in a separate export set.

# Commands beyond this point should not need to know the version.
set(CMAKE_IMPORT_FILE_VERSION)
cmake_policy(POP)