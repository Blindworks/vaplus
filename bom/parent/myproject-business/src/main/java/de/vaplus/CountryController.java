package de.vaplus;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.CountryControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.StateControllerInterface;
import de.vaplus.api.entity.Country;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.eao.CountryEao;
import de.vaplus.client.eao.ShopEao;
import de.vaplus.client.eao.StateEao;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.ShopAliasEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.ShopGroupEntity;
import de.vaplus.client.entity.VOEntity;


@Stateless
public class CountryController implements CountryControllerInterface {

	private static final long serialVersionUID = -3451195151358623430L;
	
	@Inject
    private CountryEao countryEao;

	@Override
	public List<? extends Country> getCountryList(){
		return countryEao.getCountryList();
	}

	@Override
	public Country getCountryById(long id) {
		return countryEao.getCountryById(id);
	}
}
