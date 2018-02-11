/**
 * TitaniumKit CameraAnimationParams
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "Map/CameraAnimationParams.hpp"

namespace Ti
{
	namespace Map
	{
		using namespace HAL;

		CameraAnimationParams js_to_CameraAnimationParams(const JSObject& object)
		{
			CameraAnimationParams value;

			if (object.HasProperty("camera")) {
				const auto js_camera = object.GetProperty("camera");
				if (js_camera.IsObject()) {
					value.camera = static_cast<JSObject>(js_camera).GetPrivate<Camera>();
				} else {
					TITANIUM_LOG_WARN("CameraAnimationParams.camera is not an object.");
				}
			}

			if (object.HasProperty("curve")) {
				auto curve = object.GetProperty("curve");
				if (curve.IsNumber()) {
					value.curve = Titanium::UI::Constants::to_ANIMATION_CURVE(static_cast<std::underlying_type<Titanium::UI::ANIMATION_CURVE>::type>(curve));
				} else {
					value.curve = Titanium::UI::Constants::to_ANIMATION_CURVE(static_cast<std::string>(curve));
				}
			}
			if (object.HasProperty("duration")) {
				value.duration = std::chrono::milliseconds(static_cast<std::chrono::milliseconds::rep>(static_cast<std::uint32_t>(object.GetProperty("duration"))));
			}
			return value;
		};

		JSObject CameraAnimationParams_to_js(const JSContext& js_context, CameraAnimationParams value)
		{
			auto object = js_context.CreateObject();
			object.SetProperty("camera", value.camera->get_object());
			object.SetProperty("curve",  js_context.CreateNumber(static_cast<std::underlying_type<Titanium::UI::ANIMATION_CURVE>::type>(value.curve)));
			object.SetProperty("duration", js_context.CreateNumber(static_cast<double>(value.duration.count())));
			return object;
		}
	} // namespace Map
} // namespace Titanium
