package com.example.core.util;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    public static byte[] downscaleImage(String imageData, float scale,String formatName){
        byte[] image = Base64.getDecoder().decode(imageData);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image));
            int newWidth = (int) (originalImage.getWidth() * scale);
            int newHeight = (int) (originalImage.getHeight() * scale);
            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
            scaledImage.getGraphics().drawImage(originalImage, 0, 0, newWidth, newHeight, null);

            ImageIO.write(scaledImage, formatName, bos);
        } catch (IOException e) {
            throw new CoreException(ErrorCode.UNEXPECTED_ERROR);
        }
        return bos.toByteArray();
    }
}
