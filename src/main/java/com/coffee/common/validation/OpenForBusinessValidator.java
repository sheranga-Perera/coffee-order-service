package com.coffee.common.validation;

import com.coffee.common.entity.Shop;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class OpenForBusinessValidator implements ConstraintValidator<OpenForBusiness, Shop> {

    @Override
    public void initialize(OpenForBusiness constraintAnnotation) {
    }

    @Override
    public boolean isValid(Shop shop, ConstraintValidatorContext context) {
        if (shop == null || shop.getOpenTime() == null || shop.getCloseTime() == null) {
            return false;
        }

        LocalTime currentTime = LocalTime.now();
        boolean isOpen = !currentTime.isBefore(shop.getOpenTime()) && 
                        !currentTime.isAfter(shop.getCloseTime());

        if (!isOpen) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Shop is currently closed. Operating hours: " + 
                    shop.getOpenTime() + " - " + shop.getCloseTime())
                    .addConstraintViolation();
        }

        return isOpen;
    }
} 