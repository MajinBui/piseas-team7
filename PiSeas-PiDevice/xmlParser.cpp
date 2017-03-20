#include "xmlParser.h"
#include <iostream>
#include <iomanip>
#include <stdlib.h>     /* atof */

using namespace rapidxml;

void XmlParser::updateLightSchedule(LightSchedule &ls){
	//if(ls.getSchedule().empty()){
	//	ls.setAutoRegulate(true);
	//	struct tm myTm;
	//	time_t t;
	//	int min;
	//
	//	t = std::time(0);
	//	myTm = *localtime(&t);
	//	min = myTm.tm_min;
	//	std::cout
	//	<< "Light Schedule" << std::endl
	//	<< "==============" << std::endl;
	//
	//	for (int i = 0; myTm.tm_min < 59; i++) {
	//		myTm.tm_min = min + i;
	//		bool state;
	//		if(i%2 == 0)
	//			state = false;
	//		else
	//			state = true;
	//		ls.addLightAction(myTm, state);
	//		std::cout
	//		<< myTm.tm_hour << ":"
	//		<< myTm.tm_min
	//		<< "    State = "
	//		<< state << std::endl;
	//	}	
	//}
}

void XmlParser::updateTemperatureRange(TempData &td){
	//if(td.getMin() == 0){
	//	float min = 20;
	//	float max = 30;
	//	bool autoReg = true;

	//	td.setTempData(min, max, autoReg);
	//	std::cout << std::endl << std::fixed << std::setprecision(2)
	//		<< "Temperature Range:" << std::endl
	//		<< "==================" << std::endl
	//		<< "Min      = " << min << "°C" << std::endl
	//		<< "Max      = " << max << "°C" << std::endl << std::endl;
	//}
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
	return piNode->first_node()->first_attribute(attribute)->value();
}

std::tm XmlParser::getSettingsDate() {
	std::string date = parser("Date", "date");
	// 2017-02-20T19:19:19+0500

	std::string delimiter = "-";
	int sta = 0;
	int fin = date.find(delimiter);

	std::string year = date.substr(sta, fin);
	sta = date.find(delimiter, fin)+1;
	fin = date.find(delimiter, sta);
	
	std::string month = date.substr(sta, fin-sta);
	sta = date.find(delimiter, fin) + 1;
	delimiter = "T";
	fin = date.find(delimiter, sta);

	std::string day = date.substr(sta, fin-sta);
	sta = date.find(delimiter, fin) + 1;
	delimiter = ":";
	fin = date.find(delimiter, sta);

	std::string hr = date.substr(sta, fin - sta);
	sta = date.find(delimiter, fin) + 1;
	fin = date.find(delimiter, sta);

	std::string min = date.substr(sta, fin - sta);
	sta = date.find(delimiter, fin) + 1;
	fin = date.length() - 5;

	std::string sec = date.substr(sta, fin - sta);
	
	delimiter = date.substr(date.length() - 5, 1);
	std::string tmhr = date.substr(date.length() - 4, 2);
	std::string tmmin = date.substr(date.length() - 2, 2);


	struct std::tm time;
	time.tm_hour = 1;
	return time;
}

bool* XmlParser::getSettingsUpdated() {
	bool* updates = new bool[6];
	updates[0] = (parser("Update", "feed") == "true") ? true : false;
	updates[1] = (parser("Update", "light") == "true") ? true : false;
	updates[2] = (parser("Update", "temperature") == "true") ? true : false;
	updates[3] = (parser("Update", "pump") == "true") ? true : false;
	updates[4] = (parser("Update", "pH") == "true") ? true : false;
	updates[5] = (parser("Update", "conductivity") == "true") ? true : false;
	return updates;
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
	return (parser("Tank", "type") == "true") ? true : false;
}

bool XmlParser::getSettingsAutoFeed() {

	return true;
}

FeedSchedule XmlParser::getSettingsFeedSchedule() {
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
	bool autoFeed = (piNode->first_attribute("auto")->value() == "true") ? true : false;

	FeedSchedule fs;
	fs.setAutoFeed(autoFeed);

	piNode = piNode->first_node();
	for (int i = 0; i < size; i++, piNode->next_sibling()) {
		int hr = atoi(piNode->first_attribute("hr")->value());
		int min = atoi(piNode->first_attribute("min")->value());

		struct std::tm time;
		time.tm_hour = hr;
		time.tm_min = min;

		FeedAction feed(time);
		
		xml_attribute<>* atts = piNode->first_attribute("Mon");
		for (int n = 0; n < 7; n++, atts = atts->next_attribute()) {
			std::string val = atts->value();
			feed.setWeek(n, (val == "true") ? true : false);
		}

			
		fs.addFeedAction(feed);

	}

	return fs;
}

bool XmlParser::getSettingsManualLight() {

	return true;
}

bool XmlParser::getSettingsAutoLight() {

	return true;
}

LightSchedule XmlParser::getSettingsLightSchedule() {
	rapidxml::file<> xmlFile(mobileName);
	xml_document<> doc;										// character type defaults to char
	doc.parse<0>(xmlFile.data());							// 0 means default parse flags

	std::ifstream theFile(mobileName);

	std::vector<char> buffer((std::istreambuf_iterator<char>(theFile)), std::istreambuf_iterator<char>());
	buffer.push_back('\0');

	doc.parse<0>(&buffer[0]);

	// Find root node
	xml_node<> * root_node = doc.first_node("Piseas");
	xml_node<>* piNode = root_node->first_node("Light");

	int size = atoi(piNode->first_attribute("schedules")->value());
	bool autoRegulate = (piNode->first_attribute("auto")->value() == "true") ? true : false;
	bool manual = (piNode->first_attribute("manual")->value() == "true") ? true : false; //-----------

	LightSchedule ls;
	ls.setAutoRegulate(autoRegulate);

	piNode = piNode->first_node();
	for (int i = 0; i < size; i++, piNode->next_sibling()) {
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
	
	return ls;
}

float XmlParser::getSettingsMinTemp() {
	return stof(parser("Temperature", "min"));
}

float XmlParser::getSettingsMaxTemp() {
	return stof(parser("Temperature", "max"));
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
