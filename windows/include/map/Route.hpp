/**
 * TitaniumKit Titanium.Map.Route
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef _MAP_ROUTE_HPP_
#define _MAP_ROUTE_HPP_

#include "timap_export.h"
#include "Titanium/Module.hpp"

namespace Ti
{
	namespace Map
	{

		using namespace HAL;

		/*!
		  @class
		  @discussion This is the Titanium Route Module.
		  See http://docs.appcelerator.com/titanium/latest/#!/api/Modules.Map.Route
		*/
		class TIMAP_EXPORT Route : public Titanium::Module, public JSExport<Route>
		{

		public:

			Route(const JSContext&) TITANIUM_NOEXCEPT;

			virtual ~Route() = default;
			Route(const Route&) = default;
			Route& operator=(const Route&) = default;
#ifdef TITANIUM_MOVE_CTOR_AND_ASSIGN_DEFAULT_ENABLE
			Route(Route&&)                 = default;
			Route& operator=(Route&&)      = default;
#endif

			static void JSExportInitialize();

			protected:
#pragma warning(push)
#pragma warning(disable : 4251)
				
#pragma warning(pop)
		};

	} // namespace Map
} // namespace Titanium
#endif // _TITANIUM_MAP_ROUTE_HPP_