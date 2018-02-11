/**
 * Titanium.Map.Annotation for Windows
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef _MAP_ANNOTATION_HPP_
#define _MAP_ANNOTATION_HPP_

#include "map/Constants.hpp"

#include "Titanium/UI/View.hpp"
#include "Titanium/UI/Point.hpp"

#include "timap_EXPORT.h"
#include "Titanium/detail/TiBase.hpp"

namespace Ti
{
	namespace Map
	{
		using namespace HAL;

		using TiView_shared_ptr_t = std::shared_ptr<Titanium::UI::View>;

		class TIMAP_EXPORT Annotation final : public JSExportObject, public JSExport<Annotation>
		{
		public:

		/*!
			@property
			@abstract canShowCallout
			@discussion Defines whether the annotation view is able to display extra information in a callout bubble.

			When this is set to true, the annotation view shows the callout bubble on selection. Set this to false to disabled the showing of the callout bubble on selection. This must be set before the annotation is added to the map.

			If this value is undefined, the value is treated as explicit true.
		*/
		virtual bool get_canShowCallout() const TITANIUM_NOEXCEPT;
		virtual void set_canShowCallout(const bool& canShowCallout) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract centerOffset
			@discussion Defines a center offset point for the annotation.

			By default, the center point of an annotation view is placed at the coordinate point of the associated annotation. Use this property to reposition the annotation view as needed. Positive offset values move the annotation view down and right, while negative values move it up and left.
		*/
		virtual Titanium::UI::Point get_centerOffset() const TITANIUM_NOEXCEPT;
		virtual void set_centerOffset(const Titanium::UI::Point& centerOffset) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract customView
			@discussion Defines a custom view to be used by the annotation.

			Must be set during creation. User interaction is disabled on the view. No view interaction events (click, touchstart etc) will be fired..
		*/
		virtual TiView_shared_ptr_t get_customView() const TITANIUM_NOEXCEPT;
		virtual void set_customView(const TiView_shared_ptr_t& customView) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract draggable
			@discussion Determines whether the pin can be dragged by the user.

			When an annotation is draggable, it can be moved by the user by long pressing on the pin.

			Default: false
		*/
		virtual bool get_draggable() const TITANIUM_NOEXCEPT;
		virtual void set_draggable(const bool& draggable) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract image
			@discussion Image to use for the the pin.
		*/
		virtual std::string get_image() const TITANIUM_NOEXCEPT;
		virtual void set_image(const std::string& image) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract latitude
			@discussion Latitude of the annotation, in decimal degrees.
		*/
		virtual double get_latitude() const TITANIUM_NOEXCEPT;
		virtual void set_latitude(const double& latitude) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract leftButton
			@discussion Left button image on the annotation, specified as an image URL or an iOSbutton constant.
		*/
		virtual std::string get_leftButton() const TITANIUM_NOEXCEPT;
		virtual void set_leftButton(const std::string& leftButton) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract leftView
			@discussion Left view that is displayed on the annotation.
		*/
		virtual TiView_shared_ptr_t get_leftView() const TITANIUM_NOEXCEPT;
		virtual void set_leftView(const TiView_shared_ptr_t& leftView) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract longitude
			@discussion Longitude of the annotation, in decimal degrees.
		*/
		virtual double get_longitude() const TITANIUM_NOEXCEPT;
		virtual void set_longitude(const double& longitude) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract pincolor
			@discussion Pin color. Specify one of: <Titanium.Map.ANNOTATION_GREEN>, <Titanium.Map.ANNOTATION_PURPLE> or <Titanium.Map.ANNOTATION_RED>.
		*/
		virtual ANNOTATION_COLOR get_pincolor() const TITANIUM_NOEXCEPT;
		virtual void set_pincolor(const ANNOTATION_COLOR& pincolor) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract rightButton
			@discussion Right button image on the annotation, specified as an image URL or an iOSbutton constant.
		*/
		virtual std::string get_rightButton() const TITANIUM_NOEXCEPT;
		virtual void set_rightButton(const std::string& rightButton) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract rightView
			@discussion Right view that is displayed on the annotation.
		*/
		virtual TiView_shared_ptr_t get_rightView() const TITANIUM_NOEXCEPT;
		virtual void set_rightView(const TiView_shared_ptr_t& rightView) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract showInfoWindow
			@discussion Show or hide the view that is displayed on the annotation when clicked.

			When this is false, clicking on the annotation will not center it on the map, but the annotation will still be selected, thus triggering the click event. If the annotation is selected, and the info window is hidden, then the next click will deselect the annotation, thus will NOT show the info window, regardless of the current state of this property.

			Default: true
		*/
		virtual bool get_showInfoWindow() const TITANIUM_NOEXCEPT;
		virtual void set_showInfoWindow(const bool& showInfoWindow) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract subtitle
			@discussion Secondary title of the annotation view.
		*/
		virtual std::string get_subtitle() const TITANIUM_NOEXCEPT;
		virtual void set_subtitle(const std::string& subtitle) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract subtitleid
			@discussion Key in the locale file to use for the subtitle property.
		*/
		virtual std::string get_subtitleid() const TITANIUM_NOEXCEPT;
		virtual void set_subtitleid(const std::string& subtitleid) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract title
			@discussion Primary title of the annotation view.
		*/
		virtual std::string get_title() const TITANIUM_NOEXCEPT;
		virtual void set_title(const std::string& title) TITANIUM_NOEXCEPT;

		/*!
			@property
			@abstract titleid
			@discussion Key in the locale file to use for the title property.
		*/
		virtual std::string get_titleid() const TITANIUM_NOEXCEPT;
		virtual void set_titleid(const std::string& titleid) TITANIUM_NOEXCEPT;

		void updateGeoLocation();

		Annotation(const JSContext&) TITANIUM_NOEXCEPT;
		~Annotation() TITANIUM_NOEXCEPT;
		Annotation(const Annotation&) = default;
		Annotation& operator=(const Annotation&) = default;
