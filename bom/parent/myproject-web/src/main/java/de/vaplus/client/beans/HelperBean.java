package de.vaplus.client.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import de.vaplus.client.converter.MinimalDateTimeConverter;
import de.vaplus.client.converter.NamedDateTimeConverter;


@SessionScoped
@ManagedBean(name="helperBean")
public class HelperBean implements Serializable {
	
	@Inject
	private FacesContext facesContext;
	
	private NamedDateTimeConverter namedDateTimeConverter;
	
	private MinimalDateTimeConverter minimalDateTimeConverter;

	private final String[] monthNames = {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
	
	
	public NamedDateTimeConverter getConvertDate() {
		if(namedDateTimeConverter == null){
			namedDateTimeConverter = new NamedDateTimeConverter();
//			convertDate.setPattern("EEEEEEEE, MMM dd, yyyy");
		}
	    return namedDateTimeConverter;
	}
	
	public MinimalDateTimeConverter getConvertTime() {
		if(minimalDateTimeConverter == null){
			minimalDateTimeConverter = new MinimalDateTimeConverter();
//			convertDate.setPattern("EEEEEEEE, MMM dd, yyyy");
		}
	    return minimalDateTimeConverter;
	}
	
//	public void checkDB() throws IOException {
//		if(! dbController.isDBUpToDate()){
//			facesContext.getExternalContext().redirect("/error/databaseError.xhtml");
//		}
//	}

	
	public int currentYear(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR);
	}
	
