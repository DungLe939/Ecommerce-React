package com.dungle939.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dungle939.ecommerce.dtos.DeliveryOptionDTO;
import com.dungle939.ecommerce.models.DeliveryOption;
import com.dungle939.ecommerce.repos.DeliveryOptionsRepo;

@Service
public class DeliveryOptionService {

    @Autowired
    private DeliveryOptionsRepo deliveryOptionsRepo;

    public List<DeliveryOptionDTO> getDeliveryOptions(boolean isExpand) {
        List<DeliveryOption> deliveryOptions = deliveryOptionsRepo.findAll();

        return deliveryOptions.stream().map(originalItem -> {
            DeliveryOptionDTO copyItem = new DeliveryOptionDTO();

            copyItem.setId(originalItem.getId());
            copyItem.setDeliveryDays(originalItem.getDeliveryDays());
            copyItem.setPriceCents(originalItem.getPriceCents());

            if (isExpand) {
                Long estimatedTime = System.currentTimeMillis() + ((long) originalItem.getDeliveryDays() * 86400000L);
                copyItem.setEstimatedDeliveryTimeMs(estimatedTime);
            }

            return copyItem;
        }).collect(Collectors.toList());
    }

    public DeliveryOption addDeliveryOption(DeliveryOption deliveryOption) {
        return deliveryOptionsRepo.save(deliveryOption);
    }
    
}
