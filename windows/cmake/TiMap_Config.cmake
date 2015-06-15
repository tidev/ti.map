# Titanium Windows Native Module - ti.map
#
# Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
# Licensed under the terms of the Apache Public License.
# Please see the LICENSE included with this distribution for details.

include(CMakeFindDependencyMacro)
find_dependency(HAL)
find_dependency(TitaniumKit)
find_dependency(LayoutEngine)
find_dependency(TitaniumWindows_UI)
find_dependency(TitaniumWindows_Utility)

include("${CMAKE_BINARY_DIR}/TitaniumWindows_TiMap_Targets.cmake")