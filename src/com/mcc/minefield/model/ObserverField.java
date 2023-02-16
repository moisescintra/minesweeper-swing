package com.mcc.minefield.model;

@FunctionalInterface
public interface ObserverField {
	void eventOccurred( Field field, EventField event);
	//Can be => BiConsumer< Field, EventField>
}
