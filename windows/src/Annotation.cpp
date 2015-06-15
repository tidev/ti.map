/**
 * Titanium.Map.Annotation for Windows
 *
 * Copyright (c) 2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License.
 * Please see the LICENSE included with this distribution for details.
 */

#include "Map/Annotation.hpp"
#include "TitaniumWindows/Utility.hpp"

#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
using namespace Windows::UI::Xaml::Controls::Maps;
using namespace Windows::Devices::Geolocation;
#endif

namespace Ti
{
	namespace Map
	{
		void Annotation::set_latitude(const double& latitude) TITANIUM_NOEXCEPT
		{
			latitude__ = latitude;
			updateGeoLocation();
		}

		void Annotation::set_longitude(const double& longitude) TITANIUM_NOEXCEPT
		{
			longitude__ = longitude;
			updateGeoLocation();
		}

		void Annotation::set_pincolor(const ANNOTATION_COLOR& pincolor) TITANIUM_NOEXCEPT
		{
			pincolor__ = pincolor;
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			if (pincolor == ANNOTATION_COLOR::AZURE) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Azure);
			} else if (pincolor == ANNOTATION_COLOR::BLUE) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Blue);
			} else if (pincolor == ANNOTATION_COLOR::CYAN) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Cyan);
			} else if (pincolor == ANNOTATION_COLOR::GREEN) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Green);
			} else if (pincolor == ANNOTATION_COLOR::MAGENTA) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Magenta);
			} else if (pincolor == ANNOTATION_COLOR::ORANGE) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Orange);
			} else if (pincolor == ANNOTATION_COLOR::PURPLE) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Purple);
			} else if (pincolor == ANNOTATION_COLOR::RED) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Red);
			} else if (pincolor == ANNOTATION_COLOR::ROSE) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::MistyRose); // FIXME What is a good color to match here?
			} else if (pincolor == ANNOTATION_COLOR::VIOLET) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Violet);
			} else if (pincolor == ANNOTATION_COLOR::YELLOW) {
				icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Yellow);
			}
#endif
		}

		void Annotation::updateGeoLocation() {
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			BasicGeoposition bgp = { get_latitude(), get_longitude() };
			MapControl::SetLocation(mapicon__, ref new Geopoint(bgp));
			MapControl::SetNormalizedAnchorPoint(mapicon__, Windows::Foundation::Point(0.5f, 0.75f));
#endif
		}

		void Annotation::set_title(const std::string& title) TITANIUM_NOEXCEPT
		{
			title__ = title;
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			text__->Text = TitaniumWindows::Utility::ConvertString(title);
#endif
		}

		Annotation::Annotation(const JSContext& js_context) TITANIUM_NOEXCEPT
			: JSExportObject(js_context),
			canShowCallout__(true),
			draggable__(false),
			latitude__(0),
			longitude__(0),
			pincolor__(ANNOTATION_COLOR::AZURE),
			showInfoWindow__(true),
			subtitle__(""),
			subtitleid__(""),
			title__(""),
			titleid__("")
		{
			TITANIUM_LOG_DEBUG("Annotation::ctor Initialize");
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			mapicon__ = ref new Windows::UI::Xaml::Controls::Grid();

			// Draw pin
			pin__ = ref new Windows::UI::Xaml::Shapes::Line();
			pin__->Stroke = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Black);
			pin__->StrokeThickness = 2;
			pin__->X1 = 1;
			pin__->X2 = pin__->X1;
			pin__->Y1 = 20;
			pin__->Y2 = 30;
			pin__->HorizontalAlignment = Windows::UI::Xaml::HorizontalAlignment::Center;
			pin__->VerticalAlignment = Windows::UI::Xaml::VerticalAlignment::Center;
			mapicon__->Children->Append(pin__);

			// Draw circle
			icon__ = ref new Windows::UI::Xaml::Shapes::Ellipse();
			icon__->Fill = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Red);
			icon__->Stroke = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Black);
			icon__->StrokeThickness = 1;
			icon__->Width = 16;
			icon__->Height = icon__->Width;
			mapicon__->Children->Append(icon__);

			// Draw text
			text__ = ref new Windows::UI::Xaml::Controls::TextBlock();
			//text__->Text = TitaniumWindows::Utility::ConvertUTF8String(get_title());
			text__->FontSize = 14;
			text__->FontWeight = Windows::UI::Text::FontWeights::Bold;
			text__->Foreground = ref new Windows::UI::Xaml::Media::SolidColorBrush(Windows::UI::Colors::Black);
			text__->HorizontalAlignment = Windows::UI::Xaml::HorizontalAlignment::Center;
			text__->VerticalAlignment = Windows::UI::Xaml::VerticalAlignment::Top;
			text__->Height = 62;
			mapicon__->Children->Append(text__);
