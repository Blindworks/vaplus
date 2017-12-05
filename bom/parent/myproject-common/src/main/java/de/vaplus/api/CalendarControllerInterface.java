package de.vaplus.api;

import java.io.Serializable;
import java.io.Writer;

import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;

public interface CalendarControllerInterface extends Serializable{

	void printUserCalendar(Writer out, User user) throws Exception;

	void printShopCalendar(Writer out, Shop shop) throws Exception;

}
