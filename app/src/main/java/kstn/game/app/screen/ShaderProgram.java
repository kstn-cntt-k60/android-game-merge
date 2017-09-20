package kstn.game.app.screen;

import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ShaderProgram {
    private int program = -1;
    private String vsSource, fsSource;

    public ShaderProgram(InputStream vs, InputStream fs) {
        try {
            vsSource = getFromInputStream(vs);
            fsSource = getFromInputStream(fs);
        } catch (IOException e) {
            assert (false);
            return;
        }
    }

    public void onSurfaceCreated() {
        final int[] isCompiled = new int[1];
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vsSource);
        GLES20.glCompileShader(vertexShader);
        GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, isCompiled, 0);
        if (isCompiled[0] == GLES20.GL_FALSE) {
            String errorMessage = GLES20.glGetShaderInfoLog(vertexShader);
            Log.e("ShaderProgram", errorMessage);
            GLES20.glDeleteShader(vertexShader);
            throw new RuntimeException("Can't compile vertex shader");
        }

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fsSource);
        GLES20.glCompileShader(fragmentShader);
        GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, isCompiled, 0);
        if (isCompiled[0] == GLES20.GL_FALSE) {
            String errorMessage = GLES20.glGetShaderInfoLog(fragmentShader);
            GLES20.glDeleteShader(fragmentShader);
            Log.e("ShaderProgram", errorMessage);
            throw new RuntimeException("Can't compile fragment shader");
        }

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        final int[] isLinked = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, isLinked, 0);
        if (isLinked[0] == 0) {
            String errorMessage = GLES20.glGetProgramInfoLog(program);
            GLES20.glDeleteProgram(program);
            program = -1;
            GLES20.glDeleteShader(vertexShader);
            GLES20.glDeleteShader(fragmentShader);
            Log.e("ShaderProgram", errorMessage);
            throw new RuntimeException("Can't link shaders");
        }
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        Log.d("ShaderProgram", "program: " + program);
    }

    public void use() {
        GLES20.glUseProgram(program);
    }

    public int getUniformLocation(String name) {
        return GLES20.glGetUniformLocation(program, name);
    }

    public int getAttributeLocation(String name) {
        return GLES20.glGetAttribLocation(program, name);
    }

    private String getFromInputStream(InputStream in) throws IOException {
        final int bufferSize = 1024;
        final byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int length;
        while ((length = in.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString();
    }
}