	public int currentMonth(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.MONTH);
	}

	public HelperBean() {
		// TODO Auto-generated constructor stub
	}


	public String getCssColorAsHex(String colorName){
		return getCssColorMap().get(colorName);
	}
	
	public List<String> getCssColor(){
		final List<String> colors = new LinkedList<String>();

		colors.add("waxflower");
		colors.add("newyorkpink");
		colors.add("sunglo");
		colors.add("chestnutrose");
		colors.add("flamingo");
		colors.add("sunsetorange");
		colors.add("pomegranate");
		colors.add("monza");
		colors.add("valencia");
		colors.add("tallpoppy");
		colors.add("oldbrick");
		colors.add("radicalred");
		colors.add("razzmatazz");
		colors.add("cabaret");
		colors.add("snuff");
		colors.add("wistful");
		colors.add("lightwisteria");
		colors.add("mediumpurple");
		colors.add("wisteria");
		colors.add("seance");
		colors.add("studio");
		colors.add("plum");
		colors.add("rebeccapurple");
		colors.add("honeyflower");
		colors.add("aliceblue");
		colors.add("jordyblue");
		colors.add("dodgerblue");
		colors.add("pictonblue");
		colors.add("curiousblue");
		colors.add("royalblue");
		colors.add("steelblue");
		colors.add("chambray");
		colors.add("jacksonspurple");
		colors.add("hummingbird");
		colors.add("spray");
		colors.add("shakespeare");
		colors.add("summersky");
		colors.add("jellybean");
		colors.add("fountainblue");
		colors.add("hoki");
		colors.add("aquaisland");
		colors.add("riptide");
		colors.add("mediumturquoise");
		colors.add("turquoise");
		colors.add("caribbeangreen");
		colors.add("downy");
		colors.add("lightseagreen");
		colors.add("madang");
		colors.add("silvertree");
		colors.add("mountainmeadow");
		colors.add("mediumaquamarine");
		colors.add("emerald");
		colors.add("junglegreen");
		colors.add("oceangreen");
		colors.add("freespeechaquamarine");
		colors.add("niagara");
		colors.add("observatory");
		colors.add("gossip");
		colors.add("shamrock");
		colors.add("jade");
		colors.add("eucalyptus");
		colors.add("salem");
		colors.add("capehoney");
		colors.add("creamcan");
		colors.add("saffron");
		colors.add("sandstorm");
		colors.add("casablanca");
		colors.add("lightningyellow");
		colors.add("buttercup");
		colors.add("california");
		colors.add("firebush");
		colors.add("zest");
		colors.add("crusta");
		colors.add("jaffa");
		colors.add("ecstasy");
		colors.add("burntorange");
		colors.add("whitesmoke");
		colors.add("iron");
		colors.add("pumice");
		colors.add("silversand");
		colors.add("silver");
		colors.add("edward");
		colors.add("cascade");
		colors.add("lynch");
		
		return colors;
	}
		
	public Map<String,String> getCssColorMap(){
		final Map<String, String> colors = new HashMap<String, String>();

//		colors.put("turquoise", "#1ABC9C");
//		colors.put("emerland", "#2ECC71");
//		colors.put("peter-river", "#3498DB");
//		colors.put("amethyst", "#9B59B6");
//		colors.put("wet-asphalt", "#34495E");
//
//		colors.put("green-sea", "#16A085");
//		colors.put("nephritis", "#27AE60");
//		colors.put("belize-hole", "#2980B9");
//		colors.put("wisteria", "#8E44AD");
//		colors.put("midnight-blue", "#2C3E50");
//
//		colors.put("sun-flower", "#F1C40F");
//		colors.put("carrot", "#E67E22");
//		colors.put("alizarin", "#E74C3C");
//		colors.put("clouds", "#ECF0F1");
//		colors.put("concrete", "#95A5A6");
//
//		colors.put("orange", "#F39C12");
//		colors.put("pumpkin", "#D35400");
//		colors.put("pomegranate", "#C0392B");
//		colors.put("silver", "#BDC3C7");
//		colors.put("asbestos", "#7F8C8D");

		colors.put("chestnutrose", "#D24D57");

		colors.put("pomegranate", "#F22613");

		colors.put("thunderbird", "#D91E18");

		colors.put("oldbrick", "#96281B");

		colors.put("flamingo", "#EF4836");

		colors.put("valencia", "#D64541");

		colors.put("tallpoppy", "#C0392B");

		colors.put("monza", "#CF000F");

		colors.put("cinnabar", "#E74C3C");

		colors.put("razzmatazz", "#DB0A5B");

		colors.put("sunsetorange", "#F64747");

		colors.put("waxflower", "#F1A9A0");

		colors.put("cabaret", "#D2527F");

		colors.put("newyorkpink", "#E08283");

		colors.put("radicalred", "#F62459");

		colors.put("sunglo", "#E26A6A");

		colors.put("snuff", "#DCC6E0");

		colors.put("rebeccapurple", "#663399");

		colors.put("honeyflower", "#674172");

		colors.put("wistful", "#AEA8D3");

		colors.put("plum", "#913D88");

		colors.put("seance", "#9A12B3");

		colors.put("mediumpurple", "#BF55EC");

		colors.put("lightwisteria", "#BE90D4");

		colors.put("studio", "#8E44AD");

		colors.put("wisteria", "#9B59B6");

		colors.put("sanmarino", "#446CB3");

		colors.put("aliceblue", "#E4F1FE");

		colors.put("royalblue", "#4183D7");

		colors.put("pictonblue", "#59ABE3");

		colors.put("spray", "#81CFE0");

		colors.put("shakespeare", "#52B3D9");

		colors.put("hummingbird", "#C5EFF7");

		colors.put("pictonblue", "#22A7F0");

		colors.put("curiousblue", "#3498DB");

		colors.put("madison", "#2C3E50");

		colors.put("dodgerblue", "#19B5FE");

		colors.put("ming", "#336E7B");

		colors.put("ebonyclay", "#22313F");

		colors.put("malibu", "#6BB9F0");

		colors.put("summersky", "#1E8BC3");

		colors.put("chambray", "#3A539B");

		colors.put("pickledbluewood", "#34495E");

		colors.put("hoki", "#67809F");

		colors.put("jellybean", "#2574A9");

		colors.put("jacksonspurple", "#1F3A93");

		colors.put("jordyblue", "#89C4F4");

		colors.put("steelblue", "#4B77BE");

		colors.put("fountainblue", "#5C97BF");

		colors.put("mediumturquoise", "#4ECDC4");

		colors.put("aquaisland", "#A2DED0");

		colors.put("gossip", "#87D37C");

		colors.put("darkseagreen", "#90C695");

		colors.put("eucalyptus", "#26A65B");

		colors.put("caribbeangreen", "#03C9A9");

		colors.put("silvertree", "#68C3A3");

		colors.put("downy", "#65C6BB");

		colors.put("mountainmeadow", "#1BBC9B");

		colors.put("lightseagreen", "#1BA39C");

		colors.put("mediumaquamarine", "#66CC99");

		colors.put("turquoise", "#36D7B7");

		colors.put("madang", "#C8F7C5");

		colors.put("riptide", "#86E2D5");

		colors.put("shamrock", "#2ECC71");

		colors.put("niagara", "#16a085");

		colors.put("emerald", "#3FC380");

		colors.put("greenhaze", "#019875");

		colors.put("freespeechaquamarine", "#03A678");

		colors.put("oceangreen", "#4DAF7C");

		colors.put("niagara1", "#2ABB9B");

		colors.put("jade", "#00B16A");

		colors.put("salem", "#1E824C");

		colors.put("observatory", "#049372");

		colors.put("junglegreen", "#26C281");

		colors.put("creamcan", "#F5D76E");

		colors.put("ripelemon", "#F7CA18");

		colors.put("saffron", "#F4D03F");

		colors.put("capehoney", "#FDE3A7");

		colors.put("california", "#F89406");

		colors.put("firebush", "#EB9532");

		colors.put("tahitigold", "#E87E04");

		colors.put("casablanca", "#F4B350");

		colors.put("crusta", "#F2784B");

		colors.put("seabuckthorn", "#EB974E");

		colors.put("lightningyellow", "#F5AB35");

		colors.put("burntorange", "#D35400");

		colors.put("buttercup", "#F39C12");

		colors.put("ecstasy", "#F9690E");

		colors.put("sandstorm", "#F9BF3B");

		colors.put("jaffa", "#F27935");

		colors.put("zest", "#E67E22");

		colors.put("whitesmoke", "#ececec");

		colors.put("lynch", "#6C7A89");

		colors.put("pumice", "#D2D7D3");

		colors.put("gallery", "#EEEEEE");

		colors.put("silversand", "#BDC3C7");

		colors.put("porcelain", "#ECF0F1");

		colors.put("cascade", "#95A5A6");

		colors.put("iron", "#DADFE1");

		colors.put("edward", "#ABB7B7");

		colors.put("cararra", "#F2F1EF");

		colors.put("silver", "#BFBFBF");
		
		return colors;
	}
	
