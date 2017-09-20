package kstn.game.app.asset;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class AndroidAssetManager extends BaseAssetManager {
    private final Context context;
    public AndroidAssetManager(Context context) {
        this.context = context;
    }

    @Override
    public InputStream getInputStream(String assetName) throws IOException {
        AssetManager assetManager = context.getAssets();
        return assetManager.open(assetName);
    }
}
