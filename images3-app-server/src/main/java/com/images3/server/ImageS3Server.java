package com.images3.server;

import com.images3.ImagePlantInput;
import com.images3.ImagePlantOutput;
import com.images3.ImageS3;
import com.images3.UserAccountOutput;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantFactory;
import com.images3.core.ImagePlantRepository;
import com.images3.core.UserAccount;
import com.images3.core.UserAccountRepository;

public class ImageS3Server implements ImageS3 {
    
    private UserAccountRepository userAccountRepository;
    private ImagePlantFactory imagePlantFactory;
    private ImagePlantRepository imagePlantRepository;


    public ImagePlantOutput createImagePlant(ImagePlantInput input) {
        UserAccount account = userAccountRepository.findUserAccountById(input.getUserAccountId());
        ImagePlant imagePlant = imagePlantFactory.generateImagePlant(account, input.getName());
        imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        return new ImagePlantOutput(
                imagePlant.getId(), 
                imagePlant.getName(), 
                imagePlant.getCreationTime(), 
                mapToUserAccountOutput(account));
    }
    
    private UserAccountOutput mapToUserAccountOutput(UserAccount userAccount) {
        return new UserAccountOutput(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getEmail());
    }

}
