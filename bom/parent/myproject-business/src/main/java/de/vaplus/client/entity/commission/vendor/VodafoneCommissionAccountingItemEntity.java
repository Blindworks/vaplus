package de.vaplus.client.entity.commission.vendor;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.vaplus.api.entity.VodafoneCommissionAccountingItem;

@Entity
@Table(name="VodafoneCommissionAccountingItem")
public class VodafoneCommissionAccountingItemEntity extends VendorCommissionAccountingItemEntity implements VodafoneCommissionAccountingItem{

	private static final long serialVersionUID = 6758876253758602955L;
	

	@Column(name="VOIDAbrechnung")
	private String voIDAbrechnung;

	@Column(name="VOIDAktivierung")
	private String voIDAktivierung;

	@Column(name="AdressdatenVOIDAktivierung")
	private String adressdatenVOIDAktivierung;

	@Column(name="Provisionskategorie")
	private String provisionskategorie;

	@Column(name="DatumAuftrag")
	private String datumAuftrag;

	@Column(name="DatumPreClearing")
	private String datumPreClearing;

	@Column(name="DatumAktivierung")
	private String datumAktivierung;

	@Column(name="DatumStornierung")
	private String datumStornierung;

	@Column(name="Kundenname")
	private String kundenname;

	@Column(name="RufnummerBarcodeWebOrderID")
	private String rufnummerBarcodeWebOrderID;

	@Column(name="Verkaeufer")
	private String verkaeufer;

	@Column(name="Provisionsmodell")
	private String provisionsmodell;

	@Column(name="Provisionsart")
	private String provisionsart;

	@Column(name="Artikelnummer")
	private String artikelnummer;

	@Column(name="Artikelbezeichnung")
	private String artikelbezeichnung;

	@Column(name="IMEI")
	private String imei;

	@Column(name="NettobetragEinzelEUR")
	private String nettobetragEinzelEUR;

	@Column(name="Stornierungscode")
	private String stornierungscode;

	@Column(name="Stornierungsgrund")
	private String stornierungsgrund;

	@Column(name="UrspruenglicheAbrechnungsnummer")
	private String urspruenglicheAbrechnungsnummer;

	@Column(name="Kommentar")
	private String kommentar;

	@Column(name="Kundennummer")
	private String kundennummer;

	@Column(name="Transaktionstyp")
	private String transaktionstyp;

	@Column(name="Tarifbeschreibung")
	private String tarifbeschreibung;

	@Column(name="Tarifcode")
	private String tarifcode;

	@Column(name="Preisplan")
	private String preisplan;

	@Column(name="Zusatzdienstbeschreibung")
	private String zusatzdienstbeschreibung;

	@Column(name="Zusatzdienstcode")
	private String zusatzdienstcode;

	@Column(name="HardwareLieferscheinNr")
	private String hardwareLieferscheinNr;

	@Column(name="HardwareLieferscheinDatum")
	private String hardwareLieferscheinDatum;

	@Column(name="CallYaTyp")
	private String callYaTyp;

	@Column(name="SIMKartenNr")
	private String simKartenNr;

	@Column(name="AuftragsVO")
	private String auftragsVO;

	@Column(name="Vertrag")
	private String vertrag;

	public VodafoneCommissionAccountingItemEntity(){
		
	}
	
	public static Date getTimeRange(String csvLine){
		String[] csvFields = csvLine.split(";");
		String dateField;
		Date d = null;
		DateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" );
		
		if(csvFields[7].trim().length() == 8){
			dateField = csvFields[7].trim();
		}else{
			dateField = csvFields[6].trim();
		}
		
		try
		{
		  d  = formatter.parse( dateField );
		}
		catch ( ParseException e ) { 
			
		}
		
		return d;
	}
	
	public static String getAccountingVO(String csvLine){
		String[] csvFields = csvLine.split(";");
		
		return csvFields[0];
	}
	
	public VodafoneCommissionAccountingItemEntity(String csvLine){
		super(csvLine);
		
		String[] csvFields = csvLine.split(";");
		
		setVoIDAbrechnung(csvFields[0]);
		setVoIDAktivierung(csvFields[1]);
		setAdressdatenVOIDAktivierung(csvFields[2]);
		setProvisionskategorie(csvFields[3]);
		setDatumAuftrag(csvFields[4]);
		setDatumPreClearing(csvFields[5]);
		setDatumAktivierung(csvFields[6]);
		setDatumStornierung(csvFields[7]);
		setKundenname(csvFields[8]);
		setRufnummerBarcodeWebOrderID(csvFields[9]);
		setVerkaeufer(csvFields[10]);
		setProvisionsmodell(csvFields[11]);
		setProvisionsart(csvFields[12]);
		setArtikelnummer(csvFields[13]);
		setArtikelbezeichnung(csvFields[14]);
		setImei(csvFields[15]);
		setNettobetragEinzelEUR(csvFields[16]);
		setStornierungscode(csvFields[17]);
		setStornierungsgrund(csvFields[18]);
		setUrspruenglicheAbrechnungsnummer(csvFields[19]);
		setKommentar(csvFields[20]);
		setKundennummer(csvFields[21]);
		setTransaktionstyp(csvFields[22]);
		setTarifbeschreibung(csvFields[23]);
		setTarifcode(csvFields[24]);
		setPreisplan(csvFields[25]);
		setZusatzdienstbeschreibung(csvFields[26]);
		setZusatzdienstcode(csvFields[27]);
		setHardwareLieferscheinNr(csvFields[28]);
		setHardwareLieferscheinDatum(csvFields[29]);
		setCallYaTyp(csvFields[30]);
		setSimKartenNr(csvFields[31]);
		setAuftragsVO(csvFields[32]);
		setVertrag(csvFields[33]);
	}

