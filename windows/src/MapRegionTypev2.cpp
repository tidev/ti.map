/**
 * TitaniumKit MapRegionTypev2
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "Map/MapRegionTypev2.hpp"

namespace Ti
{
	namespace Map
	{
		using namespace HAL;

		MapRegionTypev2 js_to_MapRegionTypev2(const JSObject& object)
		{
			MapRegionTypev2 value { 0, 0, 0, 0, 0, 0, 0 };
			if (object.HasProperty("bearing")) {
				value.bearing = static_cast<double>(object.GetProperty("bearing"));
			}
			if (object.HasProperty("latitude")) {
				value.latitude = static_cast<double>(object.GetProperty("latitude"));
			}
			if (object.HasProperty("latitudeDelta")) {
				value.latitudeDelta = static_cast<double>(object.GetProperty("latitudeDelta"));
			}
			if (object.HasProperty("longitude")) {
				value.longitude = static_cast<double>(object.GetProperty("longitude"));
			}
			if (object.HasProperty("longitudeDelta")) {
				value.longitudeDelta = static_cast<double>(object.GetProperty("longitudeDelta"));
			}
			if (object.HasProperty("tilt")) {
				value.tilt = static_cast<double>(object.GetProperty("tilt"));
			}
			if (object.HasProperty("zoom")) {
				value.zoom = static_cast<uint32_t>(object.GetProperty("zoom"));
			}
			return value;
		};

		JSObject MapRegionTypev2_to_js(const JSContext& js_context, MapRegionTypev2 value)
		{
			auto object = js_context.CreateObject();
			object.SetProperty("bearing", js_context.CreateNumber(value.bearing));
			object.SetProperty("latitude", js_context.CreateNumber(value.latitude));
			object.SetProperty("latitudeDelta", js_context.CreateNumber(value.latitudeDelta));
			object.SetProperty("longitude", js_context.CreateNumber(value.longitude));
			object.SetProperty("longitudeDelta", js_context.CreateNumber(value.longitudeDelta));
			object.SetProperty("tilt", js_context.CreateNumber(value.tilt));
			object.SetProperty("zoom", js_context.CreateNumber(value.zoom));
			return object;
		}
	} // namespace Map
} // namespace Titanium
