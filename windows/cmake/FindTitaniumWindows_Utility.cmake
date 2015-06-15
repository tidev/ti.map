# FindTitaniumWindows_Utility
# Author: Chris Williams
#
# Copyright (c) 2014 by Appcelerator, Inc. All Rights Reserved.
# Licensed under the terms of the Apache Public License.
# Please see the LICENSE included with this distribution for details.

# Author: Chris Williams
# Created: 2014.12.02

if(${CMAKE_SYSTEM_NAME} STREQUAL "WindowsPhone")
  set(PLATFORM phone)
elseif(${CMAKE_SYSTEM_NAME} STREQUAL "WindowsStore")
  set(PLATFORM store)
else()
  message(FATAL_ERROR "This app supports Store / Phone only.")
endif()

set(TitaniumWindows_Utility_ARCH "x86")
if(CMAKE_GENERATOR MATCHES "^Visual Studio .+ ARM$")
  set(TitaniumWindows_Utility_ARCH "arm")
endif()

# Taken and slightly modified from bUtilityld's TitaniumWindows_Utility_Targets.cmake file
# INTERFACE_INCLUDE_DIRECTORIES is modified to point to our pre-packaged include dir for module

# Create imported target TitaniumWindows_Utility
add_library(TitaniumWindows_Utility SHARED IMPORTED)

set_target_properties(TitaniumWindows_Utility PROPERTIES
  COMPATIBLE_INTERFACE_STRING "TitaniumWindows_Utility_MAJOR_VERSION"
  INTERFACE_INCLUDE_DIRECTORIES "${WINDOWS_SOURCE_DIR}/lib/TitaniumWindows_Utility/include;$<TARGET_PROPERTY:TitaniumKit,INTERFACE_INCLUDE_DIRECTORIES>"
  INTERFACE_LINK_LIBRARIES "TitaniumKit"
  INTERFACE_TitaniumWindows_Utility_MAJOR_VERSION "0"
)

set_target_properties(TitaniumWindows_Utility PROPERTIES
  IMPORTED_IMPLIB "${WINDOWS_SOURCE_DIR}/lib/TitaniumWindows_Utility/${PLATFORM}/${TitaniumWindows_Utility_ARCH}/TitaniumWindows_Utility.lib"
  IMPORTED_LOCATION "${WINDOWS_SOURCE_DIR}/lib/TitaniumWindows_Utility/${PLATFORM}/${TitaniumWindows_Utility_ARCH}/TitaniumWindows_Utility.dll"
  )
