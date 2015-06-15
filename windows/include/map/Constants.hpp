/**
 * TitaniumKit Titanium.Map Constants
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef _MAP_CONSTANTS_HPP_
#define _MAP_CONSTANTS_HPP_

#include "timap_export.h"
#include "Titanium/detail/TiBase.hpp"
#include <unordered_set>
#include <functional>

namespace Ti
{
	namespace Map
	{
		/*!
		  @enum

		  @abstract These constants are for use with the
		  Titanium.Map.Map.mapType property.

		  @constant HYBRID Used with mapType to display a satellite image of the area with road and road name information layered on top.

		  @constant SATELLITE Used with mapType to display satellite imagery of the area.

		  @constant NORMAL Used with mapType to display a street map that shows the position of all roads and some road names.

		  @constant TERRAIN Used with mapType to display the terrain that shows the position of all roads and some road names.
		*/
		enum class TIMAP_EXPORT MAP_TYPE {
			HYBRID,
			SATELLITE,
			NORMAL,
			TERRAIN
		};

		/*!
		  @enum
		  
		  @constant ABOVE_LABELS Place the overlay above roadways but below map labels, shields, or point-of-interest icons. Available in iOS 7.0 and later.

		  @constant ABOVE_ROADS Place the overlay above map labels, shields, or point-of-interest icons but below annotations and 3D projections of buildings. Available in iOS 7.0 and later.
		*/
		enum class TIMAP_EXPORT OVERLAY_LEVEL {
			ABOVE_LABELS,
			ABOVE_ROADS
		};

		/*!
		  @enum

		  @abstract These constants are for use with the
		  Titanium.Map.Map pinchangedragstate event.

		  @constant END Used in the pinchangedragstate event to indicate that the user finished moving the annotation.

		  @constant START Used in the pinchangedragstate event to indicate that the user started dragging the annotation.
		*/
		enum class TIMAP_EXPORT ANNOTATION_DRAG_STATE {
			END,
			START
		};

		/*!
		  @enum

		  @abstract Color constant used to set a map annotation to green via the Modules.Map.Annotation.pincolor property.

		  @constant AZURE
		  @constant BLUE
		  @constant CYAN
		  @constant GREEN
		  @constant MAGENTA
		  @constant ORANGE
		  @constant PURPLE
		  @constant RED
		  @constant ROSE
		  @constant VIOLET
		  @constant YELLOW
		*/
		enum class TIMAP_EXPORT ANNOTATION_COLOR {
			AZURE,
			BLUE,
			CYAN,
			GREEN,
			MAGENTA,
			ORANGE,
			PURPLE,
			RED,
			ROSE,
			VIOLET,
			YELLOW
		};

		/*!
		  @enum

		  @abstract These constants are for use with the
		  Titanium.Map.isGooglePlayServicesAvailable method.

		  @constant DISABLED Google Play services has been disabled on this device.

		  @constant INVALID The version of Google Play services installed on this device is not authentic.

		  @constant MISSING Google Play services is not installed on the device.

		  @constant VERSION_UPDATE_REQUIRED Google Play services is out of date.

		  @constant SUCCESS Google Play services is available, and the connection is successful.
		*/
		enum class TIMAP_EXPORT GOOGLE_PLAY_SERVICE_STATE {
			DISABLED,
			INVALID,
			MISSING,
			VERSION_UPDATE_REQUIRED,
			SUCCESS
		};
	} // namespace Map
}  // namespace Titanium

// Provide a hash function so that a Titanium::Map::MAP_TYPE can be stored in an
// unordered container.
namespace std
{
	using Ti::Map::MAP_TYPE;
	template <>
	struct hash<MAP_TYPE>
	{
		using argument_type = MAP_TYPE;
		using result_type = std::size_t;
		using underlying_type = std::underlying_type<argument_type>::type;
		std::hash<underlying_type> hash_function = std::hash<underlying_type>();

		result_type operator()(const argument_type& property_attribute) const
		{
			return hash_function(static_cast<underlying_type>(property_attribute));
		}
	};

	using Ti::Map::OVERLAY_LEVEL;
	template <>
	struct hash<OVERLAY_LEVEL>
	{
		using argument_type = OVERLAY_LEVEL;
		using result_type = std::size_t;
		using underlying_type = std::underlying_type<argument_type>::type;
		std::hash<underlying_type> hash_function = std::hash<underlying_type>();

		result_type operator()(const argument_type& property_attribute) const
		{
			return hash_function(static_cast<underlying_type>(property_attribute));
		}
	};

	using Ti::Map::ANNOTATION_DRAG_STATE;
	template <>
	struct hash<ANNOTATION_DRAG_STATE>
	{
		using argument_type = ANNOTATION_DRAG_STATE;
		using result_type = std::size_t;
		using underlying_type = std::underlying_type<argument_type>::type;
		std::hash<underlying_type> hash_function = std::hash<underlying_type>();

