/**
 * TitaniumKit MapLocationTypev2
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef _MAP_MAPLOCATIONTYPEV2_HPP_
#define _MAP_MAPLOCATIONTYPEV2_HPP_

#include "Titanium/detail/TiBase.hpp"

namespace Ti
{
	namespace Map
	{
		using namespace HAL;

		/*!
		  @struct
		  @discussion Simple object representing a map location and zoom level.
		  This is an abstract type. Any object meeting this description can be used where this type is used.
		  See http://docs.appcelerator.com/titanium/3.0/#!/api/MapLocationTypev2 
		*/
		struct MapLocationTypev2 {
			bool   animate;
			double latitude;
			double latitudeDelta;
			double longitude;
			double longitudeDelta;
		};
		
		MapLocationTypev2 js_to_MapLocationTypev2(const JSObject& object);
		JSObject MapLocationTypev2_to_js(const JSContext& js_context, MapLocationTypev2 value);
		
	} // namespace Map
} // namespace Titanium
#endif // _TITANIUM_MAP_MAPLOCATIONTYPEV2_HPP_
