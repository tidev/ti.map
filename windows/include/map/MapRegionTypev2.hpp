/**
 * TitaniumKit MapRegionTypev2
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef _MAP_MAPREGIONTYPEV2_HPP_
#define _MAP_MAPREGIONTYPEV2_HPP_

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
		  See http://docs.appcelerator.com/titanium/3.0/#!/api/MapRegionTypev2 
		*/
		struct MapRegionTypev2 {
			double bearing;
			double latitude;
			double latitudeDelta;
			double longitude;
			double longitudeDelta;
			double tilt;
			uint32_t zoom;
		};
		
		MapRegionTypev2 js_to_MapRegionTypev2(const JSObject& object);
		JSObject MapRegionTypev2_to_js(const JSContext& js_context, MapRegionTypev2 value);
		
	} // namespace Map
} // namespace Titanium
#endif // _MAP_MAPREGIONTYPEV2_HPP_
