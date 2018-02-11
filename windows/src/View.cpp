/**
 * Titanium.Map.View for Windows
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "TitaniumWindows/UI/WindowsViewLayoutDelegate.hpp"
#include "TitaniumWindows/Utility.hpp"

#include "Map/View.hpp"

#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
using namespace Windows::Devices::Geolocation;
#endif

namespace Ti
{
	namespace Map
	{
		View::View(const JSContext& js_context) TITANIUM_NOEXCEPT
			: TitaniumWindows::UI::View(js_context),
			animate__(false),
			compassEnabled__(false),
			enableZoomControls__(false),
			mapType__(MAP_TYPE::NORMAL),
			region__({ 0 }),
			pitchEnabled__(false),
			rotateEnabled__(false),
			showsBuildings__(false),
			showsPointsOfInterest__(false),
			traffic__(false),
			userLocation__(false),
			userLocationButton__(false),
			zOrderOnTop__(false)
		{
			TITANIUM_LOG_DEBUG("View::ctor Initialize");
		}

		View::~View()
		{
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			mapview__ = nullptr;
#endif
		}

		void View::postCallAsConstructor(const JSContext& js_context, const std::vector<JSValue>& arguments) {
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			mapview__ = ref new MapControl();

			// Set service token
			// TODO : Load this from app properties, windows_map_service_token?
			mapview__->MapServiceToken = "VrSrmXR8B5bgklWrs0CK_w";

			setLayoutDelegate<TitaniumWindows::UI::WindowsViewLayoutDelegate>();

			layoutDelegate__->set_defaultHeight(Titanium::UI::LAYOUT::FILL);
			layoutDelegate__->set_defaultWidth(Titanium::UI::LAYOUT::FILL);

			getViewLayoutDelegate<TitaniumWindows::UI::WindowsViewLayoutDelegate>()->setComponent(mapview__);

			zoom(12); // use a default zoom of 12 for Windows?
#endif
		}

		uint32_t View::get_maxZoomLevel() const TITANIUM_NOEXCEPT
		{
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			return static_cast<uint32_t>(mapview__->MaxZoomLevel); // should be 20
#else
			return 20;
#endif
		}

		uint32_t View::get_minZoomLevel() const TITANIUM_NOEXCEPT
		{
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			return static_cast<uint32_t>(mapview__->MinZoomLevel); // should be 1
#else
			return 1;
#endif
		}

		void View::set_mapType(const MAP_TYPE& mapType) TITANIUM_NOEXCEPT
		{
			mapType__ = mapType;
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			if (mapType == MAP_TYPE::HYBRID) {
				mapview__->Style = MapStyle::AerialWithRoads;
			} else if (mapType == MAP_TYPE::SATELLITE) {
				mapview__->Style = MapStyle::Aerial;
			} else if (mapType == MAP_TYPE::NORMAL) {
				mapview__->Style = MapStyle::Road;
			}
#endif
		}

		void View::set_region(const MapRegionTypev2& region) TITANIUM_NOEXCEPT
		{
			region__ = region;

#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			// set center to supplied lat/long
			BasicGeoposition location = { region.latitude, region.longitude };
			auto gp = ref new Geopoint(location);
			mapview__->Center = gp;

			// If there are deltas, use them to set the view bounds
			if (region.latitudeDelta > 0 && region.longitudeDelta > 0) {
				BasicGeoposition top_left = { region.latitude - region.latitudeDelta, region.longitude - region.longitudeDelta };
				BasicGeoposition bottom_right = { region.latitude + region.latitudeDelta, region.longitude + region.longitudeDelta };

				auto bounds = ref new GeoboundingBox(bottom_right, top_left);
				mapview__->TrySetViewBoundsAsync(bounds, nullptr, get_animate() ? MapAnimationKind::Default : MapAnimationKind::None);
				mapview__->DesiredPitch = region.tilt;
			} else {
				// no deltas, use the long/lat and zoom to set view area/region
				if (region.zoom > 0) {
					mapview__->TrySetViewAsync(gp, static_cast<double>(region.zoom), nullptr, region.tilt, get_animate() ? MapAnimationKind::Default : MapAnimationKind::None);
				} else {
					mapview__->TrySetViewAsync(gp, nullptr, nullptr, region.tilt, get_animate() ? MapAnimationKind::Default : MapAnimationKind::None);
				}
			}
#endif
		}

		void View::set_showsBuildings(const bool& buildings) TITANIUM_NOEXCEPT
		{
			showsBuildings__ = buildings;
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			mapview__->LandmarksVisible = buildings;
#endif
		}

		void View::set_traffic(const bool& traffic) TITANIUM_NOEXCEPT
		{
			traffic__ = traffic;
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			mapview__->TrafficFlowVisible = traffic;
#endif
		}

		void View::addAnnotation(const Annotation_shared_ptr_t& annotation) TITANIUM_NOEXCEPT
		{
			annotations__.push_back(annotation);
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			auto native_annotation_ptr = annotation->get_object().GetPrivate<Annotation>();
			if (native_annotation_ptr != nullptr) {
				mapview__->Children->Append(native_annotation_ptr->GetMapIcon());
			}
#endif
		}

		void View::removeAnnotation(const Annotation_shared_ptr_t& annotation) TITANIUM_NOEXCEPT
		{
			annotations__.erase(std::remove(annotations__.begin(), annotations__.end(), annotation), annotations__.end());

#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			for (size_t i = 0; i < mapview__->Children->Size; i++) {
				auto element = mapview__->Children->GetAt(i);
				Windows::UI::Xaml::Controls::Grid^ icon = dynamic_cast<Windows::UI::Xaml::Controls::Grid^>(element);
				if (icon == nullptr) {
					continue; // not an annotation
				}
				// find annotation identified by title or a reference.
				auto annotation_ptr = annotation->get_object().GetPrivate<Annotation>()->GetMapIcon();
				if (icon == annotation_ptr) {
					//mapview__->MapElements->RemoveAt(i);
					mapview__->Children->RemoveAt(i);
					break;
				}
			}
#endif
		}

		void View::zoom(const uint32_t& zoom) TITANIUM_NOEXCEPT
		{
			//Titanium::Map::View::zoom(zoom);
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			if (zoom != 0) // no zoom specified in the region object?
			{
				// TODO Enforce min/max zoom levels?
				mapview__->ZoomLevel = zoom;
			}
#endif
		}

		void View::addAnnotations(const std::vector<Annotation_shared_ptr_t>& annotations) TITANIUM_NOEXCEPT
		{
			for (Annotation_shared_ptr_t annotation : annotations) {
				addAnnotation(annotation);
			}
		}

		void View::addRoute(const Route_shared_ptr_t& route) TITANIUM_NOEXCEPT
		{
			TITANIUM_LOG_WARN("Map::View::addRoute: Unimplemented");
		}

		void View::animateCamera(const CameraAnimationParams& animationParams, JSObject& callback) TITANIUM_NOEXCEPT
		{
			TITANIUM_LOG_WARN("Map::View::animateCamera: Unimplemented");
		}

		void View::deselectAnnotation(const Annotation_shared_ptr_t& annotation) TITANIUM_NOEXCEPT
		{
			TITANIUM_LOG_WARN("Map::View::deselectAnnotation: Unimplemented");
		}

		void View::removeAllAnnotations() TITANIUM_NOEXCEPT
		{
			removeAnnotations(get_annotations());
		}

		void View::removeAnnotations(const std::vector<Annotation_shared_ptr_t>& annotations) TITANIUM_NOEXCEPT
		{
			for (Annotation_shared_ptr_t annotation : annotations) {
				removeAnnotation(annotation);
			}
		}

		void View::removeRoute(const Route_shared_ptr_t& route) TITANIUM_NOEXCEPT
		{
			TITANIUM_LOG_WARN("Map::View::removeRoute: Unimplemented");
		}

		void View::selectAnnotation(const Annotation_shared_ptr_t& annotation) TITANIUM_NOEXCEPT
		{
			TITANIUM_LOG_WARN("Map::View::selectAnnotation: Unimplemented");
		}

		void View::setLocation(const MapLocationTypev2& location) TITANIUM_NOEXCEPT
		{
			TITANIUM_LOG_WARN("Map::View::setLocation: Unimplemented");
		}

		void View::showAnnotations(const std::vector<Annotation_shared_ptr_t>& annotations) TITANIUM_NOEXCEPT
		{
			TITANIUM_LOG_WARN("Map::View::showAnnotations: Unimplemented");
		}

		void View::snapshot() TITANIUM_NOEXCEPT
		{
			TITANIUM_LOG_WARN("Map::View::snapshot: Unimplemented");
		}

		// properties
		bool View::get_animate() const TITANIUM_NOEXCEPT
		{
			return animate__;
		}

		void View::set_animate(const bool& animate) TITANIUM_NOEXCEPT
		{
			animate__ = animate;
		}

		std::vector<Annotation_shared_ptr_t> View::get_annotations() const TITANIUM_NOEXCEPT
		{
			return annotations__;
		}

		void View::set_annotations(const std::vector<Annotation_shared_ptr_t>& annotations) TITANIUM_NOEXCEPT
		{
			removeAllAnnotations();
			addAnnotations(annotations);
		}

		Camera_shared_ptr_t View::get_camera() const TITANIUM_NOEXCEPT
		{
			return camera__;
		}

		void View::set_camera(const Camera_shared_ptr_t& camera) TITANIUM_NOEXCEPT
		{
			camera__ = camera;
		}

		bool View::get_compassEnabled() const TITANIUM_NOEXCEPT
		{
			return compassEnabled__;
		}

		void View::set_compassEnabled(const bool& compass) TITANIUM_NOEXCEPT
		{
			compassEnabled__ = compass;
		}

		bool View::get_enableZoomControls() const TITANIUM_NOEXCEPT
		{
			return enableZoomControls__;
		}

		void View::set_enableZoomControls(const bool& zoomControls) TITANIUM_NOEXCEPT
		{
			enableZoomControls__ = zoomControls;
		}

		MAP_TYPE View::get_mapType() const TITANIUM_NOEXCEPT
		{
			return mapType__;
		}

		bool View::get_pitchEnabled() const TITANIUM_NOEXCEPT
		{
			return pitchEnabled__;
		}

		void View::set_pitchEnabled(const bool& pitch) TITANIUM_NOEXCEPT
		{
			pitchEnabled__ = pitch;
		}

		MapRegionTypev2 View::get_region() const TITANIUM_NOEXCEPT
		{
			return region__;
		}

		bool View::get_rotateEnabled() const TITANIUM_NOEXCEPT
		{
			return rotateEnabled__;
		}

		void View::set_rotateEnabled(const bool& rotate) TITANIUM_NOEXCEPT
		{
			rotateEnabled__ = rotate;
		}

		bool View::get_showsBuildings() const TITANIUM_NOEXCEPT
		{
			return showsBuildings__;
		}

		bool View::get_showsPointsOfInterest() const TITANIUM_NOEXCEPT
		{
			return showsPointsOfInterest__;
		}

		void View::set_showsPointsOfInterest(const bool& poi) TITANIUM_NOEXCEPT
		{
			showsPointsOfInterest__ = poi;
		}

		bool View::get_traffic() const TITANIUM_NOEXCEPT
		{
			return traffic__;
		}

		bool View::get_userLocation() const TITANIUM_NOEXCEPT
		{
			return userLocation__;
		}

		void View::set_userLocation(const bool& userLocation) TITANIUM_NOEXCEPT
		{
			userLocation__ = userLocation;
		}

		bool View::get_userLocationButton() const TITANIUM_NOEXCEPT
		{
			return userLocationButton__;
		}

		void View::set_userLocationButton(const bool& userLocationButton) TITANIUM_NOEXCEPT
		{
			userLocationButton__ = userLocationButton;
		}

		bool View::get_zOrderOnTop() const TITANIUM_NOEXCEPT
		{
			return zOrderOnTop__;
		}

		void View::set_zOrderOnTop(const bool& zOrderOnTop) TITANIUM_NOEXCEPT
		{
			zOrderOnTop__ = zOrderOnTop;
		}

		void View::JSExportInitialize() {
			JSExport<View>::SetClassVersion(1);
			JSExport<View>::SetParent(JSExport<Titanium::UI::View>::Class());

			// methods
			TITANIUM_ADD_FUNCTION(View, addAnnotation);
			TITANIUM_ADD_FUNCTION(View, addAnnotations);
			TITANIUM_ADD_FUNCTION(View, addRoute);
			TITANIUM_ADD_FUNCTION(View, animateCamera);
			TITANIUM_ADD_FUNCTION(View, deselectAnnotation);
			TITANIUM_ADD_FUNCTION(View, removeAllAnnotations);
			TITANIUM_ADD_FUNCTION(View, removeAnnotation);
			TITANIUM_ADD_FUNCTION(View, removeAnnotations);
			TITANIUM_ADD_FUNCTION(View, removeRoute);
			TITANIUM_ADD_FUNCTION(View, selectAnnotation);
			TITANIUM_ADD_FUNCTION(View, setLocation);
			TITANIUM_ADD_FUNCTION(View, showAnnotations);
			TITANIUM_ADD_FUNCTION(View, snapshot);
			TITANIUM_ADD_FUNCTION(View, zoom);

			// properties
			TITANIUM_ADD_PROPERTY(View, animate);
			TITANIUM_ADD_PROPERTY(View, camera);
			TITANIUM_ADD_PROPERTY(View, annotations);
			TITANIUM_ADD_PROPERTY(View, compassEnabled);
			TITANIUM_ADD_PROPERTY(View, enableZoomControls);
			TITANIUM_ADD_PROPERTY(View, mapType);
			TITANIUM_ADD_PROPERTY_READONLY(View, maxZoomLevel);
			TITANIUM_ADD_PROPERTY_READONLY(View, minZoomLevel);
			TITANIUM_ADD_PROPERTY(View, pitchEnabled);
			TITANIUM_ADD_PROPERTY(View, region);
			TITANIUM_ADD_PROPERTY(View, rotateEnabled);
			TITANIUM_ADD_PROPERTY(View, showsBuildings);
			TITANIUM_ADD_PROPERTY(View, showsPointsOfInterest);
			TITANIUM_ADD_PROPERTY(View, traffic);
			TITANIUM_ADD_PROPERTY(View, userLocation);
			TITANIUM_ADD_PROPERTY(View, userLocationButton);
			TITANIUM_ADD_PROPERTY(View, zOrderOnTop);

			// prperty setter functions
			TITANIUM_ADD_FUNCTION(View, setCamera);
			TITANIUM_ADD_FUNCTION(View, setAnnotations);
			TITANIUM_ADD_FUNCTION(View, setAnimate);
			TITANIUM_ADD_FUNCTION(View, setCompassEnabled);
			TITANIUM_ADD_FUNCTION(View, setEnableZoomControls);
			TITANIUM_ADD_FUNCTION(View, setMapType);
			TITANIUM_ADD_FUNCTION(View, setPitchEnabled);
			TITANIUM_ADD_FUNCTION(View, setRegion);
			TITANIUM_ADD_FUNCTION(View, setRotateEnabled);
			TITANIUM_ADD_FUNCTION(View, setShowsBuildings);
			TITANIUM_ADD_FUNCTION(View, setShowsPointsOfInterest);
			TITANIUM_ADD_FUNCTION(View, setTraffic);
			TITANIUM_ADD_FUNCTION(View, setUserLocation);
			TITANIUM_ADD_FUNCTION(View, setUserLocationButton);
			TITANIUM_ADD_FUNCTION(View, setZOrderOnTop);
		}

		TITANIUM_FUNCTION(View, addAnnotation)
		{
			TITANIUM_ASSERT(arguments.size() == 1);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			addAnnotation(static_cast<JSObject>(_0).GetPrivate<Annotation>());

			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, addAnnotations)
		{
			TITANIUM_ASSERT(arguments.size() > 0);

			const auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			const auto _0Obj = static_cast<JSObject>(_0);
			TITANIUM_ASSERT(_0Obj.IsArray());
			const auto annotations = static_cast<JSArray>(_0Obj);
			addAnnotations(annotations.GetPrivateItems<Annotation>());
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, addRoute)
		{
			TITANIUM_ASSERT(arguments.size() == 1);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			addRoute(static_cast<JSObject>(_0).GetPrivate<Route>());

			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, animateCamera)
		{
			if (arguments.size() > 1) {
				auto _0 = arguments.at(0);
				auto _1 = arguments.at(1);
				TITANIUM_ASSERT(_0.IsObject());
				TITANIUM_ASSERT(_1.IsObject());
				auto callback = static_cast<JSObject>(_1);
				animateCamera(js_to_CameraAnimationParams(static_cast<JSObject>(_0)), callback);
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, deselectAnnotation)
		{
			TITANIUM_ASSERT(arguments.size() == 1);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			deselectAnnotation(static_cast<JSObject>(_0).GetPrivate<Annotation>());

			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, removeAllAnnotations)
		{
			removeAllAnnotations();
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, removeAnnotation)
		{
			TITANIUM_ASSERT(arguments.size() == 1);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			removeAnnotation(static_cast<JSObject>(_0).GetPrivate<Annotation>());

			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, removeAnnotations)
		{
			TITANIUM_ASSERT(arguments.size() > 0);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			const auto _0Obj = static_cast<JSObject>(_0);
			TITANIUM_ASSERT(_0Obj.IsArray());
			const auto annotations = static_cast<JSArray>(_0Obj);
			removeAnnotations(annotations.GetPrivateItems<Annotation>());

			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, removeRoute)
		{
			TITANIUM_ASSERT(arguments.size() == 1);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			removeRoute(static_cast<JSObject>(_0).GetPrivate<Route>());

			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, selectAnnotation)
		{
			TITANIUM_ASSERT(arguments.size() == 1);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			selectAnnotation(static_cast<JSObject>(_0).GetPrivate<Annotation>());

			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, setLocation)
		{
			TITANIUM_ASSERT(arguments.size() == 1);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			setLocation(js_to_MapLocationTypev2(static_cast<JSObject>(_0)));

			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, showAnnotations)
		{
			TITANIUM_ASSERT(arguments.size() > 0);
			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsObject());
			const auto _0Obj = static_cast<JSObject>(_0);
			TITANIUM_ASSERT(_0Obj.IsArray());
			const auto annotations = static_cast<JSArray>(_0Obj);
			showAnnotations(annotations.GetPrivateItems<Annotation>());
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, snapshot)
		{
			snapshot();
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, zoom)
		{
			TITANIUM_ASSERT(arguments.size() == 1);

			auto _0 = arguments.at(0);
			TITANIUM_ASSERT(_0.IsNumber());
			zoom(static_cast<uint32_t>(_0));

			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, animate)
		{
			return get_context().CreateBoolean(get_animate());
		}

		TITANIUM_PROPERTY_SETTER(View, animate)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_animate(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(View, annotations)
		{
			std::vector<JSValue> js_annotations;
			const auto annotations = get_annotations();
			for (auto annotation : annotations) {
				js_annotations.push_back(annotation->get_object());
			}
			return get_context().CreateArray(js_annotations);
		}

		TITANIUM_PROPERTY_SETTER(View, annotations)
		{
			if (!argument.IsObject()) {
				return false;
			}
			const auto annotations = static_cast<JSObject>(argument);
			if (!annotations.IsArray()) {
				return false;
			}
			set_annotations(static_cast<JSArray>(annotations).GetPrivateItems<Annotation>());
			return true;
		}

		TITANIUM_PROPERTY_GETTER(View, camera)
		{
			return get_camera()->get_object();
		}

		TITANIUM_PROPERTY_SETTER(View, camera)
		{
			TITANIUM_ASSERT(argument.IsObject());
			set_camera(static_cast<JSObject>(argument).GetPrivate<Camera>());
			return true;
		}

		TITANIUM_FUNCTION(View, setAnnotations)
		{
			if (arguments.size() >= 1) {
				js_set_annotations(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, setCamera)
		{
			if (arguments.size() >= 1) {
				js_set_camera(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(View, setAnimate)
		{
			if (arguments.size() >= 1) {
				js_set_animate(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, enableZoomControls)
		{
			return get_context().CreateBoolean(get_enableZoomControls());
		}

		TITANIUM_PROPERTY_SETTER(View, enableZoomControls)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_enableZoomControls(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setEnableZoomControls)
		{
			if (arguments.size() >= 1) {
				js_set_enableZoomControls(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, compassEnabled)
		{
			return get_context().CreateBoolean(get_compassEnabled());
		}

		TITANIUM_PROPERTY_SETTER(View, compassEnabled)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_compassEnabled(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setCompassEnabled)
		{
			if (arguments.size() >= 1) {
				js_set_compassEnabled(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, mapType)
		{
			return get_context().CreateNumber(Constants::to_underlying_type(get_mapType()));
		}

		TITANIUM_PROPERTY_SETTER(View, mapType)
		{
			TITANIUM_ASSERT(argument.IsNumber());
			set_mapType(Constants::to_MAP_TYPE(static_cast<std::underlying_type<MAP_TYPE>::type>(argument)));
			return true;
		}

		TITANIUM_FUNCTION(View, setMapType)
		{
			if (arguments.size() >= 1) {
				js_set_mapType(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, maxZoomLevel)
		{
			return get_context().CreateNumber(get_maxZoomLevel());
		}

		TITANIUM_PROPERTY_GETTER(View, minZoomLevel)
		{
			return get_context().CreateNumber(get_minZoomLevel());
		}

		TITANIUM_PROPERTY_GETTER(View, pitchEnabled)
		{
			return get_context().CreateBoolean(get_pitchEnabled());
		}

		TITANIUM_PROPERTY_SETTER(View, pitchEnabled)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_pitchEnabled(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setPitchEnabled)
		{
			if (arguments.size() >= 1) {
				js_set_pitchEnabled(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, region)
		{
			return MapRegionTypev2_to_js(get_context(), get_region());
		}

		TITANIUM_PROPERTY_SETTER(View, region)
		{
			TITANIUM_ASSERT(argument.IsObject());
			const auto region = static_cast<JSObject>(argument);
			set_region(js_to_MapRegionTypev2(region));
			return true;
		}

		TITANIUM_FUNCTION(View, setRegion)
		{
			if (arguments.size() >= 1) {
				js_set_region(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, rotateEnabled)
		{
			return get_context().CreateBoolean(get_rotateEnabled());
		}

		TITANIUM_PROPERTY_SETTER(View, rotateEnabled)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_rotateEnabled(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setRotateEnabled)
		{
			if (arguments.size() >= 1) {
				js_set_rotateEnabled(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, showsBuildings)
		{
			return get_context().CreateBoolean(get_showsBuildings());
		}

		TITANIUM_PROPERTY_SETTER(View, showsBuildings)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_showsBuildings(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setShowsBuildings)
		{
			if (arguments.size() >= 1) {
				js_set_showsBuildings(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, showsPointsOfInterest)
		{
			return get_context().CreateBoolean(get_showsPointsOfInterest());
		}

		TITANIUM_PROPERTY_SETTER(View, showsPointsOfInterest)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_showsPointsOfInterest(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setShowsPointsOfInterest)
		{
			if (arguments.size() >= 1) {
				js_set_showsPointsOfInterest(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, traffic)
		{
			return get_context().CreateBoolean(get_traffic());
		}

		TITANIUM_PROPERTY_SETTER(View, traffic)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_traffic(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setTraffic)
		{
			if (arguments.size() >= 1) {
				js_set_traffic(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, userLocation)
		{
			return get_context().CreateNumber(get_userLocation());
		}

		TITANIUM_PROPERTY_SETTER(View, userLocation)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_userLocation(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setUserLocation)
		{
			if (arguments.size() >= 1) {
				js_set_userLocation(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, userLocationButton)
		{
			return get_context().CreateBoolean(get_userLocationButton());
		}

		TITANIUM_PROPERTY_SETTER(View, userLocationButton)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_userLocationButton(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setUserLocationButton)
		{
			if (arguments.size() >= 1) {
				js_set_userLocationButton(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

		TITANIUM_PROPERTY_GETTER(View, zOrderOnTop)
		{
			return get_context().CreateBoolean(get_zOrderOnTop());
		}

		TITANIUM_PROPERTY_SETTER(View, zOrderOnTop)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_zOrderOnTop(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_FUNCTION(View, setZOrderOnTop)
		{
			if (arguments.size() >= 1) {
				js_set_zOrderOnTop(arguments.at(0));
			}
			return get_context().CreateUndefined();
		}

	} // namespace Map
}  // namespace Titanium
