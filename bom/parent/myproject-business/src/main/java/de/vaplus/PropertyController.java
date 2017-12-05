package de.vaplus;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;

import de.vaplus.api.DBControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.entity.User;
import de.vaplus.client.eao.PropertyEao;
import de.vaplus.client.entity.PropertyEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.UserPropertyEntity;
import de.vaplus.interfaces.LocalPropertyControllerInterface;


@Singleton
public class PropertyController implements PropertyControllerInterface, LocalPropertyControllerInterface {

    /**
	 *
	 */
	private static final long serialVersionUID = 3720716552486426614L;

	private static final String appVerson = "0.7.0R4";

	@EJB
    private DBControllerInterface dbController;

	@Inject
    private PropertyEao eao;

    @Resource(name = "lizenseName")
    private String oldLicenseName;

    private String licenseName;

    private String baseFilePath;

    private long storageLimit;


    @Override
	public String getOldLicenseName() {
    	if(oldLicenseName == null)
    		oldLicenseName = "";
		return oldLicenseName;
	}

    @Override
	public String getLicenseName() {
		return licenseName;
	}

    @Override
	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

    @Override
	public String getBaseFilePath() {
		return baseFilePath;
	}

    @Override
	public void setBaseFilePath(String baseFilePath) {
		this.baseFilePath = baseFilePath;
	}

    @Override
	public long getStorageLimit() {
		return storageLimit;
	}

    @Override
	public void setStorageLimit(long storageLimit) {
		this.storageLimit = storageLimit;
	}

	@Override
    public String getStringProperty(String name, String defaultValue){
    	PropertyEntity property = eao.getProperty(name);

    	if(property == null){
    		property = new PropertyEntity();
    		property.setName(name);
    		property.setStringValue(defaultValue);

    		property = eao.saveProperty(property);

    	}

    	return property.getStringValue();
    }

	@Override
    public long getLongProperty(String name, long defaultValue){
    	PropertyEntity property = eao.getProperty(name);

    	if(property == null){
    		property = new PropertyEntity();
    		property.setName(name);
    		property.setLongValue(defaultValue);

    		property = eao.saveProperty(property);

    	}

    	return property.getLongValue();
    }

	@Override
    public BigDecimal getDecimalProperty(String name, BigDecimal defaultValue){
    	PropertyEntity property = eao.getProperty(name);

    	if(property == null){
    		property = new PropertyEntity();
    		property.setName(name);
    		property.setDecimalValue(defaultValue);

    		property = eao.saveProperty(property);

    	}

    	return property.getDecimalValue();
    }

	@Override
    public double getDoubleProperty(String name, double defaultValue){
    	PropertyEntity property = eao.getProperty(name);

    	if(property == null){
    		property = new PropertyEntity();
    		property.setName(name);
    		property.setDoubleValue(defaultValue);

    		property = eao.saveProperty(property);

    	}

    	return property.getDoubleValue();
    }

	@Override
	public void updateDoubleProperty(String name, double value) {
		PropertyEntity property = eao.getProperty(name);

    	if(property == null){
    		property = new PropertyEntity();
    		property.setName(name);
    	}

		property.setDoubleValue(value);
		property = eao.saveProperty(property);

    	return;
	}

	@Override
	public void updateDecimalProperty(String name, BigDecimal value) {
		PropertyEntity property = eao.getProperty(name);

    	if(property == null){
    		property = new PropertyEntity();
    		property.setName(name);
    	}

		property.setDecimalValue(value);
		property = eao.saveProperty(property);

    	return;
	}

	@Override
	public void updateLongProperty(String name, long value) {
		PropertyEntity property = eao.getProperty(name);

    	if(property == null){
    		property = new PropertyEntity();
    		property.setName(name);
    	}

		property.setLongValue(value);
		property = eao.saveProperty(property);

    	return;
	}

	@Override
	public void updateStringProperty(String name, String value) {
		PropertyEntity property = eao.getProperty(name);

    	if(property == null){
    		property = new PropertyEntity();
    		property.setName(name);
    	}

		property.setStringValue(value);
		property = eao.saveProperty(property);

    	return;
	}



	@Override
    public String getStringUserProperty(User user, String name, String defaultValue){
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    		property.setStringValue(defaultValue);

    		property = eao.saveUserProperty(property);

    	}

    	return property.getStringValue();
    }

	@Override
    public long getLongUserProperty(User user, String name, long defaultValue){
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    		property.setLongValue(defaultValue);

    		property = eao.saveUserProperty(property);

    	}

    	return property.getLongValue();
    }

	@Override
    public BigDecimal getDecimalUserProperty(User user, String name, BigDecimal defaultValue){
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    		property.setDecimalValue(defaultValue);

    		property = eao.saveUserProperty(property);

    	}

    	return property.getDecimalValue();
    }

	@Override
    public double getDoubleUserProperty(User user, String name, double defaultValue){
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    		property.setDoubleValue(defaultValue);

    		property = eao.saveUserProperty(property);

    	}

    	return property.getDoubleValue();
    }

	@Override
    public boolean getBooleanUserProperty(User user, String name, boolean defaultValue){
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    		property.setLongValue(defaultValue ? (long)1 : (long)0);

    		property = eao.saveUserProperty(property);

    	}

    	return property.getLongValue() == (long) 1 ? true : false;
    }

	@Override
	public void updateDoubleUserProperty(User user, String name, double value) {
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    	}

		property.setDoubleValue(value);
		property = eao.saveUserProperty(property);

    	return;
	}

	@Override
	public void updateDecimalUserProperty(User user, String name, BigDecimal value) {
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    	}

		property.setDecimalValue(value);
		property = eao.saveUserProperty(property);

    	return;
	}

	@Override
	public void updateLongUserProperty(User user, String name, long value) {
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    	}

		property.setLongValue(value);
		property = eao.saveUserProperty(property);

    	return;
	}

	@Override
	public void updateStringUserProperty(User user, String name, String value) {
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    	}

		property.setStringValue(value);
		property = eao.saveUserProperty(property);

    	return;
	}

	@Override
	public void updateBooleanUserProperty(User user, String name, boolean value) {
    	UserPropertyEntity property = eao.getUserProperty((UserEntity) user, name);

    	if(property == null){
    		property = new UserPropertyEntity();
    		property.setUser((UserEntity) user);
    		property.setName(name);
    	}

		property.setLongValue(value ? (long) 1 : (long) 0);
		property = eao.saveUserProperty(property);

    	return;
	}

	@Override
	public String getAppVerson() {
		return appVerson;
	}

	@Override
	public String getDBVersion() {
		return String.valueOf( getLongProperty(DBController.PROPERTY_DB_VERSION, 0) );
	}
}
