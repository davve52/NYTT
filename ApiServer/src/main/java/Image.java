import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anders on 2016-10-19.
 */
public class Image {
    private Cloudinary cloudinary = new Cloudinary();
    private String imageName;
    public Image(){

        Map config = new HashMap();
        config.put("cloud_name", "mahgetrich");
        config.put("api_key", "484399541838424");
        config.put("api_secret", "2a0xEnaXfWvwVxKNFxU0eqWiVMk");
        cloudinary = new Cloudinary(config);

    }
    public String getImage(){
        String res = cloudinary.url().transformation(new Transformation().effect("blur:1500").width(400).height(550)).imageTag(imageName);
        return res;
    }
    public void uploadImage(String name){
        try {
            imageName = name.substring(43);
            String[] cutted = imageName.split("\\.");
            String finalImageName = cutted[0];
            Map params = ObjectUtils.asMap("public_id", finalImageName);
            Map uploadResult = cloudinary.uploader().upload(name, params);
            System.out.println(uploadResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
