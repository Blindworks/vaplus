package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.State;

public interface StateControllerInterface extends Serializable {

	List<? extends State> getStateList();

	State getStateById(long id);

}
