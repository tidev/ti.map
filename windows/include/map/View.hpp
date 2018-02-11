/**
 * Ti.Map.View for Windows
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef _MAP_VIEW_HPP_
#define _MAP_VIEW_HPP_

#include "map/Annotation.hpp"
#include "map/Constants.hpp"
#include "map/MapRegionTypev2.hpp"
#include "map/MapLocationTypev2.hpp"
#include "map/Camera.hpp"
#include "map/CameraAnimationParams.hpp"
#include "map/Route.hpp"

#include "Titanium/UI/View.hpp"

#include "TitaniumWindows/UI/View.hpp"

#include "timap_EXPORT.h"
#include "Titanium/detail/TiBase.hpp"

#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
using namespace Windows::UI::Xaml::Controls::Maps;
using namespace Windows::Services::Maps;
#endif

namespace Ti
{
	namespace Map
	{
		using namespace HAL;
		using Annotation_shared_ptr_t = std::shared_ptr<Annotation>;
		using Route_shared_ptr_t = std::shared_ptr<Route>;
		using Camera_shared_ptr_t = std::shared_ptr<Camera>;

		/*!
		  @class
 		  @discussion This is the Titanium.Map.View implementation for Windows.
		*/
		class TIMAP_EXPORT View final : public TitaniumWindows::UI::View, public JSExport<View>
		{
		public:
			View(const JSContext&) TITANIUM_NOEXCEPT;

			virtual ~View();
			View(const View&)            = default;
			View& operator=(const View&) = default;
		#ifdef TITANIUM_MOVE_CTOR_AND_ASSIGN_DEFAULT_ENABLE
			View(View&&)                 = default;
			View& operator=(View&&)      = default;
		#endif

			static void JSExportInitialize();

			virtual void postCallAsConstructor(const JSContext& js_context, const std::vector<JSValue>& arguments);

			// properties
			virtual bool get_animate() const TITANIUM_NOEXCEPT final;
			virtual void set_animate(const bool& animate) TITANIUM_NOEXCEPT;

			virtual std::vector<Annotation_shared_ptr_t> get_annotations() const TITANIUM_NOEXCEPT final;
			virtual void set_annotations(const std::vector<Annotation_shared_ptr_t>& annotations) TITANIUM_NOEXCEPT;

			virtual Camera_shared_ptr_t get_camera() const TITANIUM_NOEXCEPT final;
			virtual void set_camera(const Camera_shared_ptr_t& camera) TITANIUM_NOEXCEPT;

			virtual bool get_compassEnabled() const TITANIUM_NOEXCEPT final;
			virtual void set_compassEnabled(const bool& compass) TITANIUM_NOEXCEPT;

			virtual bool get_enableZoomControls() const TITANIUM_NOEXCEPT final;
			virtual void set_enableZoomControls(const bool& zoomControls) TITANIUM_NOEXCEPT;

			virtual MAP_TYPE get_mapType() const TITANIUM_NOEXCEPT final;
			virtual void set_mapType(const MAP_TYPE& mapType) TITANIUM_NOEXCEPT;

			virtual uint32_t get_maxZoomLevel() const TITANIUM_NOEXCEPT;

			virtual uint32_t get_minZoomLevel() const TITANIUM_NOEXCEPT;

			virtual bool get_pitchEnabled() const TITANIUM_NOEXCEPT final;
			virtual void set_pitchEnabled(const bool& pitch) TITANIUM_NOEXCEPT;

			virtual MapRegionTypev2 get_region() const TITANIUM_NOEXCEPT final;
			virtual void set_region(const MapRegionTypev2& region) TITANIUM_NOEXCEPT;

			virtual bool get_rotateEnabled() const TITANIUM_NOEXCEPT final;
			virtual void set_rotateEnabled(const bool& rotate) TITANIUM_NOEXCEPT;

			virtual bool get_showsBuildings() const TITANIUM_NOEXCEPT final;
			virtual void set_showsBuildings(const bool& buildings) TITANIUM_NOEXCEPT;

			virtual bool get_showsPointsOfInterest() const TITANIUM_NOEXCEPT final;
			virtual void set_showsPointsOfInterest(const bool& poi) TITANIUM_NOEXCEPT;

			virtual bool get_traffic() const TITANIUM_NOEXCEPT final;
			virtual void set_traffic(const bool& traffic) TITANIUM_NOEXCEPT;

			virtual bool get_userLocation() const TITANIUM_NOEXCEPT final;
			virtual void set_userLocation(const bool& userLocation) TITANIUM_NOEXCEPT;

			virtual bool get_userLocationButton() const TITANIUM_NOEXCEPT final;
			virtual void set_userLocationButton(const bool& userLocationButton) TITANIUM_NOEXCEPT;

			virtual bool get_zOrderOnTop() const TITANIUM_NOEXCEPT final;
			virtual void set_zOrderOnTop(const bool& zOrderOnTop) TITANIUM_NOEXCEPT;

			// Methods
			virtual void addAnnotation(const Annotation_shared_ptr_t& annotation) TITANIUM_NOEXCEPT;
			virtual void addAnnotations(const std::vector<Annotation_shared_ptr_t>& annotations) TITANIUM_NOEXCEPT;
			virtual void addRoute(const Route_shared_ptr_t& route) TITANIUM_NOEXCEPT;
			virtual void animateCamera(const CameraAnimationParams& animationParams, JSObject& callback) TITANIUM_NOEXCEPT;
			virtual void deselectAnnotation(const Annotation_shared_ptr_t& annotation) TITANIUM_NOEXCEPT;
			virtual void removeAllAnnotations() TITANIUM_NOEXCEPT;
			virtual void removeAnnotation(const Annotation_shared_ptr_t& annotation) TITANIUM_NOEXCEPT;
			virtual void removeAnnotations(const std::vector<Annotation_shared_ptr_t>& annotations) TITANIUM_NOEXCEPT;
			virtual void removeRoute(const Route_shared_ptr_t& route) TITANIUM_NOEXCEPT;
			virtual void selectAnnotation(const Annotation_shared_ptr_t& annotation) TITANIUM_NOEXCEPT;
			virtual void setLocation(const MapLocationTypev2& location) TITANIUM_NOEXCEPT;
			virtual void showAnnotations(const std::vector<Annotation_shared_ptr_t>& annotations) TITANIUM_NOEXCEPT;
			virtual void snapshot() TITANIUM_NOEXCEPT;
			virtual void zoom(const uint32_t& level) TITANIUM_NOEXCEPT;

			TITANIUM_FUNCTION_DEF(addAnnotation);
			TITANIUM_FUNCTION_DEF(addAnnotations);
			TITANIUM_FUNCTION_DEF(addRoute);
			TITANIUM_FUNCTION_DEF(animateCamera);
			TITANIUM_FUNCTION_DEF(deselectAnnotation);
			TITANIUM_FUNCTION_DEF(removeAllAnnotations);
			TITANIUM_FUNCTION_DEF(removeAnnotation);
			TITANIUM_FUNCTION_DEF(removeAnnotations);
			TITANIUM_FUNCTION_DEF(removeRoute);
			TITANIUM_FUNCTION_DEF(selectAnnotation);
			TITANIUM_FUNCTION_DEF(setLocation);
			TITANIUM_FUNCTION_DEF(showAnnotations);
			TITANIUM_FUNCTION_DEF(snapshot);
			TITANIUM_FUNCTION_DEF(zoom);

			TITANIUM_PROPERTY_DEF(animate);
			TITANIUM_FUNCTION_DEF(setAnimate);

			TITANIUM_PROPERTY_DEF(annotations);
			TITANIUM_FUNCTION_DEF(setAnnotations);

			TITANIUM_PROPERTY_DEF(camera);
			TITANIUM_FUNCTION_DEF(setCamera);

			TITANIUM_PROPERTY_DEF(compassEnabled);
			TITANIUM_FUNCTION_DEF(setCompassEnabled);

			TITANIUM_PROPERTY_DEF(enableZoomControls);
			TITANIUM_FUNCTION_DEF(setEnableZoomControls);

			TITANIUM_PROPERTY_DEF(mapType);
			TITANIUM_FUNCTION_DEF(setMapType);

			TITANIUM_PROPERTY_READONLY_DEF(maxZoomLevel);

			TITANIUM_PROPERTY_READONLY_DEF(minZoomLevel);

			TITANIUM_PROPERTY_DEF(pitchEnabled);
			TITANIUM_FUNCTION_DEF(setPitchEnabled);

			TITANIUM_PROPERTY_DEF(region);
			TITANIUM_FUNCTION_DEF(setRegion);

			TITANIUM_PROPERTY_DEF(rotateEnabled);
			TITANIUM_FUNCTION_DEF(setRotateEnabled);

			TITANIUM_PROPERTY_DEF(showsBuildings);
			TITANIUM_FUNCTION_DEF(setShowsBuildings);

			TITANIUM_PROPERTY_DEF(showsPointsOfInterest);
			TITANIUM_FUNCTION_DEF(setShowsPointsOfInterest);

			TITANIUM_PROPERTY_DEF(traffic);
			TITANIUM_FUNCTION_DEF(setTraffic);

			TITANIUM_PROPERTY_DEF(userLocation);
			TITANIUM_FUNCTION_DEF(setUserLocation);

			TITANIUM_PROPERTY_DEF(userLocationButton);
			TITANIUM_FUNCTION_DEF(setUserLocationButton);

			TITANIUM_PROPERTY_DEF(zOrderOnTop);
			TITANIUM_FUNCTION_DEF(setZOrderOnTop);

		protected:
#pragma warning(push)
#pragma warning(disable : 4251)
			bool animate__;
			std::vector<Annotation_shared_ptr_t> annotations__;
			Camera_shared_ptr_t camera__;
			bool compassEnabled__;
			bool enableZoomControls__;
			MAP_TYPE mapType__;
			bool pitchEnabled__;
			MapRegionTypev2 region__;
			bool rotateEnabled__;
			bool showsBuildings__;
			bool showsPointsOfInterest__;
			bool traffic__;
			bool userLocation__;
			bool userLocationButton__;
			bool zOrderOnTop__;
#pragma warning(pop)

#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			MapControl^ mapview__ = { nullptr };
#endif
		};
	}
}
#endif // _MAP_VIEW_HPP_
