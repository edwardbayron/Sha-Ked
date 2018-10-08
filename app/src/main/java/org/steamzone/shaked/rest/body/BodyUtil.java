package org.steamzone.shaked.rest.body;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BodyUtil {

    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
    public static final MediaType MEDIA_TYPE_MULTIPART = MediaType.parse("multipart/form-data");

    public static MultipartBody.Part textPart(String name, String data) {
        RequestBody b = RequestBody.create(MEDIA_TYPE_TEXT, data);
        MultipartBody.Part p = MultipartBody.Part.create(Headers.of("Content-Disposition", "form-data; title=\""+ name +"\""
//                ,"Content-Transfer-Encoding", "binary"
                )

                , b);


        return p;
    }

    public static MultipartBody.Builder getMultipartBuilder(){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MEDIA_TYPE_MULTIPART);
        return builder;
    }

}