#ifdef TITANIUM_MOVE_CTOR_AND_ASSIGN_DEFAULT_ENABLE
		Annotation(Annotation&&)                 = default;
		Annotation& operator=(Annotation&&)      = default;
#endif

		static void JSExportInitialize();

		// properties
		TITANIUM_PROPERTY_DEF(canShowCallout);
		TITANIUM_PROPERTY_DEF(centerOffset);
		TITANIUM_PROPERTY_DEF(customView);
		TITANIUM_PROPERTY_DEF(draggable);
		TITANIUM_PROPERTY_DEF(image);
		TITANIUM_PROPERTY_DEF(latitude);
		TITANIUM_PROPERTY_DEF(leftButton);
		TITANIUM_PROPERTY_DEF(leftView);
		TITANIUM_PROPERTY_DEF(longitude);
		TITANIUM_PROPERTY_DEF(pincolor);
		TITANIUM_PROPERTY_DEF(rightButton);
		TITANIUM_PROPERTY_DEF(rightView);
		TITANIUM_PROPERTY_DEF(showInfoWindow);
		TITANIUM_PROPERTY_DEF(subtitle);
		TITANIUM_PROPERTY_DEF(subtitleid);
		TITANIUM_PROPERTY_DEF(title);
		TITANIUM_PROPERTY_DEF(titleid);

		// methods
		TITANIUM_FUNCTION_DEF(getCanShowCallout);
		TITANIUM_FUNCTION_DEF(setCanShowCallout);
		TITANIUM_FUNCTION_DEF(getCenterOffset);
		TITANIUM_FUNCTION_DEF(setCenterOffset);
		TITANIUM_FUNCTION_DEF(getCustomView);
		TITANIUM_FUNCTION_DEF(setCustomView);
		TITANIUM_FUNCTION_DEF(getDraggable);
		TITANIUM_FUNCTION_DEF(setDraggable);
		TITANIUM_FUNCTION_DEF(getImage);
		TITANIUM_FUNCTION_DEF(setImage);
		TITANIUM_FUNCTION_DEF(getLatitude);
		TITANIUM_FUNCTION_DEF(setLatitude);
		TITANIUM_FUNCTION_DEF(getLeftButton);
		TITANIUM_FUNCTION_DEF(setLeftButton);
		TITANIUM_FUNCTION_DEF(getLeftView);
		TITANIUM_FUNCTION_DEF(setLeftView);
		TITANIUM_FUNCTION_DEF(getLongitude);
		TITANIUM_FUNCTION_DEF(setLongitude);
		TITANIUM_FUNCTION_DEF(getPincolor);
		TITANIUM_FUNCTION_DEF(setPincolor);
		TITANIUM_FUNCTION_DEF(getRightButton);
		TITANIUM_FUNCTION_DEF(setRightButton);
		TITANIUM_FUNCTION_DEF(getRightView);
		TITANIUM_FUNCTION_DEF(setRightView);
		TITANIUM_FUNCTION_DEF(getShowInfoWindow);
		TITANIUM_FUNCTION_DEF(setShowInfoWindow);
		TITANIUM_FUNCTION_DEF(getSubtitle);
		TITANIUM_FUNCTION_DEF(setSubtitle);
		TITANIUM_FUNCTION_DEF(getSubtitleid);
		TITANIUM_FUNCTION_DEF(setSubtitleid);
		TITANIUM_FUNCTION_DEF(getTitle);
		TITANIUM_FUNCTION_DEF(setTitle);
		TITANIUM_FUNCTION_DEF(getTitleid);
		TITANIUM_FUNCTION_DEF(setTitleid);

#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
		Windows::UI::Xaml::Controls::Grid^ GetMapIcon() {
			return mapicon__;
		}
#endif

		protected:
#pragma warning(push)
#pragma warning(disable : 4251)
			bool canShowCallout__;
			Titanium::UI::Point centerOffset__;
			TiView_shared_ptr_t customView__;
			bool draggable__;
			std::string image__;
			double latitude__;
			std::string leftButton__;
			TiView_shared_ptr_t leftView__;
			double longitude__;
			ANNOTATION_COLOR pincolor__;
			std::string rightButton__;
			TiView_shared_ptr_t rightView__;
			bool showInfoWindow__;
			std::string subtitle__;
			std::string subtitleid__;
			std::string title__;
			std::string titleid__;
#pragma warning(pop)

		private:

#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			Windows::UI::Xaml::Controls::Grid^ mapicon__ = { nullptr };

			Windows::UI::Xaml::Shapes::Line^ pin__ = { nullptr };
			Windows::UI::Xaml::Shapes::Ellipse^ icon__ = { nullptr };
			Windows::UI::Xaml::Controls::TextBlock^ text__ = { nullptr };
#endif
		};
	}
}
#endif // _MAP_ANNOTATION_HPP_
