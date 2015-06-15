/**
 * TitaniumKit MapLocationTypev2
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "Map/MapLocationTypev2.hpp"

namespace Ti
{
	namespace Map
	{
		using namespace HAL;

		MapLocationTypev2 js_to_MapLocationTypev2(const JSObject& object)
		{
			MapLocationTypev2 value { false, 0, 0, 0, 0 };
			if (object.HasProperty("animate")) {
				value.animate = static_cast<bool>(object.GetProperty("animate"));
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
			return value;
		};

		JSObject MapLocationTypev2_to_js(const JSContext& js_context, MapLocationTypev2 value)
		{
			auto object = js_context.CreateObject();
			object.SetProperty("animate", js_context.CreateNumber(value.animate));
			object.SetProperty("latitude", js_context.CreateNumber(value.latitude));
			object.SetProperty("latitudeDelta", js_context.CreateNumber(value.latitudeDelta));
			object.SetProperty("longitude", js_context.CreateNumber(value.longitude));
			object.SetProperty("longitudeDelta", js_context.CreateNumber(value.longitudeDelta));
			return object;
		}
	} // namespace Map
} // namespace Titanium