	@Override
	public String getVoIDAbrechnung() {
		return voIDAbrechnung;
	}

	@Override
	public void setVoIDAbrechnung(String vOIDAbrechnung) {
		voIDAbrechnung = vOIDAbrechnung;
	}

	@Override
	public String getVoIDAktivierung() {
		return voIDAktivierung;
	}

	@Override
	public void setVoIDAktivierung(String vOIDAktivierung) {
		voIDAktivierung = vOIDAktivierung;
	}

	@Override
	public String getAdressdatenVOIDAktivierung() {
		return adressdatenVOIDAktivierung;
	}

	@Override
	public void setAdressdatenVOIDAktivierung(String adressdatenVOIDAktivierung) {
		this.adressdatenVOIDAktivierung = adressdatenVOIDAktivierung;
	}

	@Override
	public String getProvisionskategorie() {
		return provisionskategorie;
	}

	@Override
	public void setProvisionskategorie(String provisionskategorie) {
		this.provisionskategorie = provisionskategorie;
	}

	@Override
	public String getDatumAuftrag() {
		return datumAuftrag;
	}

	@Override
	public void setDatumAuftrag(String datumAuftrag) {
		this.datumAuftrag = datumAuftrag;
	}

	@Override
	public String getDatumPreClearing() {
		return datumPreClearing;
	}

	@Override
	public void setDatumPreClearing(String datumPreClearing) {
		this.datumPreClearing = datumPreClearing;
	}

	@Override
	public String getDatumAktivierung() {
		return datumAktivierung;
	}

	@Override
	public void setDatumAktivierung(String datumAktivierung) {
		this.datumAktivierung = datumAktivierung;
	}

	@Override
	public String getDatumStornierung() {
		return datumStornierung;
	}

	@Override
	public void setDatumStornierung(String datumStornierung) {
		this.datumStornierung = datumStornierung;
	}

	@Override
	public String getKundenname() {
		return kundenname;
	}

	@Override
	public void setKundenname(String kundenname) {
		this.kundenname = kundenname;
	}

	@Override
	public String getRufnummerBarcodeWebOrderID() {
		return rufnummerBarcodeWebOrderID;
	}

	@Override
	public void setRufnummerBarcodeWebOrderID(String rufnummerBarcodeWebOrderID) {
		this.rufnummerBarcodeWebOrderID = rufnummerBarcodeWebOrderID;
	}

	@Override
	public String getVerkaeufer() {
		return verkaeufer;
	}

	@Override
	public void setVerkaeufer(String verkaeufer) {
		this.verkaeufer = verkaeufer;
	}

	@Override
	public String getProvisionsmodell() {
		return provisionsmodell;
	}

	@Override
	public void setProvisionsmodell(String provisionsmodell) {
		this.provisionsmodell = provisionsmodell;
	}

	@Override
	public String getProvisionsart() {
		return provisionsart;
	}

	@Override
	public void setProvisionsart(String provisionsart) {
		this.provisionsart = provisionsart;
	}

	@Override
	public String getArtikelnummer() {
		return artikelnummer;
	}