		result_type operator()(const argument_type& property_attribute) const
		{
			return hash_function(static_cast<underlying_type>(property_attribute));
		}
	};

	using Ti::Map::ANNOTATION_COLOR;
	template <>
	struct hash<ANNOTATION_COLOR>
	{
		using argument_type = ANNOTATION_COLOR;
		using result_type = std::size_t;
		using underlying_type = std::underlying_type<argument_type>::type;
		std::hash<underlying_type> hash_function = std::hash<underlying_type>();

		result_type operator()(const argument_type& property_attribute) const
		{
			return hash_function(static_cast<underlying_type>(property_attribute));
		}
	};

	using Ti::Map::GOOGLE_PLAY_SERVICE_STATE;
	template <>
	struct hash<GOOGLE_PLAY_SERVICE_STATE>
	{
		using argument_type = GOOGLE_PLAY_SERVICE_STATE;
		using result_type = std::size_t;
		using underlying_type = std::underlying_type<argument_type>::type;
		std::hash<underlying_type> hash_function = std::hash<underlying_type>();

		result_type operator()(const argument_type& property_attribute) const
		{
			return hash_function(static_cast<underlying_type>(property_attribute));
		}
	};
}  // namespace std

namespace Ti
{
	namespace Map
	{
		class TIMAP_EXPORT Constants final
		{
		public:
			static std::string to_string(const MAP_TYPE&) TITANIUM_NOEXCEPT;
			static MAP_TYPE to_MAP_TYPE(const std::string& typeName) TITANIUM_NOEXCEPT;
			static MAP_TYPE to_MAP_TYPE(std::underlying_type<MAP_TYPE>::type) TITANIUM_NOEXCEPT;
			static std::underlying_type<MAP_TYPE>::type to_underlying_type(const MAP_TYPE&) TITANIUM_NOEXCEPT;

			static std::string to_string(const OVERLAY_LEVEL&) TITANIUM_NOEXCEPT;
			static OVERLAY_LEVEL to_OVERLAY_LEVEL(const std::string& overlayLevelName) TITANIUM_NOEXCEPT;
			static OVERLAY_LEVEL to_OVERLAY_LEVEL(std::underlying_type<OVERLAY_LEVEL>::type) TITANIUM_NOEXCEPT;
			static std::underlying_type<OVERLAY_LEVEL>::type to_underlying_type(const OVERLAY_LEVEL&) TITANIUM_NOEXCEPT;

			static std::string to_string(const ANNOTATION_DRAG_STATE&) TITANIUM_NOEXCEPT;
			static ANNOTATION_DRAG_STATE to_ANNOTATION_DRAG_STATE(const std::string& name) TITANIUM_NOEXCEPT;
			static ANNOTATION_DRAG_STATE to_ANNOTATION_DRAG_STATE(std::underlying_type<ANNOTATION_DRAG_STATE>::type) TITANIUM_NOEXCEPT;
			static std::underlying_type<ANNOTATION_DRAG_STATE>::type to_underlying_type(const ANNOTATION_DRAG_STATE&) TITANIUM_NOEXCEPT;

			static std::string to_string(const ANNOTATION_COLOR&) TITANIUM_NOEXCEPT;
			static ANNOTATION_COLOR to_ANNOTATION_COLOR(const std::string& name) TITANIUM_NOEXCEPT;
			static ANNOTATION_COLOR to_ANNOTATION_COLOR(std::underlying_type<ANNOTATION_COLOR>::type) TITANIUM_NOEXCEPT;
			static std::underlying_type<ANNOTATION_COLOR>::type to_underlying_type(const ANNOTATION_COLOR&) TITANIUM_NOEXCEPT;

			static std::string to_string(const GOOGLE_PLAY_SERVICE_STATE&) TITANIUM_NOEXCEPT;
			static GOOGLE_PLAY_SERVICE_STATE to_GOOGLE_PLAY_SERVICE_STATE(const std::string& name) TITANIUM_NOEXCEPT;
			static GOOGLE_PLAY_SERVICE_STATE to_GOOGLE_PLAY_SERVICE_STATE(std::underlying_type<GOOGLE_PLAY_SERVICE_STATE>::type) TITANIUM_NOEXCEPT;
			static std::underlying_type<GOOGLE_PLAY_SERVICE_STATE>::type to_underlying_type(const GOOGLE_PLAY_SERVICE_STATE&) TITANIUM_NOEXCEPT;
		};
	} // namespace Map
}  // namespace Titanium

#endif  // _TITANIUM_MAP_CONSTANTS_HPP_