/**
 * TitaniumKit Titanium.Map.Route
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "Map/Route.hpp"

namespace Ti
{
	namespace Map
	{
		Route::Route(const JSContext& js_context) TITANIUM_NOEXCEPT
			: Titanium::Module(js_context)
		{
		}

		void Route::JSExportInitialize() {
			JSExport<Route>::SetClassVersion(1);
			JSExport<Route>::SetParent(JSExport<Module>::Class());
		}
	} // namespace Map
} // namespace Titanium