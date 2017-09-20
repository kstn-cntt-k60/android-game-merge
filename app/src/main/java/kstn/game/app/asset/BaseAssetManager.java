package kstn.game.app.asset;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import kstn.game.view.asset.AssetManager;

public abstract class BaseAssetManager implements AssetManager {
    private final Map<String, Object> cache = new HashMap<String, Object>();

    @Override
    public Bitmap getBitmap(String name) throws IOException {
        Object object = cache.get(name);
        if (object == null) {
            Bitmap result = BitmapFactory.decodeStream(getInputStream(name));
            if (result == null)
                throw new IOException("Can't decode into Bitmap");
            cache.put(name, result);
            return result;
        }
        else {
            return (Bitmap)object;
        }
    }

    @Override
    public String getText(String name) throws IOException {
        Object object = cache.get(name);
        if (object == null) {
            InputStream inputStream = getInputStream(name);
            String result = getStringFromInputStream(inputStream);
            cache.put(name, result);
            return result;
        }
        else {
            return (String)object;
        }
    }

    private String getStringFromInputStream(InputStream in) throws IOException {
        final int bufferSize = 1024;
        final byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int length;
        while ((length = in.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString();
    }

    public abstract InputStream getInputStream(String name) throws IOException;
}
