/**
 * TitaniumKit Titanium.Map.Camera
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "Map/Camera.hpp"

namespace Ti
{
	namespace Map
	{
		Camera::Camera(const JSContext& js_context) TITANIUM_NOEXCEPT
			: Titanium::Module(js_context)
		{
		}

		void Camera::JSExportInitialize() {
			JSExport<Camera>::SetClassVersion(1);
			JSExport<Camera>::SetParent(JSExport<Module>::Class());
		}
	} // namespace Map
} // namespace Titanium