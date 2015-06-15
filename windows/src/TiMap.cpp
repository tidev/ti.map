/**
 * ti.map for Windows
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "TiMap.hpp"

namespace Ti
{
	MapModule::MapModule(const JSContext& js_context) TITANIUM_NOEXCEPT
		: Titanium::Module(js_context),
		hybrid_type__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::MAP_TYPE::HYBRID))),
		satellite_type__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::MAP_TYPE::SATELLITE))),
		normal_type__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::MAP_TYPE::NORMAL))),
		terrain_type__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::MAP_TYPE::TERRAIN))),
		annotation_drag_state_end__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_DRAG_STATE::END))),
		annotation_drag_state_start__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_DRAG_STATE::START))),
		annotation_azure__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::AZURE))),
		annotation_blue__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::BLUE))),
		annotation_cyan__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::CYAN))),
		annotation_green__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::GREEN))),
		annotation_magenta__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::MAGENTA))),
		annotation_orange__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::ORANGE))),
		annotation_purple__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::PURPLE))),
		annotation_red__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::RED))),
		annotation_rose__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::ROSE))),
		annotation_violet__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::VIOLET))),
		annotation_yellow__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::ANNOTATION_COLOR::YELLOW))),
		overlay_level_above_labels__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::OVERLAY_LEVEL::ABOVE_LABELS))),
		overlay_level_above_roads__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::OVERLAY_LEVEL::ABOVE_ROADS))),
		service_disabled__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::GOOGLE_PLAY_SERVICE_STATE::DISABLED))),
		service_invalid__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::GOOGLE_PLAY_SERVICE_STATE::INVALID))),
		service_missing__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::GOOGLE_PLAY_SERVICE_STATE::MISSING))),
		service_version_update_required__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::GOOGLE_PLAY_SERVICE_STATE::VERSION_UPDATE_REQUIRED))),
		success__(js_context.CreateNumber(Map::Constants::to_underlying_type(Map::GOOGLE_PLAY_SERVICE_STATE::SUCCESS)))
	{
		TITANIUM_LOG_DEBUG("Map::ctor Initialize");
	}

	void MapModule::postInitialize(JSObject& js_object)
	{
	}

	void MapModule::postCallAsConstructor(const JSContext& js_context, const std::vector<JSValue>& arguments)
	{
	}

	TITANIUM_PROPERTY_GETTER(MapModule, HYBRID_TYPE)
	{
		return hybrid_type__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, SATELLITE_TYPE)
	{
		return satellite_type__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, NORMAL_TYPE)
	{
		return normal_type__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, TERRAIN_TYPE)
	{
		return terrain_type__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_DRAG_STATE_END)
	{
		return annotation_drag_state_end__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_DRAG_STATE_START)
	{
		return annotation_drag_state_start__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_AZURE)
	{
		return annotation_azure__;
	}
	
	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_BLUE)
	{
		return annotation_blue__;
	}
	
	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_CYAN)
	{
		return annotation_cyan__;
	}
		
	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_GREEN)
	{
		return annotation_green__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_MAGENTA)
	{
		return annotation_magenta__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_ORANGE)
	{
		return annotation_orange__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_PURPLE)
	{
		return annotation_purple__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_RED)
	{
		return annotation_red__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_ROSE)
	{
		return annotation_rose__;
	}
		
	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_VIOLET)
	{
		return annotation_violet__;
	}
		
	TITANIUM_PROPERTY_GETTER(MapModule, ANNOTATION_YELLOW)
	{
		return annotation_yellow__;
	}
	TITANIUM_PROPERTY_GETTER(MapModule, OVERLAY_LEVEL_ABOVE_LABELS)
	{
		return overlay_level_above_labels__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, OVERLAY_LEVEL_ABOVE_ROADS)
	{
		return overlay_level_above_roads__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, SERVICE_DISABLED)
	{
		return service_disabled__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, SERVICE_INVALID)
	{
		return service_invalid__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, SERVICE_MISSING)
	{
		return service_missing__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, SERVICE_VERSION_UPDATE_REQUIRED)
	{
		return service_version_update_required__;
	}

	TITANIUM_PROPERTY_GETTER(MapModule, SUCCESS)
	{
		return success__;
	}

	JSObject MapModule::createView(const JSObject& parameters, JSObject& this_object) TITANIUM_NOEXCEPT
	{
		TITANIUM_LOG_DEBUG("Map::createView");

		Map::View* view = new Map::View(get_context());
		JSObject view_object = view->get_object();

		Titanium::Module::applyProperties(parameters, view_object);
		return view_object;
	}

	JSObject MapModule::createAnnotation(const JSObject& parameters, JSObject& this_object) TITANIUM_NOEXCEPT
	{
		TITANIUM_LOG_DEBUG("Map::createAnnotation");

		Map::Annotation* annotation = new Map::Annotation(get_context());
		JSObject annotation_object = annotation->get_object();

		Titanium::Module::applyProperties(parameters, annotation_object);
		return annotation_object;
	}

	JSObject MapModule::createRoute(const JSObject& parameters, JSObject& this_object) TITANIUM_NOEXCEPT
	{
		TITANIUM_LOG_DEBUG("Map::createRoute");

		Map::Route* route = new Map::Route(get_context());
		JSObject route_object = route->get_object();

		Titanium::Module::applyProperties(parameters, route_object);
		return route_object;
	}

	JSObject MapModule::createCamera(const JSObject& parameters, JSObject& this_object) TITANIUM_NOEXCEPT
	{
		TITANIUM_LOG_DEBUG("Map::createCamera");

		Map::Annotation* camera = new Map::Annotation(get_context());
		JSObject camera_object = camera->get_object();

		Titanium::Module::applyProperties(parameters, camera_object);
		return camera_object;
	}

	Map::GOOGLE_PLAY_SERVICE_STATE MapModule::isGooglePlayServicesAvailable() TITANIUM_NOEXCEPT
	{
		return Map::GOOGLE_PLAY_SERVICE_STATE::MISSING;
	}

	void MapModule::JSExportInitialize() {
		JSExport<MapModule>::SetClassVersion(1);
		JSExport<MapModule>::SetParent(JSExport<JSExportObject>::Class());

		TITANIUM_ADD_PROPERTY_READONLY(MapModule, HYBRID_TYPE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, SATELLITE_TYPE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, NORMAL_TYPE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, TERRAIN_TYPE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_DRAG_STATE_END);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_DRAG_STATE_START);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_AZURE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_BLUE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_CYAN);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_GREEN);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_MAGENTA);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_ORANGE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_PURPLE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_RED);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_ROSE);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_VIOLET);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, ANNOTATION_YELLOW);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, OVERLAY_LEVEL_ABOVE_LABELS);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, OVERLAY_LEVEL_ABOVE_ROADS);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, SERVICE_DISABLED);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, SERVICE_INVALID);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, SERVICE_MISSING);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, SERVICE_VERSION_UPDATE_REQUIRED);
		TITANIUM_ADD_PROPERTY_READONLY(MapModule, SUCCESS);

		TITANIUM_ADD_FUNCTION(MapModule, createView);
		TITANIUM_ADD_FUNCTION(MapModule, createAnnotation);
		TITANIUM_ADD_FUNCTION(MapModule, createRoute);
		TITANIUM_ADD_FUNCTION(MapModule, createCamera);
		TITANIUM_ADD_FUNCTION(MapModule, isGooglePlayServicesAvailable);
	}

	TITANIUM_FUNCTION(MapModule, createView)
	{
		ENSURE_OPTIONAL_OBJECT_AT_INDEX(parameters, 0);
		return createView(parameters, this_object);
	}

	TITANIUM_FUNCTION(MapModule, createAnnotation)
	{
		ENSURE_OPTIONAL_OBJECT_AT_INDEX(parameters, 0);
		return createAnnotation(parameters, this_object);
	}

	TITANIUM_FUNCTION(MapModule, createRoute)
	{
		ENSURE_OPTIONAL_OBJECT_AT_INDEX(parameters, 0);
		return createRoute(parameters, this_object);
	}

	TITANIUM_FUNCTION(MapModule, createCamera)
	{
		ENSURE_OPTIONAL_OBJECT_AT_INDEX(parameters, 0);
		return createCamera(parameters, this_object);
	}

	TITANIUM_FUNCTION(MapModule, isGooglePlayServicesAvailable)
	{
		return this_object.get_context().CreateNumber(Map::Constants::to_underlying_type(isGooglePlayServicesAvailable()));
	}
}