package se.mah.couchpotato;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Iterator;

/**
 * CUSTOM MOTHADANFUCKING DESERLAIZING THE TV SHOWS LIKE A GOD DAMN MOTHA FUCKA
 * GOOD FUCKING YARD
 */

public class CustomTvShowDeserializer extends StdDeserializer<TvShow> {

    public CustomTvShowDeserializer(){
        this(null);
    }

    @Override
    public TvShow deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec codec = p.getCodec();
        JsonNode rootNode = codec.readTree(p);

        TvShow newShow = new TvShow();
        try{
            JsonNode node = rootNode.get("show");
            String title = (String) node.get("title").toString();
            int id = (int) node.get("id").numberValue();
            String language = (String) node.get("language").toString();
            String officalString = (String) node.get("officalSite").toString();
            String name = (String) node.get("name").toString();
            ArrayNode arr = (ArrayNode) node.get("images");
            String[] pictureUrl = new String[2];
            Iterator<JsonNode> imageIterator = arr.elements();
            int i = 0;
            while(imageIterator.hasNext()){
                JsonNode imageNode = imageIterator.next();
                if(i % 2 == 0)
                    pictureUrl[i] =  imageNode.get("medium").toString();
                else
                    pictureUrl[i] = imageNode.get("original").toString();

                i++;
            }
            i = 0;

            ArrayNode temp = (ArrayNode) node.get("genres");
            String[] genres = new String[temp.size()];
            Iterator<JsonNode> genresIterator = arr.elements();
            while(genresIterator.hasNext()){
                JsonNode genre = genresIterator.next();
                genres[i] = genre.toString();
                i++;
            }

            newShow.setName(name);
            newShow.setGenres(genres);
            newShow.setLanguage(language);
            newShow.setId(id);
            newShow.setOfficalSite(officalString);
            newShow.setPictureUrl(pictureUrl);



        }catch(Exception e){
            Log.d("CUSTOMDESERALIZER", "ERROR WHEN PARSING JSON");
        }

        return newShow;

    }

    public CustomTvShowDeserializer(Class<?> vc) {
        super(vc);
    }
}
