package se.mah.couchpotato;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;

/**
 * CUSTOM MOTHADANFUCKING DESERLAIZING THE TV SHOWS LIKE A GOD DAMN MOTHA FUCKA
 * GOOD FUCKING YARD
 */

public class CustomTvShowDeserializer extends StdDeserializer<TvShow> {

    public static final String NAME = "name";
    public static final String LANGUAGE = "language";
    public static final String OFFICIAL_SITE = "officialSite";
    public static final String IMAGE = "image";
    public static final String ORIGINAL = "original";
    public static final String MEDIUM = "medium";
    public static final String GENRES = "genres";
    public static final String ID = "id";
    public static final String SHOW = "show";

    public CustomTvShowDeserializer(){
        this(null);
    }

    @Override
    public TvShow deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec codec = p.getCodec();
        JsonNode rootNode = codec.readTree(p);
        Log.d("CUSTOMTVSHOWDESERIALIZ", rootNode.toString());

        if(rootNode.get(SHOW) != null){
            rootNode = rootNode.get(SHOW);
        }

        TvShow newShow = new TvShow();
        try{
            int id = (int) rootNode.get(ID).numberValue();
            Log.d("ID", "" + id);
            String language = rootNode.get(LANGUAGE).toString();
            String officalString = rootNode.get(OFFICIAL_SITE).toString();
            String name = rootNode.get(NAME).toString();
            String[] pictureUrls = new String[2];
            pictureUrls[0] = rootNode.get(IMAGE).get(ORIGINAL).toString();
            pictureUrls[1] = rootNode.get(IMAGE).get(MEDIUM).toString();
            String genres = rootNode.get(GENRES).toString().replace("[", "").replace("]", "");
            String[] allgenres = genres.split(",");

            newShow.setName(name);
            newShow.setGenres(allgenres);
            newShow.setLanguage(language);
            newShow.setId(id);
            newShow.setOfficalSite(officalString);
            newShow.setPictureUrl(pictureUrls);



        }catch(Exception e){
            Log.d("CUSTOMDESERALIZER", "ERROR WHEN PARSING JSON");
        }

        return newShow;

    }

    public CustomTvShowDeserializer(Class<?> vc) {
        super(vc);
    }
}
