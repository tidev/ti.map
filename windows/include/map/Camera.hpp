/**
 * TitaniumKit Titanium.Map.Camera
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef _MAP_CAMERA_HPP_
#define _MAP_CAMERA_HPP_

#include "timap_export.h"
#include "Titanium/Module.hpp"

namespace Ti
{
	namespace Map
	{
		using namespace HAL;

		/*!
		  @class
		  @discussion This is the Titanium Camera Module.
		  See http://docs.appcelerator.com/titanium/latest/#!/api/Modules.Map.Camera
		*/
		class TIMAP_EXPORT Camera : public Titanium::Module, public JSExport<Camera>
		{

		public:

			Camera(const JSContext&) TITANIUM_NOEXCEPT;

			virtual ~Camera() = default;
			Camera(const Camera&) = default;
			Camera& operator=(const Camera&) = default;
#ifdef TITANIUM_MOVE_CTOR_AND_ASSIGN_DEFAULT_ENABLE
			Camera(Camera&&)                 = default;
			Camera& operator=(Camera&&)      = default;
#endif

			static void JSExportInitialize();

			protected:
#pragma warning(push)
#pragma warning(disable : 4251)
				
#pragma warning(pop)
		};

	} // namespace Map
} // namespace Titanium
#endif // _TITANIUM_MAP_CAMERA_HPP_