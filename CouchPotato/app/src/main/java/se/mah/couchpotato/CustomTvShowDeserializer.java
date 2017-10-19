package se.mah.couchpotato;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
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
        int id;
        String language, officalString, name, genres;
        String[] pictureUrls, allgenres;
        TvShow newShow = new TvShow();
        try{

            id = (int) rootNode.get(ID).numberValue();
            name = rootNode.get(NAME).toString();

            if(rootNode.has(LANGUAGE)) {
                language = rootNode.get(LANGUAGE).toString();
                newShow.setLanguage(language);
            }
            if(rootNode.has(OFFICIAL_SITE)) {
                officalString = rootNode.get(OFFICIAL_SITE).toString();
                newShow.setOfficalSite(officalString);
            }

            if (rootNode.get(IMAGE).size() != 0) {
                pictureUrls = new String[2];
                rootNode.get(IMAGE).get(ORIGINAL).toString();
                rootNode.get(IMAGE).get(MEDIUM).toString();
                newShow.setPictureUrl(pictureUrls);
            }

            if(rootNode.get(GENRES).size() != 0) {
                genres = rootNode.get(GENRES).toString().replace("[", "").replace("]", "");
                allgenres = genres.split(",");
                newShow.setGenres(allgenres);
            }

            newShow.setName(name);
            newShow.setId(id);


        }catch(Exception e){
            Log.d("CUSTOMDESERALIZER", "ERROR WHEN PARSING JSON");
        }

        return newShow;

    }

    public CustomTvShowDeserializer(Class<?> vc) {
        super(vc);
    }
}
