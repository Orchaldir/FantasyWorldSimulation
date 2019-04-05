package jfws.feature.world.attribute;

public interface AttributeCell {
	double getAttribute(int index);

	void setAttribute(int index, double value);

	void addToAttribute(int index, double value);
}
