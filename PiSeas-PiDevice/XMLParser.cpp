#include "xmlParser.h"
#include <iostream>
#include <iomanip>
#include <stdlib.h>     /* atof */

using namespace rapidxml;

void XmlParser::updateLightSchedule(LightSchedule &ls){
	if (getSettingsUpdated("light")) {

		std::list<LightAction> lists = ls.getSchedule();
		lists.clear();

		rapidxml::file<> xmlFile(mobileName);
		xml_document<> doc;										// character type defaults to char
		doc.parse<0>(xmlFile.data());							// 0 means default parse flags

		std::ifstream theFile(mobileName);

		std::vector<char> buffer((std::istreambuf_iterator<char>(theFile)), std::istreambuf_iterator<char>());
		buffer.push_back('\0');

		doc.parse<0>(&buffer[0]);

		// Find root node
		xml_node<>* root_node = doc.first_node("Piseas");
		xml_node<>* piNode = root_node->first_node("Light");

		int count = atoi(piNode->first_attribute("schedules")->value());

		std::string tmp = piNode->first_attribute("auto")->value();
		bool autoRegulate = false;
		bool manual = false;
		if (tmp == "true")
			autoRegulate = true;
		tmp = piNode->first_attribute("manual")->value();
		if (tmp == "true")
			manual = true;

		//LightSchedule ls;
		ls.setAutoRegulate(autoRegulate);
		ls.setManual(manual);

		piNode = piNode->first_node();
		for (int i = 0; piNode!=nullptr; i++, piNode = piNode->next_sibling()) {
			int onHr = atoi(piNode->first_attribute("onHr")->value());
			int onMin = atoi(piNode->first_attribute("onMin")->value());
			int offHr = atoi(piNode->first_attribute("offHr")->value());
			int offMin = atoi(piNode->first_attribute("offMin")->value());

			struct std::tm on;
			struct std::tm off;
			on.tm_hour = onHr;
			on.tm_min = onMin;
			off.tm_hour = offHr;
			off.tm_min = offMin;

			ls.addLightAction(on, true);
			ls.addLightAction(off, false);
		}
	}
}

void XmlParser::updateFeedSchedule(FeedSchedule &fs) {
	if (getSettingsUpdated("feed")) {
		rapidxml::file<> xmlFile(mobileName);
		xml_document<> doc;										// character type defaults to char
		doc.parse<0>(xmlFile.data());							// 0 means default parse flags

		std::ifstream theFile(mobileName);

		std::vector<char> buffer((std::istreambuf_iterator<char>(theFile)), std::istreambuf_iterator<char>());
		buffer.push_back('\0');

		doc.parse<0>(&buffer[0]);

		// Find root node
		xml_node<>* root_node = doc.first_node("Piseas");
		xml_node<>* piNode = root_node->first_node("Feed");

		int size = atoi(piNode->first_attribute("schedules")->value());
		std::string tmp = piNode->first_attribute("auto")->value();
		bool autoFeed = false;
		bool manual = false;
		if (tmp == "true")
			autoFeed = true;
		tmp = piNode->first_attribute("manual")->value();
		if (tmp == "true")
			manual = true;

		//FeedSchedule fs;
		fs.setAutoFeed(autoFeed);
		

		piNode = piNode->first_node();
		for (int i = 0; piNode!=nullptr; i++, piNode = piNode->next_sibling()) {
			int hr = atoi(piNode->first_attribute("hr")->value());
			int min = atoi(piNode->first_attribute("min")->value());

			struct std::tm time;
			time.tm_hour = hr;
			time.tm_min = min;

			xml_attribute<>* atts = piNode->first_attribute("Mon");
			for (int n = 0; n < 7; n++, atts = atts->next_attribute()) {
				std::string temp = atts->value();
				std::string dundun = atts->name();
				if ((temp == "true")) {
					if (n == 6)
						time.tm_wday = 0;
					else
						time.tm_wday = n+1;
					fs.addFeedSchedule(time);
				}
				//std::string val = atts->value();
			}
		}
	}
}

void XmlParser::updateTemperatureRange(TempData &td){
	if (getSettingsUpdated("temperature")) {
		td.setMin(getSettingsMinTemp());
		td.setMax(getSettingsMaxTemp());
		td.setAutoRegulate(getSettingsTempAuto());
	}
}

void XmlParser::updateConductivityRange(Conductivity &con) {
	if (getSettingsUpdated("conductivity")) {
		con.setMin(getSettingsCMin());
		con.setMax(getSettingsCMax());
		con.setAutoRegulate(getSettingsCAuto());
	}
}

void XmlParser::updatePHRange(PH &ph) {
	if (getSettingsUpdated("pH")) {
		ph.setMin(getSettingsPHMin());
		ph.setMax(getSettingsPHMax());
		ph.setAutoRegulate(getSettingsPHAuto());
	}
}

void XmlParser::updateWaterState(WaterState &water) {
	if (getSettingsUpdated("pump")) {
		water.setManualDrain(getSettingsDrain());
		water.setManualFill(getSettingsFill());
		water.setAutoRegulate(getSettingsAutoWaterChange());
	}
}

