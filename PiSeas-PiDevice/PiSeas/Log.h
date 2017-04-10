#ifndef _LOG_
#define _LOG_
#include <string>
#include <ctime>

#define ACT "ACT"
#define MANACT "MANACT"
#define NOT "NOT"

#define TEMPOUTOFBOUNDS "TEMPRANGE"
#define PHOUTOFBOUNDS "PHRANGE"
#define WATERCHANGEREQ "WATERCHANGE"

#define FEEDING "FEEDACTIVE"
#define DRAINING "DAILYDRAIN"
#define FILLING "DAILYFILL"
#define LIGHTON "LIGHTON"
#define LIGHTOFF "LIGHTOFF"
#define HEATERON "HEATERON"
#define HEATEROFF "HEATEROFF"
#define FANON "FANON"
#define FANOFF "FANOFF"
#define INPUMPON "INPUMPON"
#define INPUMPOFF "INPUMPOFF"
#define OUTPUMPON "OUTPUMPON"
#define OUTPUMPOFF "OUTPUMPOFF"

class Log{
	bool used = false;
	std::string dateTime;
	std::string desc;
	std::string type;

public:
	Log(){}
	Log(std::string& dT, std::string d, std::string t) : dateTime(dT), desc(d), type(t){}
	void setUsed(bool u) { used = u; }
	bool getUsed() { return used; }
	std::string getDateTime() { return dateTime; }
	void setDateTime() { dateTime = Log::getCurTimeFormatted();	}
	std::string getDesc() { return desc; }
	void setDesc(std::string d) { desc = d; }
	std::string getType() { return type; }
	void setType(std::string t) { type = t; }
	static std::string getCurTimeFormatted(){
		struct tm curTime;
		time_t t = std::time(0);
		char buff[25];
		
		curTime = *localtime(&t);
		strftime(buff, 25, "%Y-%m-%dT%H:%M:%S%z", &curTime);
		
		std::string temp(buff);
		
		return temp;
	}
};
#endif