#endif
		}

		Annotation::~Annotation()
		{
#if WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP
			mapicon__ = nullptr;
#endif
		}

		bool Annotation::get_canShowCallout() const TITANIUM_NOEXCEPT
		{
			return canShowCallout__;
		}

		void Annotation::set_canShowCallout(const bool& canShowCallout) TITANIUM_NOEXCEPT
		{
			canShowCallout__ = canShowCallout;
		}

		Titanium::UI::Point Annotation::get_centerOffset() const TITANIUM_NOEXCEPT
		{
			return centerOffset__;
		}

		void Annotation::set_centerOffset(const Titanium::UI::Point& centerOffset) TITANIUM_NOEXCEPT
		{
			centerOffset__ = centerOffset;
		}

		TiView_shared_ptr_t Annotation::get_customView() const TITANIUM_NOEXCEPT
		{
			return customView__;
		}

		void Annotation::set_customView(const TiView_shared_ptr_t& customView) TITANIUM_NOEXCEPT
		{
			customView__ = customView;
		}

		bool Annotation::get_draggable() const TITANIUM_NOEXCEPT
		{
			return draggable__;
		}

		void Annotation::set_draggable(const bool& draggable) TITANIUM_NOEXCEPT
		{
			draggable__ = draggable;
		}

		std::string Annotation::get_image() const TITANIUM_NOEXCEPT
		{
			return image__;
		}

		void Annotation::set_image(const std::string& image) TITANIUM_NOEXCEPT
		{
			image__ = image;
		}

		double Annotation::get_latitude() const TITANIUM_NOEXCEPT
		{
			return latitude__;
		}

		std::string Annotation::get_leftButton() const TITANIUM_NOEXCEPT
		{
			return leftButton__;
		}

		void Annotation::set_leftButton(const std::string& leftButton) TITANIUM_NOEXCEPT
		{
			leftButton__ = leftButton;
		}

		TiView_shared_ptr_t Annotation::get_leftView() const TITANIUM_NOEXCEPT
		{
			return leftView__;
		}

		void Annotation::set_leftView(const TiView_shared_ptr_t& leftView) TITANIUM_NOEXCEPT
		{
			leftView__ = leftView;
		}

		double Annotation::get_longitude() const TITANIUM_NOEXCEPT
		{
			return longitude__;
		}

		ANNOTATION_COLOR Annotation::get_pincolor() const TITANIUM_NOEXCEPT
		{
			return pincolor__;
		}

		std::string Annotation::get_rightButton() const TITANIUM_NOEXCEPT
		{
			return rightButton__;
		}

		void Annotation::set_rightButton(const std::string& rightButton) TITANIUM_NOEXCEPT
		{
			rightButton__ = rightButton;
		}

		TiView_shared_ptr_t Annotation::get_rightView() const TITANIUM_NOEXCEPT
		{
			return rightView__;
		}

		void Annotation::set_rightView(const TiView_shared_ptr_t& rightView) TITANIUM_NOEXCEPT
		{
			rightView__ = rightView;
		}

		bool Annotation::get_showInfoWindow() const TITANIUM_NOEXCEPT
		{
			return showInfoWindow__;
		}

		void Annotation::set_showInfoWindow(const bool& showInfoWindow) TITANIUM_NOEXCEPT
		{
			showInfoWindow__ = showInfoWindow;
		}

		std::string Annotation::get_subtitle() const TITANIUM_NOEXCEPT
		{
			return subtitle__;
		}

		void Annotation::set_subtitle(const std::string& subtitle) TITANIUM_NOEXCEPT
		{
			subtitle__ = subtitle;
		}

		std::string Annotation::get_subtitleid() const TITANIUM_NOEXCEPT
		{
			return subtitleid__;
		}

		void Annotation::set_subtitleid(const std::string& subtitleid) TITANIUM_NOEXCEPT
		{
			subtitleid__ = subtitleid;
		}

		std::string Annotation::get_title() const TITANIUM_NOEXCEPT
		{
			return title__;
		}

		std::string Annotation::get_titleid() const TITANIUM_NOEXCEPT
		{
			return titleid__;
		}

		void Annotation::set_titleid(const std::string& titleid) TITANIUM_NOEXCEPT
		{
			titleid__ = titleid;
		}

		void Annotation::JSExportInitialize()
		{
			JSExport<Annotation>::SetClassVersion(1);
			JSExport<Annotation>::SetParent(JSExport<JSExportObject>::Class());
			// Properties
			TITANIUM_ADD_PROPERTY(Annotation, canShowCallout);
			TITANIUM_ADD_PROPERTY(Annotation, centerOffset);
			TITANIUM_ADD_PROPERTY(Annotation, customView);
			TITANIUM_ADD_PROPERTY(Annotation, draggable);
			TITANIUM_ADD_PROPERTY(Annotation, image);
			TITANIUM_ADD_PROPERTY(Annotation, latitude);
			TITANIUM_ADD_PROPERTY(Annotation, leftButton);
			TITANIUM_ADD_PROPERTY(Annotation, leftView);
			TITANIUM_ADD_PROPERTY(Annotation, longitude);
			TITANIUM_ADD_PROPERTY(Annotation, pincolor);
			TITANIUM_ADD_PROPERTY(Annotation, rightButton);
			TITANIUM_ADD_PROPERTY(Annotation, rightView);
			TITANIUM_ADD_PROPERTY(Annotation, showInfoWindow);
			TITANIUM_ADD_PROPERTY(Annotation, subtitle);
			TITANIUM_ADD_PROPERTY(Annotation, subtitleid);
			TITANIUM_ADD_PROPERTY(Annotation, title);
			TITANIUM_ADD_PROPERTY(Annotation, titleid);
			// Methods
			TITANIUM_ADD_FUNCTION(Annotation, getCanShowCallout);
			TITANIUM_ADD_FUNCTION(Annotation, setCanShowCallout);
			TITANIUM_ADD_FUNCTION(Annotation, getCenterOffset);
			TITANIUM_ADD_FUNCTION(Annotation, setCenterOffset);
			TITANIUM_ADD_FUNCTION(Annotation, getCustomView);
			TITANIUM_ADD_FUNCTION(Annotation, setCustomView);
			TITANIUM_ADD_FUNCTION(Annotation, getDraggable);
			TITANIUM_ADD_FUNCTION(Annotation, setDraggable);
			TITANIUM_ADD_FUNCTION(Annotation, getImage);
			TITANIUM_ADD_FUNCTION(Annotation, setImage);
			TITANIUM_ADD_FUNCTION(Annotation, getLatitude);
			TITANIUM_ADD_FUNCTION(Annotation, setLatitude);
			TITANIUM_ADD_FUNCTION(Annotation, getLeftButton);
			TITANIUM_ADD_FUNCTION(Annotation, setLeftButton);
			TITANIUM_ADD_FUNCTION(Annotation, getLeftView);
			TITANIUM_ADD_FUNCTION(Annotation, setLeftView);
			TITANIUM_ADD_FUNCTION(Annotation, getLongitude);
			TITANIUM_ADD_FUNCTION(Annotation, setLongitude);
			TITANIUM_ADD_FUNCTION(Annotation, getPincolor);
			TITANIUM_ADD_FUNCTION(Annotation, setPincolor);
			TITANIUM_ADD_FUNCTION(Annotation, getRightButton);
			TITANIUM_ADD_FUNCTION(Annotation, setRightButton);
			TITANIUM_ADD_FUNCTION(Annotation, getRightView);
			TITANIUM_ADD_FUNCTION(Annotation, setRightView);
			TITANIUM_ADD_FUNCTION(Annotation, getSubtitle);
			TITANIUM_ADD_FUNCTION(Annotation, setSubtitle);
			TITANIUM_ADD_FUNCTION(Annotation, getSubtitleid);
			TITANIUM_ADD_FUNCTION(Annotation, setSubtitleid);
			TITANIUM_ADD_FUNCTION(Annotation, getTitle);
			TITANIUM_ADD_FUNCTION(Annotation, setTitle);
			TITANIUM_ADD_FUNCTION(Annotation, getTitleid);
			TITANIUM_ADD_FUNCTION(Annotation, setTitleid);
		}

		TITANIUM_PROPERTY_GETTER(Annotation, canShowCallout)
		{
			return get_context().CreateBoolean(get_canShowCallout());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, canShowCallout)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_canShowCallout(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, centerOffset)
		{
			return Titanium::UI::Point_to_js(get_context(), get_centerOffset());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, centerOffset)
		{
			TITANIUM_ASSERT(argument.IsObject());
			set_centerOffset(Titanium::UI::js_to_Point(static_cast<JSObject>(argument)));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, customView)
		{
			return get_customView()->get_object();
		}

		TITANIUM_PROPERTY_SETTER(Annotation, customView)
		{
			TITANIUM_ASSERT(argument.IsObject());
			set_customView(static_cast<JSObject>(argument).GetPrivate<Titanium::UI::View>());
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, draggable)
		{
			return get_context().CreateBoolean(get_draggable());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, draggable)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_draggable(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, image)
		{
			return get_context().CreateString(get_image());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, image)
		{
			TITANIUM_ASSERT(argument.IsString());
			set_image(static_cast<std::string>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, latitude)
		{
			return get_context().CreateNumber(get_latitude());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, latitude)
		{
			TITANIUM_ASSERT(argument.IsNumber());
			set_latitude(static_cast<double>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, leftButton)
		{
			return get_context().CreateString(get_leftButton());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, leftButton)
		{
			TITANIUM_ASSERT(argument.IsString());
			set_leftButton(static_cast<std::string>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, leftView)
		{
			return get_leftView()->get_object();
		}

		TITANIUM_PROPERTY_SETTER(Annotation, leftView)
		{
			TITANIUM_ASSERT(argument.IsObject());
			set_leftView(static_cast<JSObject>(argument).GetPrivate<Titanium::UI::View>());
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, longitude)
		{
			return get_context().CreateNumber(get_longitude());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, longitude)
		{
			TITANIUM_ASSERT(argument.IsNumber());
			set_longitude(static_cast<double>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, pincolor)
		{
			return get_context().CreateNumber(Constants::to_underlying_type(get_pincolor()));
		}

		TITANIUM_PROPERTY_SETTER(Annotation, pincolor)
		{
			TITANIUM_ASSERT(argument.IsNumber());
			set_pincolor(Constants::to_ANNOTATION_COLOR(static_cast<std::underlying_type<ANNOTATION_COLOR>::type>(argument)));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, rightButton)
		{
			return get_context().CreateString(get_rightButton());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, rightButton)
		{
			TITANIUM_ASSERT(argument.IsString());
			set_rightButton(static_cast<std::string>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, rightView)
		{
			return get_rightView()->get_object();
		}

		TITANIUM_PROPERTY_SETTER(Annotation, rightView)
		{
			TITANIUM_ASSERT(argument.IsObject());
			set_rightView(static_cast<JSObject>(argument).GetPrivate<Titanium::UI::View>());
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, showInfoWindow)
		{
			return get_context().CreateBoolean(get_showInfoWindow());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, showInfoWindow)
		{
			TITANIUM_ASSERT(argument.IsBoolean());
			set_showInfoWindow(static_cast<bool>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, subtitle)
		{
			return get_context().CreateString(get_subtitle());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, subtitle)
		{
			TITANIUM_ASSERT(argument.IsString());
			set_subtitle(static_cast<std::string>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, subtitleid)
		{
			return get_context().CreateString(get_subtitleid());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, subtitleid)
		{
			TITANIUM_ASSERT(argument.IsString());
			set_subtitleid(static_cast<std::string>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, title)
		{
			return get_context().CreateString(get_title());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, title)
		{
			TITANIUM_ASSERT(argument.IsString());
			set_title(static_cast<std::string>(argument));
			return true;
		}

		TITANIUM_PROPERTY_GETTER(Annotation, titleid)
		{
			return get_context().CreateString(get_titleid());
		}

		TITANIUM_PROPERTY_SETTER(Annotation, titleid)
		{
			TITANIUM_ASSERT(argument.IsString());
			set_titleid(static_cast<std::string>(argument));
			return true;
		}

		TITANIUM_FUNCTION(Annotation, getCanShowCallout)
		{
			return js_get_canShowCallout();
		}

		TITANIUM_FUNCTION(Annotation, setCanShowCallout)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_canShowCallout(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getCenterOffset)
		{
			return js_get_centerOffset();
		}

		TITANIUM_FUNCTION(Annotation, setCenterOffset)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_centerOffset(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getCustomView)
		{
			return js_get_customView();
		}

		TITANIUM_FUNCTION(Annotation, setCustomView)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_customView(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getDraggable)
		{
			return js_get_draggable();
		}

		TITANIUM_FUNCTION(Annotation, setDraggable)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_draggable(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getImage)
		{
			return js_get_image();
		}

		TITANIUM_FUNCTION(Annotation, setImage)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_image(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getLatitude)
		{
			return js_get_latitude();
		}

		TITANIUM_FUNCTION(Annotation, setLatitude)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_latitude(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getLeftButton)
		{
			return js_get_leftButton();
		}

		TITANIUM_FUNCTION(Annotation, setLeftButton)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_leftButton(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getLeftView)
		{
			return js_get_leftView();
		}

		TITANIUM_FUNCTION(Annotation, setLeftView)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_leftView(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getLongitude)
		{
			return js_get_longitude();
		}

		TITANIUM_FUNCTION(Annotation, setLongitude)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_longitude(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getPincolor)
		{
			return js_get_pincolor();
		}

		TITANIUM_FUNCTION(Annotation, setPincolor)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_pincolor(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getRightButton)
		{
			return js_get_rightButton();
		}

		TITANIUM_FUNCTION(Annotation, setRightButton)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_rightButton(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getRightView)
		{
			return js_get_rightView();
		}

		TITANIUM_FUNCTION(Annotation, setRightView)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_rightView(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getShowInfoWindow)
		{
			return js_get_showInfoWindow();
		}

		TITANIUM_FUNCTION(Annotation, setShowInfoWindow)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_showInfoWindow(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getSubtitle)
		{
			return js_get_subtitle();
		}

		TITANIUM_FUNCTION(Annotation, setSubtitle)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_subtitle(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getSubtitleid)
		{
			return js_get_subtitleid();
		}

		TITANIUM_FUNCTION(Annotation, setSubtitleid)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_subtitleid(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getTitle)
		{
			return js_get_title();
		}

		TITANIUM_FUNCTION(Annotation, setTitle)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_title(arguments.at(0));
			
			return get_context().CreateUndefined();
		}

		TITANIUM_FUNCTION(Annotation, getTitleid)
		{
			return js_get_titleid();
		}

		TITANIUM_FUNCTION(Annotation, setTitleid)
		{
			TITANIUM_ASSERT(arguments.size() == 1);
			
			js_set_titleid(arguments.at(0));
			
			return get_context().CreateUndefined();
		}
	}
}