std::string XmlParser::parser(const char* tag, const char* attribute) {
	rapidxml::file<> xmlFile(mobileName);
	xml_document<> doc;										// character type defaults to char
	doc.parse<0>(xmlFile.data());							// 0 means default parse flags

	std::ifstream theFile(mobileName);

	std::vector<char> buffer((std::istreambuf_iterator<char>(theFile)), std::istreambuf_iterator<char>());
	buffer.push_back('\0');

	doc.parse<0>(&buffer[0]);

	// Find root node
	xml_node<> * root_node = doc.first_node("Piseas");

	xml_node<>* piNode = root_node->first_node(tag);

	// return attribute from <Date.. /> tag
	if (tag == "Date") 
		return piNode->first_attribute(attribute)->value();
	
	// return attribute from <details.. /> tag
	std::string trytry = piNode->first_node()->first_attribute(attribute)->value();
	return piNode->first_node()->first_attribute(attribute)->value();
}

std::tm XmlParser::getSettingsDate() {
	std::string date = parser("Date", "date");
	// 2017-02-20T19:19:19+0500

	std::string delimiter = "-";
	int sta = 0;
	int fin = date.find(delimiter);

	int year = stoi(date.substr(sta, fin));
	sta = date.find(delimiter, fin)+1;
	fin = date.find(delimiter, sta);
	
	int month = stoi(date.substr(sta, fin-sta));
	sta = date.find(delimiter, fin) + 1;
	delimiter = "T";
	fin = date.find(delimiter, sta);

	int day = stoi(date.substr(sta, fin-sta));
	sta = date.find(delimiter, fin) + 1;
	delimiter = ":";
	fin = date.find(delimiter, sta);

	int hr = stoi(date.substr(sta, fin - sta));
	sta = date.find(delimiter, fin) + 1;
	fin = date.find(delimiter, sta);

	int min = stoi(date.substr(sta, fin - sta));
	sta = date.find(delimiter, fin) + 1;
	fin = date.length() - 5;

	int sec = stoi(date.substr(sta, fin - sta));
	
	delimiter = date.substr(date.length() - 5, 1);
	int tmhr = stoi(date.substr(date.length() - 4, 2));
	int tmmin = stoi(date.substr(date.length() - 2, 2));

	struct std::tm time;
	time.tm_year = year;
	time.tm_mon = month;
	time.tm_hour = hr;
	time.tm_min = min;
	time.tm_sec = sec;
	
	if (delimiter == "+") {
		time.tm_min += tmmin;
		time.tm_hour += tmhr;
	}
	else {
		time.tm_min -= tmmin;
		time.tm_hour -= tmhr;
	}

	return time;
}

bool XmlParser::getSettingsUpdated(const char* update) {
	return (parser("Update", update) == "true") ? true : false;
}

std::string XmlParser::getSettingsID() {
	return parser("Tank", "id");
}

std::string XmlParser::getSettingsName() {
	return parser("Tank", "name");
}

std::string XmlParser::getSettingsPassword() {
	return parser("Tank", "password");
}

float XmlParser::getSettingsSize() {
	return stof(parser("Tank", "size"));
}

std::string XmlParser::getSettingsDescription() {
	return parser("Tank", "description");
}

bool XmlParser::getSettingsType() {
	return (parser("Tank", "type") == "true") ? true : false;
}

bool XmlParser::getSettingsManualFeed() {
	return (parser("Feed", "manual") == "true") ? true : false;
}

bool XmlParser::getSettingsAutoFeed() {
	return (parser("Feed", "auto") == "true") ? true : false;
}

bool XmlParser::getSettingsManualLight() {
	return (parser("Light", "manual") == "true") ? true : false;
}

bool XmlParser::getSettingsAutoLight() {
	return (parser("Light", "auto") == "true") ? true : false;
}

float XmlParser::getSettingsMinTemp() {
	return stof(parser("Temperature", "min"));
}

float XmlParser::getSettingsMaxTemp() {
	return stof(parser("Temperature", "max"));
}

bool XmlParser::getSettingsTempAuto() {
	return (parser("Temperature", "auto") == "true") ? true : false;
}

bool XmlParser::getSettingsDrain() {
	return (parser("Pump", "manualDrain") == "true") ? true : false;
}

bool XmlParser::getSettingsFill() {
	return (parser("Pump", "manualFill") == "true") ? true : false;
}

bool XmlParser::getSettingsAutoWaterChange() {
	return (parser("Pump", "auto") == "true") ? true : false;
}

float XmlParser::getSettingsPHMin() {
	return stof(parser("PH", "pHmin"));
}

float XmlParser::getSettingsPHMax() {
	return stof(parser("PH", "pHmax"));
}

float XmlParser::getSettingsPHAuto() {
	return (parser("PH", "auto") == "true") ? true : false;
}

float XmlParser::getSettingsCMin() {
	return stof(parser("Conductivity", "cMin"));
}

float XmlParser::getSettingsCMax() {
	return stof(parser("Conductivity", "cMax"));
}

bool XmlParser::getSettingsCAuto() {
	return (parser("Conductivity", "auto") == "true") ? true : false;
}
