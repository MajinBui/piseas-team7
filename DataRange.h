#ifndef _DATARANGE_
#define _DATARANGE_
class DataRange {
	float min;
	float max;
	bool autoRegulate;

public:
	DataRange() : min(0), max(0), autoRegulate(0) {}
	void setData(float, float, bool);
	float getMin();
	float getMax();
	bool getAutoRegulate();
};
#endif