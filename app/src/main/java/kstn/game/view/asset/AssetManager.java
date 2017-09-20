package kstn.game.view.asset;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;

public interface AssetManager {
    Bitmap getBitmap(String name) throws IOException;

    String getText(String name) throws IOException;

    InputStream getInputStream(String name) throws IOException;
}
