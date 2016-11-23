/**
 * Created by Goldbin on 02.11.2016.
 */
package comgoldbin.vk.geonotes;


import java.io.Serializable;

@SuppressWarnings("serial")
public class DBData implements Serializable{

    private long id;
    private String date;
    private String text;
    private String photo;
    private double geolat;
    private double geolon;

    public DBData(long id,String date,String text,String photo,double geolat,double geolon) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.photo = photo;
        this.geolat = geolat;
        this.geolon = geolon;
    }

    public long getID () {return id;}
    public String getDate() {return date;}
    public String getText() {return text;}
    public String getPhoto () {return photo;}
    public double getLat () {return geolat;}
    public double getLon () {return geolon;}
}