//	public String getCssColorByIndex(int id){
//		return getCssColor().get(id);
//	}
	
	public String getMonthShortName(int id){
		String[] monthNames = {"Jan", "Feb", "Mrz", "Apr", "May", "Jun", "Jul", "Aug", "Sep",  "Okt",  "Nov", "Dez"};
		return monthNames[id];
	}
	
	public String getBaseURL(){
		HttpServletRequest servletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		
		String baseURL = servletRequest.getScheme() + "://"+ servletRequest.getServerName();
		
		if(servletRequest.getServerName().equalsIgnoreCase("localhost")){
			baseURL += ":"+servletRequest.getServerPort();
		}
		
		if(servletRequest.getContextPath().length() > 0)
			baseURL += "/"+servletRequest.getContextPath();
			
		return baseURL;
	}
	
	public String minToString(long min){
		return minToString(min, false);
	}
		
	public String minToString(long min, boolean addPlus){
			
		String hourString = null;
		
		int hours = (int) (min / 60);
		int minutes = (int) (min % 60);

		if(addPlus && min > 0)
			hourString = "+"+ hours;
		else
			hourString = ""+ hours;
		
		hourString += ":";
		
		if(minutes < 0)
			minutes *= -1;
		
		if(minutes < 10)
			hourString += "0"+minutes;
		else
			hourString += ""+ minutes;
		

		return hourString;
	}
	
	public int prevYear(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -1);
		return c.get(Calendar.YEAR);
	}
	
	public int thisYear(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR);
	}
	
	public int nextYear(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, 1);
		return c.get(Calendar.YEAR);
	}
	
	public List<Integer> getMonthList(){
		

	    List<Integer> monthList = new LinkedList<Integer>();
		
		for(int i = 0; i <= 11; i++){
			monthList.add(i);
		}
		
		return monthList;
	}
	
	public String getMonthName(int month){
		return monthNames[month];
	}

	public int getLimitedPercentage(BigDecimal piece, BigDecimal cake){
		

		piece = piece.setScale(4, BigDecimal.ROUND_HALF_UP);
		cake = cake.setScale(4, BigDecimal.ROUND_HALF_UP);
		
		
		if(piece == null || cake == null)
			return 0;
		
		if(cake.compareTo(new BigDecimal(0)) == 0)
			return 0;
		
		if(piece.compareTo(new BigDecimal(0)) == 0)
			return 0;

		int p = piece.divide(cake, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

		if(p > 100)
			return 100;
		if(p < 0)
			return 0;
		return p;
	}
	
	public String getFileSize(long size){
		
		if(size == 0)
			return "";
		
		else if(size > 1024 * 1024 * 1024){
			return (size / (1024 * 1024 * 1024) )  + " GB";
		}
		else if(size > 1024 * 1024){
			return (size / (1024 * 1024) )  + " MB";
		}
		else if(size > 1024){
			return (size / (1024) )  + " KB";
		}
		else{
			return size + " B";
		}
	}
	
	public String nl2br(String s){
		return s.replaceAll("\n", "<br/>");
	}
	
	public Integer[] getCurrentMonthRange(){
		
		Integer[] data = new Integer[12];
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -9);

		for(int i = 0; i < data.length; i++) {
			
			data[i] = c.get(Calendar.MONTH);
			c.add(Calendar.MONTH, 1);
			
		}
		
		return data;
	}
}