	@Override
	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}

	@Override
	public String getArtikelbezeichnung() {
		return artikelbezeichnung;
	}

	@Override
	public void setArtikelbezeichnung(String artikelbezeichnung) {
		this.artikelbezeichnung = artikelbezeichnung;
	}

	@Override
	public String getImei() {
		return imei;
	}

	@Override
	public void setImei(String imei) {
		this.imei = imei;
	}

	@Override
	public String getNettobetragEinzelEUR() {
		return nettobetragEinzelEUR;
	}

	@Override
	public void setNettobetragEinzelEUR(String nettobetragEinzelEUR) {
		this.nettobetragEinzelEUR = nettobetragEinzelEUR;
	}

	@Override
	public String getStornierungscode() {
		return stornierungscode;
	}

	@Override
	public void setStornierungscode(String stornierungscode) {
		this.stornierungscode = stornierungscode;
	}

	@Override
	public String getStornierungsgrund() {
		return stornierungsgrund;
	}

	@Override
	public void setStornierungsgrund(String stornierungsgrund) {
		this.stornierungsgrund = stornierungsgrund;
	}

	@Override
	public String getUrspruenglicheAbrechnungsnummer() {
		return urspruenglicheAbrechnungsnummer;
	}

	@Override
	public void setUrspruenglicheAbrechnungsnummer(
			String urspruenglicheAbrechnungsnummer) {
		this.urspruenglicheAbrechnungsnummer = urspruenglicheAbrechnungsnummer;
	}

	@Override
	public String getKommentar() {
		return kommentar;
	}

	@Override
	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	@Override
	public String getKundennummer() {
		return kundennummer;
	}

	@Override
	public void setKundennummer(String kundennummer) {
		this.kundennummer = kundennummer;
	}

	@Override
	public String getTransaktionstyp() {
		return transaktionstyp;
	}

	@Override
	public void setTransaktionstyp(String transaktionstyp) {
		this.transaktionstyp = transaktionstyp;
	}

	@Override
	public String getTarifbeschreibung() {
		return tarifbeschreibung;
	}

	@Override
	public void setTarifbeschreibung(String tarifbeschreibung) {
		this.tarifbeschreibung = tarifbeschreibung;
	}

	@Override
	public String getTarifcode() {
		return tarifcode;
	}

	@Override
	public void setTarifcode(String tarifcode) {
		this.tarifcode = tarifcode;
	}

	@Override
	public String getPreisplan() {
		return preisplan;
	}

	@Override
	public void setPreisplan(String preisplan) {
		this.preisplan = preisplan;
	}

	@Override
	public String getZusatzdienstbeschreibung() {
		return zusatzdienstbeschreibung;
	}

	@Override
	public void setZusatzdienstbeschreibung(String zusatzdienstbeschreibung) {
		this.zusatzdienstbeschreibung = zusatzdienstbeschreibung;
	}

	@Override
	public String getZusatzdienstcode() {
		return zusatzdienstcode;
	}

	@Override
	public void setZusatzdienstcode(String zusatzdienstcode) {
		this.zusatzdienstcode = zusatzdienstcode;
	}

	@Override
	public String getHardwareLieferscheinNr() {
		return hardwareLieferscheinNr;
	}

	@Override
	public void setHardwareLieferscheinNr(String hardwareLieferscheinNr) {
		this.hardwareLieferscheinNr = hardwareLieferscheinNr;
	}

	@Override
	public String getHardwareLieferscheinDatum() {
		return hardwareLieferscheinDatum;
	}

	@Override
	public void setHardwareLieferscheinDatum(String hardwareLieferscheinDatum) {
		this.hardwareLieferscheinDatum = hardwareLieferscheinDatum;
	}

	@Override
	public String getCallYaTyp() {
		return callYaTyp;
	}

	@Override
	public void setCallYaTyp(String callYaTyp) {
		this.callYaTyp = callYaTyp;
	}

	@Override
	public String getSimKartenNr() {
		return simKartenNr;
	}

	@Override
	public void setSimKartenNr(String simKartenNr) {
		this.simKartenNr = simKartenNr;
	}

	@Override
	public String getAuftragsVO() {
		return auftragsVO;
	}

	@Override
	public void setAuftragsVO(String auftragsVO) {
		this.auftragsVO = auftragsVO;
	}

	@Override
	public String getVertrag() {
		return vertrag;
	}

	@Override
	public void setVertrag(String vertrag) {
		this.vertrag = vertrag;
	}

	@Override
	public BigDecimal getCommissionWorth() {
		try{
			return new BigDecimal(nettobetragEinzelEUR.replace(",", ".").trim());
		}catch(NumberFormatException e){
			return new BigDecimal(0);
		}
	}

	@Override
	public String getTitle() {
		return getProvisionsart()+" | "+getProvisionsmodell();
	}

	@Override
	public Date getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		try {
			return formatter.parse(getDatumAktivierung());
		} catch (ParseException e) {
			System.out.println("Error while parsing DatumAktivierung in Vodafone Accounting Item: "+e.getMessage());

			System.out.println("vItem ID: "+this.getId());
			System.out.println("vItem RN: "+this.getRufnummerBarcodeWebOrderID());
			System.out.println("vItem DatumAktivierung: "+this.getDatumAktivierung());
			
			return null;
		}
	}
	
	@Override
	public Date getCancelationDate() {
		
		if(this.getDatumStornierung().trim().length() == 0)
			return null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		try {
			return formatter.parse(this.getDatumStornierung());
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public boolean isCancelation() {
		if(this.getDatumStornierung().trim().length() > 0)
			return true;
		else 
			return false;
	}
	

	

}
