package de.vaplus.api.entity;

import java.util.Date;

public interface VodafoneCommissionAccountingItem extends VendorCommissionAccountingItem{


	public String getVoIDAbrechnung();

	public void setVoIDAbrechnung(String voIDAbrechnung);

	public String getVoIDAktivierung();

	public void setVoIDAktivierung(String voIDAktivierung);

	public String getAdressdatenVOIDAktivierung();

	public void setAdressdatenVOIDAktivierung(String adressdatenVOIDAktivierung);

	public String getProvisionskategorie();

	public void setProvisionskategorie(String provisionskategorie);

	public String getDatumAuftrag();

	public void setDatumAuftrag(String datumAuftrag);

	public String getDatumPreClearing();

	public void setDatumPreClearing(String datumPreClearing);

	public String getDatumAktivierung();

	public void setDatumAktivierung(String datumAktivierung);

	public String getDatumStornierung();

	public void setDatumStornierung(String datumStornierung);

	public String getKundenname();

	public void setKundenname(String kundenname);

	public String getRufnummerBarcodeWebOrderID();

	public void setRufnummerBarcodeWebOrderID(String rufnummerBarcodeWebOrderID);

	public String getVerkaeufer();

	public void setVerkaeufer(String verkaeufer);

	public String getProvisionsmodell();

	public void setProvisionsmodell(String provisionsmodell);

	public String getProvisionsart();

	public void setProvisionsart(String provisionsart);

	public String getArtikelnummer();

	public void setArtikelnummer(String artikelnummer);

	public String getArtikelbezeichnung();

	public void setArtikelbezeichnung(String artikelbezeichnung);

	public String getImei();

	public void setImei(String imei);

	public String getNettobetragEinzelEUR();

	public void setNettobetragEinzelEUR(String nettobetragEinzelEUR);

	public String getStornierungscode();

	public void setStornierungscode(String stornierungscode);

	public String getStornierungsgrund();

	public void setStornierungsgrund(String stornierungsgrund);

	public String getUrspruenglicheAbrechnungsnummer();

	public void setUrspruenglicheAbrechnungsnummer(String urspruenglicheAbrechnungsnummer);

	public String getKommentar();

	public void setKommentar(String kommentar);

	public String getKundennummer();

	public void setKundennummer(String kundennummer);

	public String getTransaktionstyp();

	public void setTransaktionstyp(String transaktionstyp);

	public String getTarifbeschreibung();

	public void setTarifbeschreibung(String tarifbeschreibung);

	public String getTarifcode();

	public void setTarifcode(String tarifcode);

	public String getPreisplan();

	public void setPreisplan(String preisplan);

	public String getZusatzdienstbeschreibung();

	public void setZusatzdienstbeschreibung(String zusatzdienstbeschreibung);

	public String getZusatzdienstcode();

	public void setZusatzdienstcode(String zusatzdienstcode);

	public String getHardwareLieferscheinNr();

	public void setHardwareLieferscheinNr(String hardwareLieferscheinNr);

	public String getHardwareLieferscheinDatum();

	public void setHardwareLieferscheinDatum(String hardwareLieferscheinDatum);

	public String getCallYaTyp();

	public void setCallYaTyp(String callYaTyp);

	public String getSimKartenNr();

	public void setSimKartenNr(String sIMKartenNr);

	public String getAuftragsVO();

	public void setAuftragsVO(String auftragsVO);

	public String getVertrag();

	public void setVertrag(String vertrag);

	Date getCancelationDate();
}
