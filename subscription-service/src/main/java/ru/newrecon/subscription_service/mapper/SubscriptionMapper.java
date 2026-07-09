package ru.newrecon.subscription_service.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.newrecon.subscription_service.config.MapstructConfig;
import ru.newrecon.subscription_service.dto.CreateSubscriptionRq;
import ru.newrecon.subscription_service.dto.CreateSubscriptionRs;
import ru.newrecon.subscription_service.dto.GetSubscriptionRs;
import ru.newrecon.subscription_service.dto.UpdateSubscriptionRq;
import ru.newrecon.subscription_service.dto.UpdateSubscriptionRs;
import ru.newrecon.subscription_service.entity.Subscription;

@Mapper(config = MapstructConfig.class)
public interface SubscriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    Subscription map(CreateSubscriptionRq source);

    @Mapping(target = "createAt", ignore = true)
    Subscription map(UUID id, UpdateSubscriptionRq source);

    CreateSubscriptionRs mapToCreateSubscriptionRs(Subscription source);

    UpdateSubscriptionRs mapToUpdateSubscriptionRs(Subscription source);

    GetSubscriptionRs mapToGetSubscriptionRs(Subscription source);
}
