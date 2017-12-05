package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserPayroll;

public interface PayrollControllerInterface extends Serializable{

	List<? extends UserPayroll> getUserPayrollList(int month, int year);

	UserPayroll getUserPayroll(User user, int month, int year);

	UserPayroll factoryNewUserPayroll(User user, int month, int year);

	void generateUserPayrolls(int month, int year);

	UserPayroll saveUserPayroll(UserPayroll userPayroll);

}
