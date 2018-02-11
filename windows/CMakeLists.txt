# Titanium Windows Native Module - ti.map
#
# Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
# Licensed under the terms of the Apache Public License.
# Please see the LICENSE included with this distribution for details.

cmake_minimum_required(VERSION 3.0.0)

if(${CMAKE_SYSTEM_NAME} STREQUAL "WindowsPhone")
  set(PLATFORM wp)
  add_definitions("-DPHONE")
elseif(${CMAKE_SYSTEM_NAME} STREQUAL "WindowsStore")
  set(PLATFORM store)
else()
  message(FATAL_ERROR "This app supports Store / Phone only.")
endif()

project(TiMap)

set(TiMap_VERSION 0.1.0)

set(WINDOWS_SOURCE_DIR "C:/ProgramData/Titanium/mobilesdk/win32/4.1.0.v20150605164428/windows")

SET(CMAKE_FIND_LIBRARY_PREFIXES "")
SET(CMAKE_FIND_LIBRARY_SUFFIXES ".lib" ".dll")

set(CMAKE_INCLUDE_CURRENT_DIR ON)
set(CMAKE_INCLUDE_CURRENT_DIR_IN_INTERFACE ON)

option(TiMap_DISABLE_TESTS "Disable compiling the tests" OFF)

set_property(GLOBAL PROPERTY USE_FOLDERS ON)

get_filename_component(APPCELERATOR_CMAKE_MODULE_PATH ${PROJECT_SOURCE_DIR}/cmake ABSOLUTE)
list(INSERT CMAKE_MODULE_PATH 0 ${APPCELERATOR_CMAKE_MODULE_PATH})

set(Boost_USE_STATIC_LIBS ON )
set(Boost_USE_MULTITHREADED ON )
set(Boost_USE_STATIC_RUNTIME OFF)
find_package(Boost 1.55.0 REQUIRED)

find_package(HAL REQUIRED)
find_package(TitaniumKit REQUIRED)
find_package(JavaScriptCore REQUIRED)

find_package(LayoutEngine REQUIRED)
find_package(TitaniumWindows_UI REQUIRED)
find_package(TitaniumWindows_Utility REQUIRED)

enable_testing()

set(SOURCE_TiMap
  include/TiMap.hpp
  src/TiMap.cpp
  include/map/Annotation.hpp
  src/Annotation.cpp
  include/map/View.hpp
  src/View.cpp
  include/map/MapLocationTypev2.hpp
  src/MapLocationTypev2.cpp
  include/map/MapRegionTypev2.hpp
  src/MapRegionTypev2.cpp
  include/map/Constants.hpp
  src/Constants.cpp
  include/map/Camera.hpp
  src/Camera.cpp
  include/map/CameraAnimationParams.hpp
  src/CameraAnimationParams.cpp
  include/map/Route.hpp
  src/Route.cpp
  )

source_group(TiMap FILES ${SOURCE_TiMap})

set(CMAKE_CXX_VISIBILITY_PRESET hidden)
set(CMAKE_VISIBILITY_INLINES_HIDDEN 1)

add_library(TiMap SHARED
  ${SOURCE_TiMap}
  )

include(GenerateExportHeader)
generate_export_header(TiMap)
target_compile_definitions(TiMap PRIVATE TiMap_EXPORTS)

target_include_directories(TiMap PUBLIC
  ${PROJECT_SOURCE_DIR}/include
  $<TARGET_PROPERTY:HAL,INTERFACE_INCLUDE_DIRECTORIES>
  $<TARGET_PROPERTY:TitaniumKit,INTERFACE_INCLUDE_DIRECTORIES>
  $<TARGET_PROPERTY:LayoutEngine,INTERFACE_INCLUDE_DIRECTORIES>
  $<TARGET_PROPERTY:TitaniumWindows_UI,INTERFACE_INCLUDE_DIRECTORIES>
  $<TARGET_PROPERTY:TitaniumWindows_Utility,INTERFACE_INCLUDE_DIRECTORIES>
  ${JavaScriptCore_INCLUDE_DIRS}
  ${Boost_INCLUDE_DIRS}
  )

target_link_libraries(TiMap
  ${Boost_LIBRARIES}
  HAL
  TitaniumKit
  TitaniumWindows_UI
  TitaniumWindows_Utility
  )

set_target_properties(TiMap PROPERTIES VS_WINRT_COMPONENT TRUE)

set_property(TARGET TiMap APPEND_STRING PROPERTY LINK_FLAGS_DEBUG "/OPT:NOREF /OPT:NOICF")

if (NOT TiMap_DISABLE_TESTS)
  add_subdirectory(test)
endif()

set_property(TARGET TiMap PROPERTY VERSION ${TiMap_VERSION})
set_property(TARGET TiMap PROPERTY SOVERSION 0)
set_property(TARGET TiMap PROPERTY INTERFACE_TiMap_MAJOR_VERSION 0)
set_property(TARGET TiMap APPEND PROPERTY
  COMPATIBLE_INTERFACE_STRING TiMap_MAJOR_VERSION
  )

install(TARGETS TiMap EXPORT TiMap_Targets
  LIBRARY DESTINATION lib
  ARCHIVE DESTINATION lib
  RUNTIME DESTINATION bin
  INCLUDES DESTINATION include
  )

export(EXPORT TiMap_Targets
  FILE "${CMAKE_BINARY_DIR}/TiMap_Targets.cmake"
)

configure_file(cmake/TiMap_Config.cmake
  "${CMAKE_BINARY_DIR}/TiMap_Config.cmake"
  COPYONLY
  )

include(CMakePackageConfigHelpers)
write_basic_package_version_file(
  "${CMAKE_BINARY_DIR}/TiMap_ConfigVersion.cmake"
  VERSION ${TiMap_VERSION}
  COMPATIBILITY AnyNewerVersion
  )

set_target_properties(TiMap PROPERTIES VS_WINRT_REFERENCES HAL)

export(PACKAGE TiMap)