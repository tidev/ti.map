/**
 * TitaniumKit
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "Map/Constants.hpp"
#include <unordered_map>
#include <mutex>

namespace Ti
{
	namespace Map
	{
		std::string Constants::to_string(const MAP_TYPE& type) TITANIUM_NOEXCEPT
		{
			static std::string unknown_string = "MAP_TYPE::Unknown";
			static std::unordered_map<MAP_TYPE, std::string> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[MAP_TYPE::HYBRID] = "HYBRID_TYPE";
				map[MAP_TYPE::SATELLITE] = "SATELLITE_TYPE";
				map[MAP_TYPE::NORMAL] = "NORMAL_TYPE";
				map[MAP_TYPE::TERRAIN] = "TERRAIN_TYPE";
			});

			std::string string = unknown_string;
			const auto position = map.find(type);
			if (position != map.end()) {
				string = position->second;
			}

			return string;
		}

		MAP_TYPE Constants::to_MAP_TYPE(const std::string& typeName) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::string, MAP_TYPE> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map["HYBRID_TYPE"] = MAP_TYPE::HYBRID;
				map["SATELLITE_TYPE"] = MAP_TYPE::SATELLITE;
				map["NORMAL_TYPE"] = MAP_TYPE::NORMAL;
				map["TERRAIN_TYPE"] = MAP_TYPE::TERRAIN;
			});

			MAP_TYPE type = MAP_TYPE::NORMAL;
			const auto position = map.find(typeName);
			if (position != map.end()) {
				type = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_MAP_TYPE: Titanium::Map::MAP_TYPE with name '", typeName, "' does not exist");
			}

			return type;
		}

		MAP_TYPE Constants::to_MAP_TYPE(std::underlying_type<MAP_TYPE>::type type_underlying_type) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::underlying_type<MAP_TYPE>::type, MAP_TYPE> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[static_cast<std::underlying_type<MAP_TYPE>::type>(MAP_TYPE::HYBRID)] = MAP_TYPE::HYBRID;
				map[static_cast<std::underlying_type<MAP_TYPE>::type>(MAP_TYPE::SATELLITE)] = MAP_TYPE::SATELLITE;
				map[static_cast<std::underlying_type<MAP_TYPE>::type>(MAP_TYPE::NORMAL)] = MAP_TYPE::NORMAL;
				map[static_cast<std::underlying_type<MAP_TYPE>::type>(MAP_TYPE::TERRAIN)] = MAP_TYPE::TERRAIN;
			});

			MAP_TYPE type = MAP_TYPE::NORMAL;
			const auto position = map.find(type_underlying_type);
			if (position != map.end()) {
				type = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_MAP_TYPE: Titanium::Map::MAP_TYPE with value '", type_underlying_type, "' does not exist");
			}

			return type;
		}

		std::underlying_type<MAP_TYPE>::type Constants::to_underlying_type(const MAP_TYPE& mapType) TITANIUM_NOEXCEPT
		{
			return static_cast<std::underlying_type<MAP_TYPE>::type>(mapType);
		}

		std::string Constants::to_string(const OVERLAY_LEVEL& overlayLevel) TITANIUM_NOEXCEPT
		{
			static std::string unknown_string = "OVERLAY_LEVEL::Unknown";
			static std::unordered_map<OVERLAY_LEVEL, std::string> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[OVERLAY_LEVEL::ABOVE_LABELS]    = "OVERLAY_LEVEL_ABOVE_LABELS";
				map[OVERLAY_LEVEL::ABOVE_ROADS] 	= "OVERLAY_LEVEL_ABOVE_ROADS";
			});

			std::string string = unknown_string;
			const auto position = map.find(overlayLevel);
			if (position != map.end()) {
				string = position->second;
			}

			return string;
		}

		OVERLAY_LEVEL Constants::to_OVERLAY_LEVEL(const std::string& overlayLevelName) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::string, OVERLAY_LEVEL> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map["OVERLAY_LEVEL_ABOVE_LABELS"] = OVERLAY_LEVEL::ABOVE_LABELS;
				map["OVERLAY_LEVEL_ABOVE_ROADS"]  = OVERLAY_LEVEL::ABOVE_ROADS;
			});

			OVERLAY_LEVEL level = OVERLAY_LEVEL::ABOVE_LABELS;
			const auto position = map.find(overlayLevelName);
			if (position != map.end()) {
				level = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_OVERLAY_LEVEL: Titanium::Map::OVERLAY_LEVEL with name '", overlayLevelName, "' does not exist");
			}

			return level;
		}

		OVERLAY_LEVEL Constants::to_OVERLAY_LEVEL(std::underlying_type<OVERLAY_LEVEL>::type level_underlying_type) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::underlying_type<OVERLAY_LEVEL>::type, OVERLAY_LEVEL> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[static_cast<std::underlying_type<OVERLAY_LEVEL>::type>(OVERLAY_LEVEL::ABOVE_LABELS)] = OVERLAY_LEVEL::ABOVE_LABELS;
				map[static_cast<std::underlying_type<OVERLAY_LEVEL>::type>(OVERLAY_LEVEL::ABOVE_ROADS)]  = OVERLAY_LEVEL::ABOVE_ROADS;
			});

			OVERLAY_LEVEL level = OVERLAY_LEVEL::ABOVE_LABELS;
			const auto position = map.find(level_underlying_type);
			if (position != map.end()) {
				level = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_OVERLAY_LEVEL: Titanium::Map::OVERLAY_LEVEL with value '", level_underlying_type, "' does not exist");
			}

			return level;
		}

		std::underlying_type<OVERLAY_LEVEL>::type Constants::to_underlying_type(const OVERLAY_LEVEL& level) TITANIUM_NOEXCEPT
		{
			return static_cast<std::underlying_type<OVERLAY_LEVEL>::type>(level);
		}

		std::string Constants::to_string(const ANNOTATION_DRAG_STATE& state) TITANIUM_NOEXCEPT
		{
			static std::string unknown_string = "ANNOTATION_DRAG_STATE::Unknown";
			static std::unordered_map<ANNOTATION_DRAG_STATE, std::string> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[ANNOTATION_DRAG_STATE::END]   = "ANNOTATION_DRAG_STATE_END";
				map[ANNOTATION_DRAG_STATE::START] = "ANNOTATION_DRAG_STATE_START";
			});

			std::string string = unknown_string;
			const auto position = map.find(state);
			if (position != map.end()) {
				string = position->second;
			}

			return string;
		}

		ANNOTATION_DRAG_STATE Constants::to_ANNOTATION_DRAG_STATE(const std::string& name) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::string, ANNOTATION_DRAG_STATE> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map["ANNOTATION_DRAG_STATE_END"]   = ANNOTATION_DRAG_STATE::END;
				map["ANNOTATION_DRAG_STATE_START"] = ANNOTATION_DRAG_STATE::START;
			});

			ANNOTATION_DRAG_STATE state = ANNOTATION_DRAG_STATE::END;
			const auto position = map.find(name);
			if (position != map.end()) {
				state = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_ANNOTATION_DRAG_STATE: Titanium::Map::ANNOTATION_DRAG_STATE with name '", name, "' does not exist");
			}

			return state;
		}

		ANNOTATION_DRAG_STATE Constants::to_ANNOTATION_DRAG_STATE(std::underlying_type<ANNOTATION_DRAG_STATE>::type level_underlying_type) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::underlying_type<ANNOTATION_DRAG_STATE>::type, ANNOTATION_DRAG_STATE> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[static_cast<std::underlying_type<ANNOTATION_DRAG_STATE>::type>(ANNOTATION_DRAG_STATE::END)]   = ANNOTATION_DRAG_STATE::END;
				map[static_cast<std::underlying_type<ANNOTATION_DRAG_STATE>::type>(ANNOTATION_DRAG_STATE::START)] = ANNOTATION_DRAG_STATE::START;
			});

			ANNOTATION_DRAG_STATE state = ANNOTATION_DRAG_STATE::END;
			const auto position = map.find(level_underlying_type);
			if (position != map.end()) {
				state = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_ANNOTATION_DRAG_STATE: Titanium::Map::ANNOTATION_DRAG_STATE with value '", level_underlying_type, "' does not exist");
			}

			return state;
		}

		std::underlying_type<ANNOTATION_DRAG_STATE>::type Constants::to_underlying_type(const ANNOTATION_DRAG_STATE& state) TITANIUM_NOEXCEPT
		{
			return static_cast<std::underlying_type<ANNOTATION_DRAG_STATE>::type>(state);
		}

		std::string Constants::to_string(const ANNOTATION_COLOR& color) TITANIUM_NOEXCEPT
		{
			static std::string unknown_string = "ANNOTATION_COLOR::Unknown";
			static std::unordered_map<ANNOTATION_COLOR, std::string> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[ANNOTATION_COLOR::AZURE]   = "ANNOTATION_AZURE";
				map[ANNOTATION_COLOR::BLUE]    = "ANNOTATION_BLUE";
				map[ANNOTATION_COLOR::CYAN]    = "ANNOTATION_CYAN";
				map[ANNOTATION_COLOR::GREEN]   = "ANNOTATION_GREEN";
				map[ANNOTATION_COLOR::MAGENTA] = "ANNOTATION_MAGENTA";
				map[ANNOTATION_COLOR::ORANGE]  = "ANNOTATION_ORANGE";
				map[ANNOTATION_COLOR::PURPLE]  = "ANNOTATION_PURPLE";
				map[ANNOTATION_COLOR::RED]     = "ANNOTATION_RED";
				map[ANNOTATION_COLOR::ROSE]    = "ANNOTATION_ROSE";
				map[ANNOTATION_COLOR::VIOLET]  = "ANNOTATION_VIOLET";
				map[ANNOTATION_COLOR::YELLOW]  = "ANNOTATION_YELLOW";
			});

			std::string string = unknown_string;
			const auto position = map.find(color);
			if (position != map.end()) {
				string = position->second;
			}

			return string;
		}

		ANNOTATION_COLOR Constants::to_ANNOTATION_COLOR(const std::string& name) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::string, ANNOTATION_COLOR> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map["ANNOTATION_AZURE"]   = ANNOTATION_COLOR::AZURE;
				map["ANNOTATION_BLUE"]    = ANNOTATION_COLOR::BLUE;
				map["ANNOTATION_CYAN"]    = ANNOTATION_COLOR::CYAN;
				map["ANNOTATION_GREEN"]   = ANNOTATION_COLOR::GREEN;
				map["ANNOTATION_MAGENTA"] = ANNOTATION_COLOR::MAGENTA;
				map["ANNOTATION_ORANGE"]  = ANNOTATION_COLOR::ORANGE;
				map["ANNOTATION_PURPLE"]  = ANNOTATION_COLOR::PURPLE;
				map["ANNOTATION_RED"]     = ANNOTATION_COLOR::RED;
				map["ANNOTATION_ROSE"]    = ANNOTATION_COLOR::ROSE;
				map["ANNOTATION_VIOLET"]  = ANNOTATION_COLOR::VIOLET;
				map["ANNOTATION_YELLOW"]  = ANNOTATION_COLOR::YELLOW;
			});

			ANNOTATION_COLOR color = ANNOTATION_COLOR::AZURE;
			const auto position = map.find(name);
			if (position != map.end()) {
				color = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_ANNOTATION_COLOR: Titanium::Map::ANNOTATION_COLOR with name '", name, "' does not exist");
			}

			return color;
		}

		ANNOTATION_COLOR Constants::to_ANNOTATION_COLOR(std::underlying_type<ANNOTATION_COLOR>::type color_underlying_type) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::underlying_type<ANNOTATION_COLOR>::type, ANNOTATION_COLOR> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::AZURE)]   = ANNOTATION_COLOR::AZURE;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::BLUE)]    = ANNOTATION_COLOR::BLUE;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::CYAN)]    = ANNOTATION_COLOR::CYAN;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::GREEN)]   = ANNOTATION_COLOR::GREEN;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::MAGENTA)] = ANNOTATION_COLOR::MAGENTA;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::ORANGE)]  = ANNOTATION_COLOR::ORANGE;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::PURPLE)]  = ANNOTATION_COLOR::PURPLE;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::RED)]     = ANNOTATION_COLOR::RED;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::ROSE)]    = ANNOTATION_COLOR::ROSE;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::VIOLET)]  = ANNOTATION_COLOR::VIOLET;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(ANNOTATION_COLOR::YELLOW)]  = ANNOTATION_COLOR::YELLOW;
			});

			ANNOTATION_COLOR color = ANNOTATION_COLOR::AZURE;
			const auto position = map.find(color_underlying_type);
			if (position != map.end()) {
				color = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_ANNOTATION_COLOR: Titanium::Map::ANNOTATION_COLOR with value '", color_underlying_type, "' does not exist");
			}

			return color;
		}

		std::underlying_type<ANNOTATION_COLOR>::type Constants::to_underlying_type(const ANNOTATION_COLOR& color) TITANIUM_NOEXCEPT
		{
			return static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(color);
		}

		std::string Constants::to_string(const GOOGLE_PLAY_SERVICE_STATE& state) TITANIUM_NOEXCEPT
		{
			static std::string unknown_string = "GOOGLE_PLAY_SERVICE_STATE::Unknown";
			static std::unordered_map<GOOGLE_PLAY_SERVICE_STATE, std::string> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[GOOGLE_PLAY_SERVICE_STATE::DISABLED] 				= "SERVICE_DISABLED";
				map[GOOGLE_PLAY_SERVICE_STATE::INVALID] 				= "SERVICE_INVALID";
				map[GOOGLE_PLAY_SERVICE_STATE::MISSING] 				= "SERVICE_MISSING";
				map[GOOGLE_PLAY_SERVICE_STATE::VERSION_UPDATE_REQUIRED] = "SERVICE_VERSION_UPDATE_REQUIRED";
				map[GOOGLE_PLAY_SERVICE_STATE::SUCCESS] 				= "SUCCESS";
			});

			std::string string = unknown_string;
			const auto position = map.find(state);
			if (position != map.end()) {
				string = position->second;
			}

			return string;
		}

		GOOGLE_PLAY_SERVICE_STATE Constants::to_GOOGLE_PLAY_SERVICE_STATE(const std::string& name) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::string, GOOGLE_PLAY_SERVICE_STATE> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map["SERVICE_DISABLED"] 				= GOOGLE_PLAY_SERVICE_STATE::DISABLED;
				map["SERVICE_INVALID"] 					= GOOGLE_PLAY_SERVICE_STATE::INVALID;
				map["SERVICE_MISSING"] 					= GOOGLE_PLAY_SERVICE_STATE::MISSING;
				map["SERVICE_VERSION_UPDATE_REQUIRED"] 	= GOOGLE_PLAY_SERVICE_STATE::VERSION_UPDATE_REQUIRED;
				map["SUCCESS"] 							= GOOGLE_PLAY_SERVICE_STATE::SUCCESS;
			});

			GOOGLE_PLAY_SERVICE_STATE state = GOOGLE_PLAY_SERVICE_STATE::MISSING;
			const auto position = map.find(name);
			if (position != map.end()) {
				state = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_GOOGLE_PLAY_SERVICE_STATE: Titanium::Map::GOOGLE_PLAY_SERVICE_STATE with name '", name, "' does not exist");
			}

			return state;
		}

		GOOGLE_PLAY_SERVICE_STATE Constants::to_GOOGLE_PLAY_SERVICE_STATE(std::underlying_type<GOOGLE_PLAY_SERVICE_STATE>::type state_underlying_type) TITANIUM_NOEXCEPT
		{
			static std::unordered_map<std::underlying_type<GOOGLE_PLAY_SERVICE_STATE>::type, GOOGLE_PLAY_SERVICE_STATE> map;
			static std::once_flag of;
			std::call_once(of, []() {
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(GOOGLE_PLAY_SERVICE_STATE::DISABLED)] 				= GOOGLE_PLAY_SERVICE_STATE::DISABLED;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(GOOGLE_PLAY_SERVICE_STATE::INVALID)] 					= GOOGLE_PLAY_SERVICE_STATE::INVALID;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(GOOGLE_PLAY_SERVICE_STATE::MISSING)] 					= GOOGLE_PLAY_SERVICE_STATE::MISSING;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(GOOGLE_PLAY_SERVICE_STATE::VERSION_UPDATE_REQUIRED)]  = GOOGLE_PLAY_SERVICE_STATE::VERSION_UPDATE_REQUIRED;
				map[static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(GOOGLE_PLAY_SERVICE_STATE::SUCCESS)] 					= GOOGLE_PLAY_SERVICE_STATE::SUCCESS;
			});

			GOOGLE_PLAY_SERVICE_STATE state = GOOGLE_PLAY_SERVICE_STATE::MISSING;
			const auto position = map.find(state_underlying_type);
			if (position != map.end()) {
				state = position->second;
			} else {
				TITANIUM_LOG_WARN("Constants::to_GOOGLE_PLAY_SERVICE_STATE: Titanium::Map::GOOGLE_PLAY_SERVICE_STATE with value '", state_underlying_type, "' does not exist");
			}

			return state;
		}

		std::underlying_type<GOOGLE_PLAY_SERVICE_STATE>::type Constants::to_underlying_type(const GOOGLE_PLAY_SERVICE_STATE& state) TITANIUM_NOEXCEPT
		{
			return static_cast<std::underlying_type<GOOGLE_PLAY_SERVICE_STATE>::type>(state);
		}
	} // namespace Map
}  // namespace Titanium