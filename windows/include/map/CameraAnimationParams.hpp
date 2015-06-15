/**
 * TitaniumKit CameraAnimationParams
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef _MAP_CAMERAANIMATIONPARAMS_HPP_
#define _MAP_CAMERAANIMATIONPARAMS_HPP_

#include "Map/Camera.hpp"

#include "Titanium/UI/Constants.hpp"
#include "Titanium/detail/TiBase.hpp"

namespace Ti
{
	namespace Map
	{
		using namespace HAL;

		/*!
		  @struct
		  @discussion Simple object used to control camera animations.
		  This is an abstract type. Any object meeting this description can be used where this type is used.
		  See http://docs.appcelerator.com/titanium/3.0/#!/api/CameraAnimationParams 
		*/
		struct CameraAnimationParams {
			std::shared_ptr<Camera> camera;
			Titanium::UI::ANIMATION_CURVE curve;
			std::chrono::milliseconds duration;
		};
		
		CameraAnimationParams js_to_CameraAnimationParams(const JSObject& object);
		JSObject CameraAnimationParams_to_js(const JSContext& js_context, CameraAnimationParams value);
		
	} // namespace Map
} // namespace Titanium
#endif // _TITANIUM_MAP_CAMERAANIMATIONPARAMS_HPP_
