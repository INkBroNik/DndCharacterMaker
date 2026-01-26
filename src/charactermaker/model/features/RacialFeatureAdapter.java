package charactermaker.model.features;

import com.google.gson.*;

import java.lang.reflect.Type;

public class RacialFeatureAdapter implements JsonDeserializer<RacialFeature>, JsonSerializer<RacialFeature> {


    @Override
    public RacialFeature deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) throw new JsonParseException("Expected object for RacialFeature");
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("stat") && jsonObject.has("value")) {
            return context.deserialize(json, StatBonusFeature.class);
        }
        if (jsonObject.has("groupId") &&  jsonObject.has("bonus")) {
            return context.deserialize(json , ChoiceStatBonusFeature.class);
        }
        throw new JsonParseException("Unknown RacialFeature shape: " + jsonObject);
    }

    @Override
    public JsonElement serialize(RacialFeature source, Type type, JsonSerializationContext context) {
        return context.serialize(source, source.getClass());
    }
}
