package com.grupo2.audiohelp;
import okhttp3.*;
import java.io.File;
import java.io.IOException;
public class WhisperConexion {
    private static final String API_KEY = "sk-svcacct-ROoTzJZk7m2Bi03P3R_T2vRHacmtOXvYfZ80odzX9pehqzHFe8FK9ml6nh4o44m7T3BlbkFJjtjOyUb4QNq4cYH1FRASERPRPXeZLh4K4TF7xrP_zzLOYi4nc-704cIrk9XIm5MA";
    private static final String BASE_URL = "https://api.openai.com/v1/audio/transcriptions";

    public static void transcribeAudio(String filePath) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("audio/mpeg");
        File file = new File(filePath);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(mediaType, file))
                .addFormDataPart("model", "whisper-1")
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    System.out.println(responseData);
                } else {
                    System.out.println("Error : " + response.message());
                }
            }
        });
    }
}
