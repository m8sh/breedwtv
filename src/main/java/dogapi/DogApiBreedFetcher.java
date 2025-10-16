package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        final String url = "https://dog.ceo/api/breeds/list/all";
        final Request request = new Request.Builder().url(url).get().build();

        try (Response response = client.newCall(request).execute()){
            final String body = response.body().string();
            final JSONObject jsonObject = new JSONObject(body);

            final JSONObject message = jsonObject.getJSONObject("message");

            final JSONArray arr = message.getJSONArray(breed.toLowerCase(Locale.ROOT));

            final List<String> subBreeds = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                subBreeds.add(arr.getString(i));
            }
            return subBreeds;

        } catch (IOException | JSONException e) {
            throw new BreedNotFoundException(breed);
        }
    }
}